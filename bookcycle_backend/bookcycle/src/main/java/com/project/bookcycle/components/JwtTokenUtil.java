package com.project.bookcycle.components;

import com.project.bookcycle.exceptions.InvalidParamException;
import com.project.bookcycle.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    @Value("${jwt.expiration}")
    private int expiration;

    @Value("${jwt.secretKey}") // khóa mật chỉ có server biết
    private String secretKey; // this is environment variable

    public String generateToken(User user)throws InvalidParamException{
        Map<String, Object> claims = new HashMap<>();
        claims.put("phonenumber", user.getPhoneNumber());
        try{
            String token = Jwts.builder()
                    .setClaims(claims) // "claims" đề cập đến các thông tin cụ thể về thực thể mà token đại diện.(Thông tin cụ thể về người dùng(VD: tên người dùng, vai trò,...))
                    //Khi JWT được tạo và ký, các claims được mã hóa vào trong phần payload của JWT. Khi người dùng gửi token này đến ứng dụng, ứng dụng có thể giải mã token và trích xuất các claims để xác định thông tin về người dùng hoặc các yêu cầu khác.
                    .setSubject(user.getPhoneNumber()) //Đặt chủ đề của token, thường là một đặc điểm định danh của người dùng. Trong trường hợp này, chủ đề được đặt là số điện thoại của người dùng.
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))//Đặt thời gian hết hạn cho token.
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256) //Ký JWT token bằng cách sử dụng một key đã được tạo. Trong trường hợp này, thuật toán ký là HS256.
                    .compact();
            return token;
        }catch (Exception e){
            throw new InvalidParamException("Cannot create jwt token, error: "+e.getMessage());
        }
    }

    private Key getSignInKey() {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }

    // Tạo secretKey cho ứng dụng
    private String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32]; // 256-bit key
        random.nextBytes(keyBytes);
        String secretKey = Encoders.BASE64.encode(keyBytes);
        return secretKey;
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey()) // Lấy key ra để extract sang Tất cả các claims
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public  <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);// appy và lấy ra từng thành phần trong mỗi claim một
    }
    //check expiration
    public boolean isTokenExpired(String token) {
        Date expirationDate = this.extractClaim(token, Claims::getExpiration); // Từ claims lấy ra số ngày hết hạn
        return expirationDate.before(new Date()); // Kiểm tra hết hạn token hay chưa
    }
    public String extractPhoneNumber(String token) {
        return extractClaim(token, Claims::getSubject); // Từ token -> extract claims lấy ra phone_number là subject của token
    }
    public boolean validateToken(String token, UserDetails userDetails) {
        String phoneNumber = extractPhoneNumber(token);
        return (phoneNumber.equals(userDetails.getUsername()))
                && !isTokenExpired(token); // Kiểm tra xem phone_number lấy ra từ token có trùng với phone_number trong userDetials hay không, hay token đã hết hạn hay không
        // Nói chung là kiểm tra tính hợp lệ của token
    }
}
