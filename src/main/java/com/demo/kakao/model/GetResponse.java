package com.demo.kakao.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class GetResponse {
    private LocalDateTime createAt;
    private int totalAmount;
    private int receivedAmount;
    private List<GetDetail> infos;
}
