package com.project.bookcycle.service;

import com.project.bookcycle.dto.UserChangePassDTO;
import com.project.bookcycle.dto.UserDTO;
import com.project.bookcycle.exceptions.DataNotFoundException;
import com.project.bookcycle.exceptions.PermissionDenyException;
import com.project.bookcycle.model.User;
import com.project.bookcycle.response.LoginResponse;
import com.project.bookcycle.response.UserResponse;

import java.util.List;

public interface IUserService {
    User createUser(UserDTO userDTO) throws DataNotFoundException, PermissionDenyException;
    LoginResponse login(String phoneNumber, String password) throws Exception;
    User updateUser(Long id, UserDTO userDTO) throws DataNotFoundException;
    User getUser(Long id) throws DataNotFoundException;
    void deleteUser(Long id) throws DataNotFoundException;
    String changePassword(Long id, UserChangePassDTO userChangePassDTO) throws DataNotFoundException;
    List<UserResponse> getAll() throws DataNotFoundException;
    User updateImage(Long id, String urlFile) throws DataNotFoundException;
    List<UserResponse> banUser(Long id, boolean isActive) throws DataNotFoundException;
    List<UserResponse> getAllNotUser(Long id);
}
