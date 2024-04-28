package com.project.bookcycle.controller;

import com.project.bookcycle.dto.UserDTO;
import com.project.bookcycle.dto.UserLoginDTO;
import com.project.bookcycle.model.User;
import com.project.bookcycle.response.LoginResponse;
import com.project.bookcycle.response.RegisterResponse;
import com.project.bookcycle.service.IUserService;
import com.project.bookcycle.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor // Tự động tạo Constructor cho các biến final,...
public class UserController {
    private final IUserService userService;



    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody UserDTO userDTO,
            BindingResult result
    ){
        try {
            RegisterResponse registerResponse = new RegisterResponse();
            if(result.hasErrors()){
                List<String> messageError = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                registerResponse.setMessage(messageError.toString());
                return ResponseEntity.badRequest().body(registerResponse);
            }
            if(!userDTO.getPassword().equals(userDTO.getRetypePassword())){
                registerResponse.setMessage("Re-entered password is incorrect!");
                return ResponseEntity.badRequest().body(registerResponse);
            }
            User user = userService.createUser(userDTO);
            registerResponse.setUser(user);
            return ResponseEntity.ok().body(registerResponse);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            @Valid @RequestBody UserLoginDTO userLoginDTO
    ) throws Exception{
        try{
            LoginResponse loginResponse =userService.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword());
            loginResponse.setMessage(MessageKeys.LOGIN_SUCCESSFULLY);
            return ResponseEntity.ok().body(loginResponse);
        }catch (Exception e){
            return ResponseEntity.ok().body(LoginResponse.builder()
                .message(MessageKeys.LOGIN_FAILED)
                .build());
        }

    }

}
