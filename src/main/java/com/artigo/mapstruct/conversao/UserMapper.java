package com.artigo.mapstruct.conversao;

import com.artigo.mapstruct.dto.request.UserRequestDto;
import com.artigo.mapstruct.dto.response.UserResponseDto;
import com.artigo.mapstruct.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {


    @Mapping(target = "id", ignore = true)
    User converterParaEntidadeUser(UserRequestDto dto);

    UserResponseDto converterParaUserResposta(UserRequestDto requestDto);

    @Mapping(target = "User.id", ignore = true)
    UserRequestDto converterParaUserRequestDTO(User user);

    List<UserResponseDto> converterParaListaUser(List<User> users);
}
