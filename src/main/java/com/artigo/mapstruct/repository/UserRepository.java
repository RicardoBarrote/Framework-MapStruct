package com.artigo.mapstruct.repository;

import com.artigo.mapstruct.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
