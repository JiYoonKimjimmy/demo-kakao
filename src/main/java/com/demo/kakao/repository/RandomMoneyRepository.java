package com.demo.kakao.repository;

import com.demo.kakao.entity.RandomMoney;
import com.demo.kakao.entity.RandomMoneyWallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RandomMoneyRepository extends JpaRepository<RandomMoney, Long> {
    List<RandomMoney> findByRandomMoneyWalletIdAndReceivedUserIdNotNull(Long walletId);

    List<RandomMoney> findByRandomMoneyWalletIdAndReceivedUserId(Long walletId, long userId);

    void deleteByRandomMoneyWallet(RandomMoneyWallet wallet);
}
