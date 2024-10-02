package com.example.demo.controllers.assemblers;

import com.example.demo.controllers.UserController;
import com.example.demo.services.dtos.UserDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserModelAssembler extends RepresentationModelAssemblerSupport<UserDTO, EntityModel<UserDTO>> {

    public UserModelAssembler() {
        super(UserController.class, (Class<EntityModel<UserDTO>>) (Class<?>) EntityModel.class);
    }

    @Override
    public EntityModel<UserDTO> toModel(UserDTO userDTO) {
        EntityModel<UserDTO> model = EntityModel.of(userDTO);
        model.add(linkTo(methodOn(UserController.class).getUserById(userDTO.getId())).withSelfRel());
        model.add(linkTo(methodOn(UserController.class).patchUser(userDTO.getId(), userDTO)).withRel("update"));
        model.add(linkTo(methodOn(UserController.class).deleteUser(userDTO.getId())).withRel("delete"));
        return model;
    }

    public PagedModel<EntityModel<UserDTO>> toPagedModel(Page<UserDTO> users, Pageable pageable) {
        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(
                users.getSize(), users.getNumber(), users.getTotalElements());

        PagedModel<EntityModel<UserDTO>> pagedModel = PagedModel.of(users.map(this::toModel).getContent(), metadata);

        if (users.hasNext()) {
            pagedModel.add(linkTo(methodOn(UserController.class)
                    .getAllUsers(pageable.next().getPageNumber(), pageable.getPageSize()))
                    .withRel("next"));
        }
        if (users.hasPrevious()) {
            pagedModel.add(linkTo(methodOn(UserController.class)
                    .getAllUsers(pageable.previousOrFirst().getPageNumber(), pageable.getPageSize()))
                    .withRel("previous"));
        }

        pagedModel.add(linkTo(methodOn(UserController.class)
                .getAllUsers(pageable.getPageNumber(), pageable.getPageSize()))
                .withSelfRel());

        return pagedModel;
    }
}
