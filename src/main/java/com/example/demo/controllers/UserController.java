package com.example.demo.controllers;

import com.example.demo.controllers.assemblers.UserModelAssembler;
import com.example.demo.services.UserService;
import com.example.demo.services.dtos.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserModelAssembler assembler;

    @Autowired
    public UserController(UserService userService, UserModelAssembler assembler) {
        this.userService = userService;
        this.assembler = assembler;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserDTO>> getUserById(@PathVariable UUID id) {
        UserDTO userDTO = userService.getUserById(id);
        return ResponseEntity.ok(assembler.toModel(userDTO));
    }

    @PostMapping
    public ResponseEntity<EntityModel<UserDTO>> createUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.created(assembler.toModel(createdUser).getRequiredLink("self").toUri())
                .body(assembler.toModel(createdUser));
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<UserDTO>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<UserDTO> users = userService.getAllUsers(pageable);
        PagedModel<EntityModel<UserDTO>> pagedModel = assembler.toPagedModel(users, pageable);

        return ResponseEntity.ok(pagedModel);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<UserDTO>> patchUser(@PathVariable UUID id, @Valid @RequestBody UserDTO userDTO) {
        UserDTO patchedUser = userService.patchUser(id, userDTO);
        return ResponseEntity.ok(assembler.toModel(patchedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
