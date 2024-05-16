package com.project.bookcycle.controller;

import com.project.bookcycle.dto.ProductImageDTO;
import com.project.bookcycle.dto.UserChangePassDTO;
import com.project.bookcycle.dto.UserDTO;
import com.project.bookcycle.dto.UserLoginDTO;
import com.project.bookcycle.exceptions.DataNotFoundException;

import com.project.bookcycle.model.User;
import com.project.bookcycle.response.*;
import com.project.bookcycle.service.IUserService;
import com.project.bookcycle.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
            if(loginResponse.getUser().isActive()){
                loginResponse.setMessage(MessageKeys.LOGIN_SUCCESSFULLY);
                loginResponse.setEc(0);
                return ResponseEntity.ok().body(loginResponse);
            }else{
                return ResponseEntity.ok().body(LoginResponse.builder()
                        .message(MessageKeys.LOGIN_FAILED).ec(-1)
                        .build());
            }
        }catch (Exception e){
            return ResponseEntity.ok().body(LoginResponse.builder()
                .message(MessageKeys.LOGIN_FAILED).ec(-1)
                .build());
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> getUser(
            @PathVariable Long id
    ){
        try{

            User user = userService.getUser(id);

            JSONObject responseJson = new JSONObject();
            responseJson.put("user", user);
            responseJson.put("EC", 0);
            return ResponseEntity.ok().body(responseJson.toString());
        } catch (DataNotFoundException | JSONException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(UserResponse.builder().ec(-1));
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserDTO userDTO
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

    @PostMapping ("")
    public ResponseEntity<UserListResponse> getUser(
    ) {
        try {
            List<UserResponse> listUser = userService.getAll();
            return ResponseEntity.ok(UserListResponse
                    .builder()
                    .userResponseList(listUser)
                    .EC(0)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(UserListResponse.builder().EC(-1).build());
        }
    }
    @PostMapping(value = "uploads/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(
            @PathVariable("id") Long userId,
            @ModelAttribute("file") MultipartFile file
    ){
        try {
                if(file.getSize() == 0) {
                    return ResponseEntity.badRequest().body("File invalid");
                }
                // Kiểm tra kích thước file và định dạng
                if(file.getSize() > 10 * 1024 * 1024) { // Kích thước > 10MB
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                            .body(MessageKeys.UPLOAD_IMAGES_FILE_LARGE);
                }
                String contentType = file.getContentType();
                if(contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body(MessageKeys.UPLOAD_IMAGES_FILE_MUST_BE_IMAGE);
                }
                String filename = storeFile(file);
                User user = userService.updateImage(userId, filename);
            return ResponseEntity.ok().body(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping(value = "ban/{id}")
    public ResponseEntity<?> toFalseActive(
            @PathVariable("id") Long userId
    ) throws DataNotFoundException {
        try {
        List<UserResponse> listUser = userService.banUser(userId, false);
        return ResponseEntity.ok(UserListResponse
                .builder()
                .userResponseList(listUser)
                .EC(0)
                .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(UserListResponse.builder().EC(-1).build());
        }
    }
    @PutMapping(value = "unban/{id}")
    public ResponseEntity<?> toTrueActive(
            @PathVariable("id") Long userId
    ) throws DataNotFoundException {
        try {
            List<UserResponse> listUser = userService.banUser(userId, true);
            return ResponseEntity.ok(UserListResponse
                    .builder()
                    .userResponseList(listUser)
                    .EC(0)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(UserListResponse.builder().EC(-1).build());
        }
    }
    @GetMapping("images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName) {
        try {
            java.nio.file.Path imagePath = Paths.get("uploads/user_image/"+imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());

            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    private String storeFile(MultipartFile file) throws IOException {
        if (!isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("Invalid image format");
        }
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        // Thêm UUID vào trước tên file để đảm bảo tên file là duy nhất
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
        // Đường dẫn đến thư mục mà bạn muốn lưu file
        java.nio.file.Path uploadDir = Paths.get("uploads/user_image");
        // Kiểm tra và tạo thư mục nếu nó không tồn tại
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        // Đường dẫn đầy đủ đến file
        java.nio.file.Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        // Sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }
    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }
}