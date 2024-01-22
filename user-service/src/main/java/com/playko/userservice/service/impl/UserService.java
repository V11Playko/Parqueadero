package com.playko.userservice.service.impl;

import com.playko.userservice.entities.User;
import com.playko.userservice.repository.IUserRepository;
import com.playko.userservice.service.IAuthPasswordEncoderPort;
import com.playko.userservice.service.IUserService;
import com.playko.userservice.service.exceptions.NoDataFoundException;
import com.playko.userservice.service.exceptions.UnauthorizedException;
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
     * Obtiene un usuario por su dirección de correo electrónico.
     *
     * @param email La dirección de correo electrónico del usuario a buscar.
     * @return Un objeto Optional que contiene el usuario encontrado.
     * @throws NoDataFoundException - Si no se encuentra el usuario asociado al correo electrónico.
     * @throws UnauthorizedException - Si el usuario no tiene el rol de socio o administrador.
     */
    @Override
    public Optional<User> getUserByEmail(String email) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(email));

        if (userOptional.isEmpty()) {
            throw new NoDataFoundException();
        }

        String userRole = userOptional.get().getRole().getName();

        if (!userRole.equals("ROLE_PARTNER") && !userRole.equals("ROLE_ADMIN")) {
            throw new UnauthorizedException();
        }

        return userOptional;
    }
}
