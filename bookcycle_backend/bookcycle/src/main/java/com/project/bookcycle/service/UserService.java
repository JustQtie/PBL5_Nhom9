package com.project.bookcycle.service;

import com.project.bookcycle.components.JwtTokenUtil;
import com.project.bookcycle.dto.UserChangePassDTO;
import com.project.bookcycle.dto.UserDTO;
import com.project.bookcycle.exceptions.DataNotFoundException;
import com.project.bookcycle.exceptions.PermissionDenyException;
import com.project.bookcycle.model.Role;
import com.project.bookcycle.model.User;
import com.project.bookcycle.repository.RoleRepository;
import com.project.bookcycle.repository.UserRepository;
import com.project.bookcycle.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public User createUser(UserDTO userDTO) throws DataNotFoundException, PermissionDenyException {
        String phoneNumber = userDTO.getPhoneNumber();
        if(userRepository.existsByPhoneNumber(phoneNumber)){
            throw new DataIntegrityViolationException("Phone number already exists");
        }
        Role role = roleRepository.findById(1L)
                .orElseThrow(()-> new DataNotFoundException("Role not found!"));
        if(role.getName().toUpperCase().equals("ADMIN")){
            throw new PermissionDenyException("Admin cannot create account");
        }
        User newUser = User.builder()
                .username(userDTO.getUsername())
                .fullname(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .active(true)
                .dateOfBirth(userDTO.getDateOfBirth())
                .gender(userDTO.isGender())
                .role(role)
                .build();
        String password = userDTO.getPassword();
        String encodePassword = passwordEncoder.encode(password);
        newUser.setPassword(encodePassword);
        return userRepository.save(newUser);
    }

    @Override
    public LoginResponse login(String phoneNumber, String password) throws Exception {
        Role role = userRepository.findRoleByPhoneNumber(phoneNumber);
        // UsernamePasswordAuthenticationToken là một loại đối tượng Authentication được sử dụng để biểu diễn việc xác
        // thực bằng tên người dùng và mật khẩu. Khi một người dùng cố gắng đăng nhập vào hệ thống, thông tin tên người
        // dùng và mật khẩu của họ được chứa trong đối tượng này và sau đó được chuyển đến AuthenticationManager để kiểm tra tính hợp lệ.
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                phoneNumber, password
        );
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        User principal = (User) authenticate.getPrincipal(); //Sau khi quá trình xác thực thành công, đối tượng
        // Authentication sẽ chứa thông tin người dùng được xác thực. Phương thức getPrincipal() trả về đối tượng chính của người dùng đã được xác thực.
        return LoginResponse.builder().token(jwtTokenUtil.generateToken(principal))
                .role(role.getName())
                .build();
    }

    @Override
    public User getUser(Long id) throws DataNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("Cannot find user with id " + id));
    }

    @Override
    public void deleteUser(Long id) throws DataNotFoundException{
        User existsUser = getUser(id);
        if(existsUser != null){
            existsUser.setActive(false);
            userRepository.save(existsUser);
        }
    }

    @Override
    public String changePassword(Long id, UserChangePassDTO userChangePassDTO) throws DataNotFoundException {
        User existUser = getUser(id);
        if(existUser != null){
            if(existUser.getPassword().equals(userChangePassDTO.getOldPass())){
                existUser.setPassword(userChangePassDTO.getNewPass());
                userRepository.save(existUser);
                return "Change password complete";
            }else{
                return "The old password entered is invalid";
            }
        }
        return null;
    }

    @Override
    public User updateUser(Long id, UserDTO userDTO) throws DataNotFoundException {
        User existsUser = getUser(id);
        if(existsUser != null){
            userDTO.setPassword(existsUser.getPassword());
            existsUser.setFullname(userDTO.getFullName());
            existsUser.setPhoneNumber(userDTO.getPhoneNumber());
            existsUser.setDateOfBirth(userDTO.getDateOfBirth());
            existsUser.setGender(userDTO.isGender());
            existsUser.setAddress(userDTO.getAddress());
            existsUser.setPassword(existsUser.getPassword());
            return userRepository.save(existsUser);
        }
        return null;
    }


}
