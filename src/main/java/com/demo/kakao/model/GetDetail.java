package com.demo.kakao.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetDetail {
    private int amount;
    private Long receivedUserId;
}
