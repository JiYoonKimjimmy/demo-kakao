package com.demo.kakao.entity;

import com.demo.kakao.common.converter.TokenCryptoConverter;
import com.demo.kakao.entity.common.CommonDateEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "random_money_wallet")
public class RandomMoneyWallet extends CommonDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_id")
    private Long id;
    private Long userId;
    private String token;
    private int totalAmount;
    private int totalCount;

    @ManyToOne
    @JoinColumn(name = "room_id")
    @JsonBackReference
    private Room room;

    @OneToMany(mappedBy = "randomMoneyWallet")
    @JsonManagedReference
    private List<RandomMoney> randomMoneyList = new ArrayList<>();

}
