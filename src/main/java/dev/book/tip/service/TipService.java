package dev.book.tip.service;

import dev.book.global.config.security.dto.CustomUserDetails;
import dev.book.global.sse.service.SseService;
import dev.book.tip.dto.request.TipRequest;
import dev.book.tip.dto.response.TipResponse;
import dev.book.tip.entity.Tip;
import dev.book.tip.repository.TipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipService {

    private final TipRepository tipRepository;
    private final SseService sseService;

    public void createTip(CustomUserDetails userDetails, TipRequest tipRequest) {
        Tip tip = new Tip(tipRequest.content(), userDetails.user());
        tipRepository.save(tip);
        sseService.sendTipToAllUsers(new TipResponse(tip.getUser().getNickname(), tip.getContent()));
    }

    public List<TipResponse> getTips() {
        List<Tip> tips = tipRepository.find20RandomTips();
        return tips.stream()
                .map(tip -> new TipResponse(tip.getUser().getNickname(), tip.getContent())).toList();
    }
}
