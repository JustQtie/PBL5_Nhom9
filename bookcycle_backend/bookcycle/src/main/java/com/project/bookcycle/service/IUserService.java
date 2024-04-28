package com.project.bookcycle.service;

import com.project.bookcycle.dto.UserDTO;
import com.project.bookcycle.exceptions.DataNotFoundException;
import com.project.bookcycle.exceptions.PermissionDenyException;
import com.project.bookcycle.model.User;
import com.project.bookcycle.response.LoginResponse;

public interface IUserService {
    User createUser(UserDTO userDTO) throws DataNotFoundException, PermissionDenyException;
    LoginResponse login(String phoneNumber, String password) throws Exception;
}
