package dev.book.challenge.rank.controller;

import dev.book.challenge.rank.service.RankService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class RankController {
    private final RankService rankService;


    @MessageMapping("challenge/{challengeId}/rank")
    public void checkRank(@DestinationVariable Long challengeId) {
        rankService.checkRank(challengeId);

    }

}
