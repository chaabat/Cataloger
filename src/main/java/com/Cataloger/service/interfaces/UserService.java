package com.Cataloger.service.interfaces;

<<<<<<< HEAD
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.Cataloger.dto.request.UserRequest;
import com.Cataloger.dto.response.UserResponse;
import com.Cataloger.entity.Role;

public interface UserService {
    Page<UserResponse> getAllUsers(Pageable pageable);
    UserResponse getUser(Long id);
    UserResponse createUser(UserRequest request);
    UserResponse updateUser(Long id, UserRequest request);
    void deleteUser(Long id);
    UserResponse login(String login, String password);
    UserResponse updateRole(Long id, Role role);
    UserResponse findByLogin(String login);
=======
public class UserService {

>>>>>>> parent of 9d2fc01 (category , product and user done)
}
