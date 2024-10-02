package com.example.demo.services.Impl;


import com.example.demo.controllers.exceptions.entityNotFoundExceptions.UserNotFoundException;

import com.example.demo.models.Listing;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserService;
import com.example.demo.services.dtos.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        User savedUser = userRepository.saveAndFlush(user);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public UserDTO patchUser(UUID id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        if (userDTO.getUsername() != null) {
            user.setUsername(userDTO.getUsername());
        }
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        User patchedUser = userRepository.saveAndFlush(user);
        return modelMapper.map(patchedUser, UserDTO.class);
    }

    @Override
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(user);
    }

    @Override
    public UserDTO getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);

        userDTO.setListingIds(getListingIds(user));
        return userDTO;
    }

    @Override
    public Page<UserDTO> getAllUsers(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);

        return userPage.map(user -> {
            UserDTO userDTO = modelMapper.map(user, UserDTO.class);
            userDTO.setListingIds(getListingIds(user));
            return userDTO;
        });
    }

    private List<UUID> getListingIds(User user) {
        return user.getListings().stream()
                .map(Listing::getId)
                .collect(Collectors.toList());
    }



}
