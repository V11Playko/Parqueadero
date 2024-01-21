package com.playko.userservice.service.impl;

import com.playko.userservice.entities.User;
import com.playko.userservice.repository.IUserRepository;
import com.playko.userservice.service.IAuthPasswordEncoderPort;
import com.playko.userservice.service.IUserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class UserService implements IUserService {
    private final IUserRepository userRepository;
    private final IAuthPasswordEncoderPort authPasswordEncoderPort;

    public UserService(IUserRepository userRepository, IAuthPasswordEncoderPort authPasswordEncoderPort) {
        this.userRepository = userRepository;
        this.authPasswordEncoderPort = authPasswordEncoderPort;
    }

    /**
     * Guardar un usuario
     * Guarda un nuevo usuario en el sistema.
     *
     * @param user - Informacion del usuario
     *
     * */
    @Override
    public void saveUser(User user) {
        user.setPassword(authPasswordEncoderPort.encodePassword(user.getPassword()));
        userRepository.save(user);
    }

    /**
     * Buscar usuario
     * Buscar un usuario por su correo electronico
     *
     * @param email - Correo electronico del usuario que se quiere buscar
     *
     * */
    @Override
    public Optional<User> getUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }
}
