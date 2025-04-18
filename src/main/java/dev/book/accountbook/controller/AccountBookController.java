package dev.book.accountbook.controller;

import dev.book.accountbook.controller.swagger.AccountBookApi;
import dev.book.accountbook.dto.request.AccountBookIncomeRequest;
import dev.book.accountbook.dto.request.AccountBookSpendRequest;
import dev.book.accountbook.dto.response.AccountBookIncomeResponse;
import dev.book.accountbook.dto.response.AccountBookSpendResponse;
import dev.book.accountbook.service.AccountBookService;
import dev.book.accountbook.type.Category;
import dev.book.global.config.security.dto.CustomUserDetails;
import dev.book.user.entity.UserEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountBookController implements AccountBookApi {
    private final AccountBookService accountBookService;

    @Override
    public ResponseEntity<List<AccountBookSpendResponse>> getSpendList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.user().getId();
        List<AccountBookSpendResponse> responses = accountBookService.getSpendList(userId);

        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<AccountBookSpendResponse> getSpendOne(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long id) {
        Long userId = userDetails.user().getId();
        AccountBookSpendResponse response = accountBookService.getSpendOne(id,userId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/spend")
    public ResponseEntity<AccountBookSpendResponse> createSpend(@AuthenticationPrincipal CustomUserDetails userDetails, @Valid @RequestBody AccountBookSpendRequest request) {
        UserEntity userId = userDetails.user();
        AccountBookSpendResponse response = accountBookService.createSpend(request, userId);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<AccountBookSpendResponse> modifySpend(@AuthenticationPrincipal CustomUserDetails userDetails, @Valid @RequestBody AccountBookSpendRequest request, @PathVariable Long id) {
        Long userId = userDetails.user().getId();
        AccountBookSpendResponse response = accountBookService.modifySpend(request, id, userId);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Boolean> deleteSpend(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long id) {
        Long userId = userDetails.user().getId();
        boolean response = accountBookService.deleteSpend(id, userId);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<AccountBookIncomeResponse>> getIncomeList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.user().getId();
        List<AccountBookIncomeResponse> responses = accountBookService.getIncomeList(userId);

        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<AccountBookIncomeResponse> getIncomeOne(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long id) {
        Long userId = userDetails.user().getId();
        AccountBookIncomeResponse response = accountBookService.getIncomeOne(id, userId);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<AccountBookIncomeResponse> createIncome(@AuthenticationPrincipal CustomUserDetails userDetails, @Valid @RequestBody AccountBookIncomeRequest request) {
        UserEntity user = userDetails.user();
        AccountBookIncomeResponse response = accountBookService.createIncome(request, user);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<AccountBookIncomeResponse> modifyIncome(@AuthenticationPrincipal CustomUserDetails userDetails, @Valid @RequestBody AccountBookIncomeRequest request, @PathVariable Long id) {
        Long userId = userDetails.user().getId();
        AccountBookIncomeResponse response = accountBookService.modifyIncome(id, request, userId);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Boolean> deleteIncome(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long id) {
        Long userId = userDetails.user().getId();
        boolean response = accountBookService.deleteIncome(id, userId);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<List<AccountBookSpendResponse>> getCategorySpendList(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Category category) {
        Long userId = userDetails.user().getId();
        List<AccountBookSpendResponse> responses = accountBookService.getCategorySpendList(category, userId);

        return ResponseEntity.ok(responses);
    }
}
