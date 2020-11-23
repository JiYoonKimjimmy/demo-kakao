package com.demo.kakao.controller;

import com.demo.kakao.entity.RandomMoneyWallet;
import com.demo.kakao.model.PostResponse;
import com.demo.kakao.repository.RandomMoneyWalletRepository;
import com.demo.kakao.service.RandomMoneyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RandomMoneyTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private RandomMoneyWalletRepository randomMoneyWalletRepository;
    @Autowired
    private RandomMoneyService randomMoneyService;

    @Test
    public void postTest() throws Exception {
        /**
         * random money wallet 생성
         * (뿌릴 금액 & 뿌릴 인원수만 지정)
         */
        RandomMoneyWallet wallet = RandomMoneyWallet
                                    .builder()
                                    .totalAmount(7500)
                                    .totalCount(7)
                                    .build();

        String content = objectMapper.writeValueAsString(wallet);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-USER-ID", "1");
        headers.add("X-ROOM-ID", "1");

        final ResultActions actions = mvc.perform(post("/random-money")
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
        ).andDo(print());

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true));
    }

    @Test
    public void putTest() throws Exception {
        RandomMoneyWallet wallet = randomMoneyWalletRepository.findById(1L).get();
        String content = objectMapper.writeValueAsString(PostResponse.builder().token(wallet.getToken()).build());

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-USER-ID", "2");
        headers.add("X-ROOM-ID", "1");

        final ResultActions actions = mvc.perform(put("/random-money")
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
        ).andDo(print());

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true));

    }

    @Test
    public void getTest() throws Exception {
        RandomMoneyWallet wallet = randomMoneyWalletRepository.findById(1L).get();

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-USER-ID", "1");
        headers.add("X-ROOM-ID", "1");

        final ResultActions actions = mvc.perform(get("/random-money/" + wallet.getToken())
                .headers(headers)
                .accept(MediaType.APPLICATION_JSON)
        ).andDo(print());

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value(true));
    }

    @Test
    public void put2000ErrorTest() throws Exception {
        /**
         * 본인의 뿌리기 수령시 "-2000" exception 발생
         */
        RandomMoneyWallet wallet = randomMoneyWalletRepository.findById(1L).get();
        String content = objectMapper.writeValueAsString(PostResponse.builder().token(wallet.getToken()).build());

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-USER-ID", "1");
        headers.add("X-ROOM-ID", "1");

        final ResultActions actions = mvc.perform(put("/random-money")
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
        ).andDo(print());

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("code").value("-2000"));
    }

    @Test
    public void put2001ErrorTest() throws Exception {
        /**
         * 대화방에 유효하지 않은 뿌리기 수령시 "-2001" exception 발생
         */
        RandomMoneyWallet wallet = randomMoneyWalletRepository.findById(1L).get();
        String content = objectMapper.writeValueAsString(PostResponse.builder().token(wallet.getToken()).build());

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-USER-ID", "2");
        headers.add("X-ROOM-ID", "2");

        final ResultActions actions = mvc.perform(put("/random-money")
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(content)
                .accept(MediaType.APPLICATION_JSON)
        ).andDo(print());

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("code").value("-2001"));
    }

    @Test
    public void put2002ErrorTest() throws Exception {
        /**
         * 같은 뿌리기를 수령한 경우 "-2002" exception 발생
         */
        RandomMoneyWallet wallet = randomMoneyWalletRepository.findById(1L).get();
        String content = objectMapper.writeValueAsString(PostResponse.builder().token(wallet.getToken()).build());

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-USER-ID", "2");
        headers.add("X-ROOM-ID", "1");

        ResultActions actions = null;
        for (int i = 0; i < 2; i++) {
            actions = mvc.perform(put("/random-money")
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(content)
                    .accept(MediaType.APPLICATION_JSON)
            ).andDo(print());
        }

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("code").value("-2002"));
    }

    @Test
    public void get3000ErrorTest() throws Exception {
        /**
         * 뿌리기 등록 사용자와 조회 사용자가 다른 경우, "-3000" exception 발생
         */
        RandomMoneyWallet wallet = randomMoneyWalletRepository.findById(1L).get();

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-USER-ID", "2");
        headers.add("X-ROOM-ID", "1");

        final ResultActions actions = mvc.perform(get("/random-money/" + wallet.getToken())
                .headers(headers)
                .accept(MediaType.APPLICATION_JSON)
        ).andDo(print());

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("code").value("-3000"));
    }


    @Test
    public void get3001ErrorTest() throws Exception {
        /**
         * 유효한 뿌리기 token 아닌 경우, "-3001" exception 발생
         */
        RandomMoneyWallet wallet = randomMoneyWalletRepository.findById(1L).get();

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-USER-ID", "2");
        headers.add("X-ROOM-ID", "1");

        final ResultActions actions = mvc.perform(get("/random-money/" + wallet.getToken().substring(0, 2))
                .headers(headers)
                .accept(MediaType.APPLICATION_JSON)
        ).andDo(print());

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("success").value(false))
                .andExpect(jsonPath("code").value("-3001"));
    }

}
