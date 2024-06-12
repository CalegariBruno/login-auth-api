package com.example.login_auth_api.repositories;

import com.example.login_auth_api.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRespository extends JpaRepository<User, String> {
}
