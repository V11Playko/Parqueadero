package com.playko.userservice.configuration.security.userDetails;

import com.playko.userservice.entities.Role;
import com.playko.userservice.entities.User;
import com.playko.userservice.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = this.userRepository.findByEmail(email);

        List<User> userEntity = userRepository.findAllById(Collections.singleton(user.getId()));

        if (userEntity.isEmpty()) {
            throw new UsernameNotFoundException("Invalid email or password");
        }

        List<Role> roles = new ArrayList<>();

        for (User usuario : userEntity) {
            roles.add(user.getRole());
        }
        return CustomUserDetails.build(user, roles);
    }
}

