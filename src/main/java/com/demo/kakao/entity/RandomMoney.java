package com.demo.kakao.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "random_money")
public class RandomMoney {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long receivedUserId;
    private int amount;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    @JsonBackReference
    private RandomMoneyWallet randomMoneyWallet;

}
