package com.dgramada.registrationexample.user;

import com.dgramada.registrationexample.exceptionhandling.exception.ResourceAlreadyExistsException;
import com.dgramada.registrationexample.user.dto.UserCreateDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void save(UserCreateDTO dto) {
        userRepository.findByEmail(dto.email()).orElseThrow(
                () -> new ResourceAlreadyExistsException("User with email " + dto.email() + " already exists")
        );

        final var user = new User(
                dto.firstName(),
                dto.lastName(),
                dto.email(),
                passwordEncoder.encode(dto.password())
        );

        userRepository.save(user);
    }

}
