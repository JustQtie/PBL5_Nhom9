package com.project.bookcycle.controller;

import com.project.bookcycle.dto.UserChangePassDTO;
import com.project.bookcycle.dto.UserDTO;
import com.project.bookcycle.dto.UserLoginDTO;
import com.project.bookcycle.model.User;
import com.project.bookcycle.response.LoginResponse;
import com.project.bookcycle.response.RegisterResponse;
import com.project.bookcycle.service.IUserService;
import com.project.bookcycle.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody UserDTO userDTO
    ) throws Exception{
        try {
            User updateUser = userService.updateUser(id, userDTO);
            return ResponseEntity.ok(updateUser);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable long id){
        try{
            userService.deleteUser(id);
            return ResponseEntity.ok(String.format("User has been successfully banned from using"));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/changepass/{id}")
    public ResponseEntity<String> changePass(
            @PathVariable long id,
            @Valid @RequestBody UserChangePassDTO userChangePassDTO,
            BindingResult result
            ){
        try {
            if(result.hasErrors()){
                List<String> messageError = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(messageError.toString());
            }
            String response = userService.changePassword(id, userChangePassDTO);
            ResponseEntity.ok().body(response);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return null;
    }
}
