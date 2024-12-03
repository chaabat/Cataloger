package com.Cataloger.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.Cataloger.dto.request.UserRequest;
import com.Cataloger.dto.response.UserResponse;
 
public interface UserService {
    Page<UserResponse> getAllUsers(Pageable pageable);
    UserResponse getUser(Long id);
    UserResponse createUser(UserRequest request);
    UserResponse updateUser(Long id, UserRequest request);
    void deleteUser(Long id);
 }
