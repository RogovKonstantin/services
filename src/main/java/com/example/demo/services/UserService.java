package com.example.demo.services;

import com.example.demo.services.dtos.ListingDTO;
import com.example.demo.services.dtos.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO patchUser(UUID id, UserDTO userDTO);
    void deleteUser(UUID id);
    UserDTO getUserById(UUID id);
    Page<UserDTO> getAllUsers(Pageable pageable);
}
