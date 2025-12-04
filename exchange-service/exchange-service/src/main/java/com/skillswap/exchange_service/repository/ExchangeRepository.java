package com.skillswap.exchange_service.repository;

import com.skillswap.exchange_service.entities.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExchangeRepository extends JpaRepository<Exchange, Long> {
    List<Exchange> findByRequester(String requester);
    List<Exchange> findByProvider(String provider);
}