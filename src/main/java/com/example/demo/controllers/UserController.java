package com.example.demo.controllers;

import com.example.contract.controllers.UserApi;
import com.example.contract.dtos.UserRequest;
import com.example.contract.dtos.UserResponse;
import com.example.demo.services.UserService;
import com.example.demo.services.dtos.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserController implements UserApi {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @Override
    public ResponseEntity<EntityModel<UserResponse>> patchUser(UUID id, UserRequest userRequest) {
        UserDTO userDTO = toDTO(userRequest);
        UserDTO patchedUser = userService.patchUser(id, userDTO);
        UserResponse response = toResponse(patchedUser);
        return ResponseEntity.ok(toEntityModel(response));
    }

    @Override
    public ResponseEntity<Void> deleteUser(UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


    @Override
    public ResponseEntity<PagedModel<EntityModel<UserResponse>>> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDTO> users = userService.getAllUsers(pageable);

        Page<UserResponse> responsePage = users.map(this::toResponse);

        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(
                responsePage.getSize(),
                responsePage.getNumber(),
                responsePage.getTotalElements()
        );

        List<EntityModel<UserResponse>> content = responsePage.getContent().stream()
                .map(this::toEntityModel)
                .toList();

        PagedModel<EntityModel<UserResponse>> pagedModel = PagedModel.of(content, metadata);

        if (responsePage.hasNext()) {
            pagedModel.add(linkTo(methodOn(UserController.class)
                    .getAllUsers(pageable.next().getPageNumber(), pageable.getPageSize()))
                    .withRel("next"));
        }
        if (responsePage.hasPrevious()) {
            pagedModel.add(linkTo(methodOn(UserController.class)
                    .getAllUsers(pageable.previousOrFirst().getPageNumber(), pageable.getPageSize()))
                    .withRel("previous"));
        }
        pagedModel.add(linkTo(methodOn(UserController.class)
                .getAllUsers(pageable.getPageNumber(), pageable.getPageSize()))
                .withSelfRel());

        return ResponseEntity.ok(pagedModel);
    }


    @Override
    public ResponseEntity<EntityModel<UserResponse>> createUser(UserRequest userRequest) {
        UserDTO createdUser = userService.createUser(toDTO(userRequest));
        UserResponse response = toResponse(createdUser);

        return ResponseEntity.created(
                linkTo(methodOn(UserController.class).getUserById(response.id())).toUri()
        ).body(toEntityModel(response));

    }


    @Override
    public ResponseEntity<EntityModel<UserResponse>> getUserById(UUID id) {
        UserDTO userDTO = userService.getUserById(id);
        UserResponse userResponse = toResponse(userDTO);
        return ResponseEntity.ok(toEntityModel(userResponse));
    }

    private UserDTO toDTO(UserRequest request) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(request.username());
        userDTO.setEmail(request.email());
        return userDTO;
    }

    private UserResponse toResponse(UserDTO dto) {
        return new UserResponse(dto.getId(), dto.getUsername(), dto.getEmail(), dto.getListingIds());
    }

    private EntityModel<UserResponse> toEntityModel(UserResponse response) {
        return EntityModel.of(response,
                linkTo(methodOn(UserController.class).getUserById(response.id())).withSelfRel(),
                linkTo(methodOn(UserController.class).getAllUsers(0, 3)).withRel("all"),
                linkTo(methodOn(UserController.class).patchUser(response.id(), null)).withRel("update"),
                linkTo(methodOn(UserController.class).deleteUser(response.id())).withRel("delete"));
    }
}