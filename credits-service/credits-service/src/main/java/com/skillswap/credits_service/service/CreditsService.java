package com.skillswap.credits_service.service;

import com.skillswap.credits_service.dto.*;
import com.skillswap.credits_service.entities.Account;
import com.skillswap.credits_service.entities.TransactionRecord;
import com.skillswap.credits_service.repository.AccountRepository;
import com.skillswap.credits_service.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreditsService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public BalanceDto getBalance(String username) {
        Account account = accountRepository.findByUsername(username)
                .orElseGet(() -> Account.builder().username(username).balance(0L).build());
        return new BalanceDto(account.getBalance());
    }

    @Transactional
    public BalanceDto topUp(String username, TopUpRequest req) {
        if (req.getAmount() == null || req.getAmount() <= 0) throw new IllegalArgumentException("Amount must be > 0");
        Account account = accountRepository.findByUsername(username)
                .orElse(Account.builder().username(username).balance(0L).build());
        account.setBalance(account.getBalance() + req.getAmount());
        accountRepository.save(account);

        TransactionRecord tr = TransactionRecord.builder()
                .type("TOPUP")
                .amount(req.getAmount())
                .fromUser(null)
                .toUser(username)
                .createdAt(Instant.now())
                .description(req.getDescription())
                .build();
        transactionRepository.save(tr);

        return new BalanceDto(account.getBalance());
    }

    @Transactional
    public BalanceDto charge(String username, ChargeRequest req) {
        if (req.getAmount() == null || req.getAmount() <= 0) throw new IllegalArgumentException("Amount must be > 0");
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        if (account.getBalance() < req.getAmount()) throw new RuntimeException("Insufficient balance");
        account.setBalance(account.getBalance() - req.getAmount());
        accountRepository.save(account);

        TransactionRecord tr = TransactionRecord.builder()
                .type("CHARGE")
                .amount(req.getAmount())
                .fromUser(username)
                .toUser(null)
                .createdAt(Instant.now())
                .description(req.getDescription())
                .build();
        transactionRepository.save(tr);

        return new BalanceDto(account.getBalance());
    }

    @Transactional
    public BalanceDto transfer(String fromUsername, TransferRequest req) {
        if (req.getAmount() == null || req.getAmount() <= 0) throw new IllegalArgumentException("Amount must be > 0");
        if (fromUsername.equals(req.getToUsername())) throw new IllegalArgumentException("Cannot transfer to self");

        Account from = accountRepository.findByUsername(fromUsername)
                .orElseThrow(() -> new RuntimeException("Sender account not found"));
        if (from.getBalance() < req.getAmount()) throw new RuntimeException("Insufficient balance");

        Account to = accountRepository.findByUsername(req.getToUsername())
                .orElse(Account.builder().username(req.getToUsername()).balance(0L).build());

        from.setBalance(from.getBalance() - req.getAmount());
        to.setBalance(to.getBalance() + req.getAmount());

        accountRepository.save(from);
        accountRepository.save(to);

        TransactionRecord out = TransactionRecord.builder()
                .type("TRANSFER_OUT")
                .amount(req.getAmount())
                .fromUser(fromUsername)
                .toUser(req.getToUsername())
                .createdAt(Instant.now())
                .description(req.getDescription())
                .build();
        TransactionRecord in = TransactionRecord.builder()
                .type("TRANSFER_IN")
                .amount(req.getAmount())
                .fromUser(fromUsername)
                .toUser(req.getToUsername())
                .createdAt(Instant.now())
                .description(req.getDescription())
                .build();

        transactionRepository.save(out);
        transactionRepository.save(in);

        return new BalanceDto(from.getBalance());
    }

    public List<TransactionDto> listTransactions(String username) {
        List<TransactionRecord> records = transactionRepository.findByFromUserOrToUserOrderByCreatedAtDesc(username, username);
        return records.stream().map(r -> TransactionDto.builder()
                .id(r.getId())
                .type(r.getType())
                .amount(r.getAmount())
                .fromUser(r.getFromUser())
                .toUser(r.getToUser())
                .createdAt(r.getCreatedAt())
                .description(r.getDescription())
                .build()).collect(Collectors.toList());
    }
}
