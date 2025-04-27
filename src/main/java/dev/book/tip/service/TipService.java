package dev.book.tip.service;

import dev.book.challenge.exception.ChallengeException;
import dev.book.challenge.exception.ErrorCode;
import dev.book.challenge.user_challenge.entity.UserChallenge;
import dev.book.challenge.user_challenge.repository.UserChallengeRepository;
import dev.book.global.config.security.dto.CustomUserDetails;
import dev.book.global.sse.service.SseService;
import dev.book.tip.dto.request.TipRequest;
import dev.book.tip.dto.response.TipResponse;
import dev.book.tip.entity.Tip;
import dev.book.tip.exception.TipErrorCode;
import dev.book.tip.exception.TipErrorException;
import dev.book.tip.repository.TipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipService {

    private final TipRepository tipRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final SseService sseService;

    @Transactional
    public void createTip(CustomUserDetails userDetails, TipRequest tipRequest) {
        UserChallenge userChallenge = userChallengeRepository.findByUserIdAndChallengeId(userDetails.user().getId(), tipRequest.challengeId())
                .orElseThrow(() -> new ChallengeException(ErrorCode.CHALLENGE_NOT_FOUND_USER));
        checkUserWriteTip(userChallenge);

        Tip tip = new Tip(tipRequest.content(), userDetails.user());
        tipRepository.save(tip);

        sseService.sendTipToAllUsers(new TipResponse(tip.getUser().getNickname(), tip.getContent()));
    }

    private static void checkUserWriteTip(UserChallenge userChallenge) {
        if (userChallenge.isWriteTip())
            throw new TipErrorException(TipErrorCode.ALREADY_EXISTED);
        else userChallenge.writeTip();
    }

    public List<TipResponse> getTips() {
        List<Tip> tips = tipRepository.find20RandomTips();
        return tips.stream()
                .map(tip -> new TipResponse(tip.getUser().getNickname(), tip.getContent())).toList();
    }
}
