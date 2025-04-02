package com.example.demo.graphql;

import com.example.demo.services.UserService;
import com.example.demo.services.dtos.ListingDTO;
import com.example.demo.services.dtos.UserDTO;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.UUID;

@DgsComponent
public class UserDataFetcher {

    private final UserService userService;

    @Autowired
    public UserDataFetcher(UserService userService) {
        this.userService = userService;
    }

    @DgsQuery
    public UserDTO getUserById(String id) {
        return userService.getUserById(UUID.fromString(id));
    }

    @DgsQuery
    public Page<UserDTO> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.getAllUsers(pageable);
    }

    @DgsMutation
    public UserDTO createUser(String username, String email) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setEmail(email);
        return userService.createUser(userDTO);
    }

    @DgsMutation
    public UserDTO patchUser(String id, String username, String email) {
        UserDTO userDTO = new UserDTO();
        if (username != null) {
            userDTO.setUsername(username);
        }
        if (email != null) {
            userDTO.setEmail(email);
        }
        return userService.patchUser(UUID.fromString(id), userDTO);
    }

    @DgsMutation
    public Boolean deleteUser(String id) {
        userService.deleteUser(UUID.fromString(id));
        return true;
    }
}
