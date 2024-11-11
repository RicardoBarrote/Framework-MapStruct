package com.artigo.mapstruct.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDto(@NotBlank String nome,
                             @Email String email,
                             @NotBlank String senha) {
}
