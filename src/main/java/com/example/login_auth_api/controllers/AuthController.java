package com.example.login_auth_api.controllers;

import com.example.login_auth_api.domain.user.User;
import com.example.login_auth_api.dtos.LoginRequestDTO;
import com.example.login_auth_api.dtos.RegisterRequestDTO;
import com.example.login_auth_api.dtos.ResponseDTO;
import com.example.login_auth_api.infra.security.TokenService;
import com.example.login_auth_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @PostMapping("login")
    public ResponseEntity login(@RequestBody LoginRequestDTO dto){
        User user = this.userRepository.findByEmail(dto.email()).orElseThrow(() -> new RuntimeException("User Not Found"));
        if(passwordEncoder.matches(user.getPassword(), dto.password())){
            String token = this.tokenService.generateToken(user);
            return ResponseEntity.ok( new ResponseDTO(user.getName(), token));
        }else{
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO dto){
        Optional<User> user = this.userRepository.findByEmail(dto.email());

        if(user.isEmpty()){
            User newUser = new User();

            newUser.setEmail(dto.email());
            newUser.setName(dto.name());
            newUser.setPassword(passwordEncoder.encode(dto.password()));

            this.userRepository.save(newUser);

            String token = this.tokenService.generateToken(newUser);
            return ResponseEntity.ok( new ResponseDTO(newUser.getName(), token));

        }
        return ResponseEntity.badRequest().build();
    }
}
