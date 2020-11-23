package com.demo.kakao.repository;

import com.demo.kakao.entity.RandomMoneyWallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RandomMoneyWalletRepository extends JpaRepository<RandomMoneyWallet, Long> {
    RandomMoneyWallet findByToken(String token);
}
