package com.azeroth.api.service;

import com.azeroth.api.dto.AuthResponse;
import com.azeroth.api.dto.LoginRequest;
import com.azeroth.api.dto.RegisterRequest;
import com.azeroth.api.entity.Usuario;
import com.azeroth.api.exception.BussinesException;
import com.azeroth.api.enums.ErrorCode;
import com.azeroth.api.repository.IUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final IUsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Verificar si el username ya existe
        if (usuarioRepository.existsByUsername(request.username())) {
            throw new BussinesException(
                    "El nombre de usuario ya está en uso",
                    ErrorCode.NOMBRE_JUGADOR_YA_EN_USO
            );
        }

        // Verificar si el email ya existe
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new BussinesException(
                    "El email ya está registrado",
                    ErrorCode.NOMBRE_JUGADOR_YA_EN_USO
            );
        }

        // Crear usuario
        Usuario usuario = Usuario.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .email(request.email())
                .role(request.role())
                .enabled(true)
                .build();

        usuarioRepository.save(usuario);

        // Generar token
        String jwtToken = jwtService.generateToken(usuario);

        return AuthResponse.builder()
                .token(jwtToken)
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .role(usuario.getRole().name())
                .build();
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        // Autenticar usuario
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        // Buscar usuario
        Usuario usuario = usuarioRepository.findByUsername(request.username())
                .orElseThrow(() -> new BussinesException(
                        "Usuario no encontrado",
                        ErrorCode.NOMBRE_JUGADOR_YA_EN_USO
                ));

        // Generar token
        String jwtToken = jwtService.generateToken(usuario);

        return AuthResponse.builder()
                .token(jwtToken)
                .username(usuario.getUsername())
                .email(usuario.getEmail())
                .role(usuario.getRole().name())
                .build();
    }
}

