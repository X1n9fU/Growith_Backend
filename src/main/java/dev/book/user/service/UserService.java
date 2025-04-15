package dev.book.user.service;

import dev.book.user.entity.UserEntity;
import dev.book.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean isUserExist(String email){
        return userRepository.existsByEmail(email);
    }

    public void unComplete(UserEntity newUser) {
        userRepository.save(newUser);
    }
}
