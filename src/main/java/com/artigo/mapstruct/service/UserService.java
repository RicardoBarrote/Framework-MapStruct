package com.artigo.mapstruct.service;

import com.artigo.mapstruct.conversao.UserMapper;
import com.artigo.mapstruct.dto.request.UserRequestDto;
import com.artigo.mapstruct.dto.response.UserResponseDto;
import com.artigo.mapstruct.entity.User;
import com.artigo.mapstruct.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserResponseDto criar(UserRequestDto requestDto) {
        return userMapper.converterParaUserResposta(userMapper
                .converterParaUserRequestDTO(userRepository
                        .save(userMapper.converterParaEntidadeUser(requestDto))));
    }

    public UserResponseDto atualizarNomeEmail(Long id, UserResponseDto dto) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setNome(dto.nome());
                    user.setEmail(dto.email());
                    return userMapper.converterParaUserResposta(userMapper.converterParaUserRequestDTO(userRepository.save(user)
                    ));
                }).orElseThrow(() -> new NoSuchElementException("Usuário não encontrado, " + id));
    }

    public void deletar(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Usuário não encontrado, " + id));
        userRepository.delete(user);
    }

    public List<UserResponseDto> listar() {
        return userMapper.converterParaListaUser(userRepository.findAll());
    }

    public UserResponseDto buscar(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Usuário não existe."));
        return userMapper.converterParaUserResposta(userMapper.converterParaUserRequestDTO(user));
    }
}
