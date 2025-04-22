package dev.book.challenge.rank.controller;

import dev.book.challenge.rank.dto.response.RankResponse;
import dev.book.challenge.rank.service.RankService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RankController {
    private final RankService rankService;


    @MessageMapping("challenge/{challengeId}/rank")
    public void checkRank(@DestinationVariable Long challengeId) {
        rankService.checkRank(challengeId);

    }

}
