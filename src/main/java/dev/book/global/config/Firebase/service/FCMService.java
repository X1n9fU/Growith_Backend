package dev.book.global.config.Firebase.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import dev.book.global.config.Firebase.entity.FcmToken;
import dev.book.global.config.Firebase.repository.FcmTokenRepository;
import dev.book.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FCMService {
    private final FcmTokenRepository fcmTokenRepository;

    public void sendSpendNotification(String fcmToken, String userName, int budget, long amount, long percent) {
        String title = "지출 알림";
        String body = userName + "님, 현재까지 지출은 " + amount + "원입니다." +
                "정하신 예산" + budget + "원 에서" + percent + "% 만큼 사용하셨습니다.";

        Message message = messageBuild(fcmToken, title, body);

        try {
            String response = FirebaseMessaging.getInstance().send(message);

        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }

    public void saveToken(UserEntity user, String token) {
        fcmTokenRepository.save(new FcmToken(user, token));
    }

    private Message messageBuild(String fcmToken, String title, String body) {
        return Message.builder()
                .setToken(fcmToken)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .build())
                .build();
    }
}
