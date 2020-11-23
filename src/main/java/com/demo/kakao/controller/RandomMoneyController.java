package com.demo.kakao.controller;

import com.demo.kakao.common.response.ListResult;
import com.demo.kakao.common.response.SingleResult;
import com.demo.kakao.common.service.ResponseService;
import com.demo.kakao.entity.RandomMoney;
import com.demo.kakao.entity.RandomMoneyWallet;
import com.demo.kakao.service.RandomMoneyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/random-money")
public class RandomMoneyController {

    private final ResponseService responseService;
    private final RandomMoneyService randomMoneyService;

    @PostMapping
    public SingleResult save(@RequestHeader("X-USER-ID") long userId,
                             @RequestHeader("X-ROOM-ID") long roomId,
                             @RequestBody RandomMoneyWallet reqWallet) {
        return responseService.getSingleResult(randomMoneyService.spreadRandomMoney(userId, roomId, reqWallet));
    }

    @PutMapping
    public SingleResult update(@RequestHeader("X-USER-Id") long userId,
                               @RequestHeader("X-ROOM-Id") long roomId,
                               @RequestParam String token) {
        return responseService.getSingleResult(randomMoneyService.receiveRandomMoney(userId, roomId, token));
    }

    @GetMapping(value = "/{token}")
    public SingleResult get(@RequestHeader("X-USER-Id") long userId,
                            @RequestHeader("X-ROOM-Id") long roomId,
                            @PathVariable String token) {
        return responseService.getSingleResult(randomMoneyService.getRandomMoney(userId, roomId, token));
    }

}
