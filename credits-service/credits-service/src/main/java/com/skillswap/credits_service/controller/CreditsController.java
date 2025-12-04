package com.skillswap.credits_service.controller;

import com.skillswap.credits_service.dto.*;
import com.skillswap.credits_service.service.CreditsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/credits")
@RequiredArgsConstructor
public class CreditsController {

    private final CreditsService creditsService;

    @GetMapping("/balance")
    public ResponseEntity<BalanceDto> getBalance(@RequestHeader("X-Authenticated-User") String username) {
        return ResponseEntity.ok(creditsService.getBalance(username));
    }

    @PostMapping("/topup")
    public ResponseEntity<BalanceDto> topUp(@RequestHeader("X-Authenticated-User") String username,
                                            @RequestBody TopUpRequest req) {
        return ResponseEntity.ok(creditsService.topUp(username, req));
    }

    @PostMapping("/charge")
    public ResponseEntity<BalanceDto> charge(@RequestHeader("X-Authenticated-User") String username,
                                             @RequestBody ChargeRequest req) {
        return ResponseEntity.ok(creditsService.charge(username, req));
    }

    @PostMapping("/transfer")
    public ResponseEntity<BalanceDto> transfer(@RequestHeader("X-Authenticated-User") String username,
                                               @RequestBody TransferRequest req) {
        return ResponseEntity.ok(creditsService.transfer(username, req));
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDto>> listTransactions(@RequestHeader("X-Authenticated-User") String username) {
        return ResponseEntity.ok(creditsService.listTransactions(username));
    }
}
