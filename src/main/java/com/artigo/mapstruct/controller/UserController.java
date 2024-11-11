package com.artigo.mapstruct.controller;

import com.artigo.mapstruct.dto.request.UserRequestDto;
import com.artigo.mapstruct.dto.response.UserResponseDto;
import com.artigo.mapstruct.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "users")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> criar(@Valid @RequestBody UserRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.criar(dto));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UserResponseDto> atualizar(@PathVariable long id, @Valid @RequestBody UserResponseDto dto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.atualizarNomeEmail(id, dto));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> listar() {
        return ResponseEntity.ok().body(userService.listar());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserResponseDto> buscar(@PathVariable long id) {
        return ResponseEntity.ok().body(userService.buscar(id));
    }
}
