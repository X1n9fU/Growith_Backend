package dev.book.global.config.security.service.refresh;

import dev.book.global.config.security.entity.RefreshToken;
import dev.book.global.config.security.repository.RefreshTokenRepository;
import dev.book.user.entity.UserEntity;
import dev.book.user.exception.UserNotFoundException;
import dev.book.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Transactional
    public void saveAndUpdateRefreshToken(String email, String refreshToken){
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("이메일에 해당하는 유저가 존재하지 않습니다. : " + email));

        saveAndUpdateRefreshToken(user, refreshToken);
    }

    public void saveAndUpdateRefreshToken(UserEntity user, String refreshToken){
        RefreshToken refreshTokenEntity = refreshTokenRepository.findByUser(user)
                .orElseGet(() -> new RefreshToken(user ,refreshToken));
        refreshTokenEntity.updateToken(refreshToken);
        refreshTokenRepository.save(refreshTokenEntity);

        user.updateRefreshToken(refreshTokenEntity);
    }
}
