package com.project.bookcycle.filter;

import com.project.bookcycle.components.JwtTokenUtil;
import com.project.bookcycle.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter { // //Lớp OncePerRequestFilter: Xử lý hoạt động chạy một lần duy nhất cho mỗi yêu cầu HTTP
    @Value("${api.prefix}")
    private String apiPrefix;
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(isPassToken(request)){
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }

        final String token = authHeader.substring(7);
        final String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
        if(phoneNumber != null &&
                SecurityContextHolder.getContext().getAuthentication() == null){
            User user = (User) userDetailsService.loadUserByUsername(phoneNumber);
            if(jwtTokenUtil.validateToken(token, user)){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // Thiết lập các thông tin chi tiết về việc xác thực người dùng
                // Các thông tin chi tiết bao gồm: Các yêu cầu từ web, địa chỉ IP người dùng,...
                SecurityContextHolder.getContext().setAuthentication(authenticationToken); // Xác thực người dùng.
            }
        }
        filterChain.doFilter(request, response);
    }
    private boolean isPassToken(@NonNull HttpServletRequest request){
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of(String.format("%s/products", apiPrefix), "GET"),
                Pair.of(String.format("%s/categories", apiPrefix), "GET"),
                Pair.of(String.format("%s/users/register", apiPrefix), "POST"),
                Pair.of(String.format("%s/users/login", apiPrefix), "POST"),
                Pair.of(String.format("%s/users/images", apiPrefix), "GET"),
                Pair.of("/notify", "GET")
        ); // Tạo danh sách các api được lọc qua mà không cần token hay đăng nhập, đăng ký.
        for(Pair<String, String> bypassToken : bypassTokens){
            if (request.getServletPath().contains(bypassToken.getFirst()) &&
                    request.getMethod().equals(bypassToken.getSecond())) {
                return true;
            }
        }
        return false;
    }

}
