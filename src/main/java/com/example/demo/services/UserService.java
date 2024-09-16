package com.example.demo.services;

import com.example.demo.services.dtos.UserDTO;
import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO updateUser(UUID id, UserDTO userDTO);
    void deleteUser(UUID id);
    UserDTO getUserById(UUID id);
    List<UserDTO> getAllUsers();
}
