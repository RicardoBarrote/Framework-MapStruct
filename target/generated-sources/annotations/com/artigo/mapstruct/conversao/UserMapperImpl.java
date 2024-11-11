package com.artigo.mapstruct.conversao;

import com.artigo.mapstruct.dto.request.UserRequestDto;
import com.artigo.mapstruct.dto.response.UserResponseDto;
import com.artigo.mapstruct.entity.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-08T15:45:50-0300",
    comments = "version: 1.6.2, compiler: javac, environment: Java 21.0.4 (Azul Systems, Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User converterParaEntidadeUser(UserRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setNome( dto.nome() );
        user.setEmail( dto.email() );
        user.setSenha( dto.senha() );

        return user;
    }

    @Override
    public UserResponseDto converterParaUserResposta(UserRequestDto requestDto) {
        if ( requestDto == null ) {
            return null;
        }

        String nome = null;
        String email = null;

        nome = requestDto.nome();
        email = requestDto.email();

        UserResponseDto userResponseDto = new UserResponseDto( nome, email );

        return userResponseDto;
    }

    @Override
    public UserRequestDto converterParaUserRequestDTO(User user) {
        if ( user == null ) {
            return null;
        }

        String nome = null;
        String email = null;
        String senha = null;

        nome = user.getNome();
        email = user.getEmail();
        senha = user.getSenha();

        UserRequestDto userRequestDto = new UserRequestDto( nome, email, senha );

        return userRequestDto;
    }

    @Override
    public List<UserResponseDto> converterParaListaUser(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserResponseDto> list = new ArrayList<UserResponseDto>( users.size() );
        for ( User user : users ) {
            list.add( converterParaUserResposta( converterParaUserRequestDTO( user ) ) );
        }

        return list;
    }
}
