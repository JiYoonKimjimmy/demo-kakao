package com.demo.kakao.service;

import com.demo.kakao.common.advice.exception.*;
import com.demo.kakao.common.converter.TokenCryptoConverter;
import com.demo.kakao.entity.*;
import com.demo.kakao.model.GetDetail;
import com.demo.kakao.model.GetResponse;
import com.demo.kakao.model.PostResponse;
import com.demo.kakao.model.PutResponse;
import com.demo.kakao.repository.RandomMoneyRepository;
import com.demo.kakao.repository.RandomMoneyWalletRepository;
import com.demo.kakao.repository.RoomRepository;
import com.demo.kakao.repository.UserRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RandomMoneyService {

    private final RoomRepository roomRepository;
    private final UserRoomRepository userRoomRepository;
    private final RandomMoneyRepository randomMoneyRepository;
    private final RandomMoneyWalletRepository randomMoneyWalletRepository;

    public final int TOKEN_LENGTH = 3;

    /**
     * 뿌리기 등록
     * @param userId    뿌리기 요청 user id
     * @param roomId    뿌리기 대화방 room id
     * @param wallet    뿌리기 정보
     * @return PostResponse
     */
    public PostResponse spreadRandomMoney(long userId, long roomId, RandomMoneyWallet wallet) {
        // room 정보 조회
        Room room = roomRepository.findById(roomId).get();
        // 뿌리기 정보 저장
        wallet.setRoom(room);                           // 대화방 정보
        wallet.setUserId(userId);                       // 뿌리기 요청 user id
        wallet.setToken(generateToken(userId, roomId)); // token 정보

        RandomMoneyWallet responseWallet = randomMoneyWalletRepository.save(wallet);

        // 뿌리기 목록 저장
        List<RandomMoney> list = saveRandomMoney(wallet.getTotalAmount(), wallet.getTotalCount(), responseWallet);

        // 저장된 뿌리기 목록과 total_count 일치하지 않은 경우,
        if (list.size() != wallet.getTotalCount()) {
            // 해당 뿌리기 목록 전체 삭제
            randomMoneyRepository.deleteByRandomMoneyWallet(wallet);
            throw new InconsistentException();
        }

        return PostResponse
                .builder()
                .token(responseWallet.getToken())
                .build();
    }

    /**
     * RandomMoney 목록 저장
     * @param totalAmount   뿌리기 전체 금액
     * @param totalCount    뿌리기 인원수
     * @param wallet        뿌리기 정보
     * @return List<RandomMoney>
     */
    public List<RandomMoney> saveRandomMoney(int totalAmount, int totalCount, RandomMoneyWallet wallet) {
        List<RandomMoney> list = new ArrayList<>();

        /**
         * [뿌리기 금액 나누기]
         * 1. 1 / N 로 나누고,
         * 2. 남은 금액 = 전체 / (1 / N)
         * 3. 난수와 일치한 index 항목에 남은 금액 추가
         */
        int amount = totalAmount / totalCount;
        int remainder = totalAmount - (amount * totalCount);
        int randomNumber = new Random().nextInt(totalCount);

        if ((amount * totalCount) + remainder == totalAmount) {
            for (int i = 0; i < totalCount; i++) {
                if (i == randomNumber) {
                    amount += remainder;
                }
                RandomMoney randomMoney = RandomMoney
                                            .builder()
                                            .amount(amount)
                                            .randomMoneyWallet(wallet)
                                            .build();
                list.add(randomMoney);
            }
        }

        // 뿌리기 목록 전체 저장
        return randomMoneyRepository.saveAll(list);
    }

    /**
     * 뿌리기 받기 정보 저장
     * @param token     뿌리기 token
     * @return PutResponse
     */
    public PutResponse receiveRandomMoney(long userId, long roomId, String token) {
        RandomMoneyWallet wallet = randomMoneyWalletRepository.findByToken(token);

        // 본인의 뿌리기 건인 경우, OwnerReceivedException 처리
        if (userId == wallet.getUserId()) {
            throw new OwnerReceivedException();
        }

        // 10분 지난 뿌리기 건인 경우, TimeExpiredException 처리
        LocalDateTime nowDate = LocalDateTime.now();
        LocalDateTime compareDate = wallet.getCreatedAt().plusMinutes(10);
        if (nowDate.isAfter(compareDate)) {
            throw new TimeExpiredException();
        }

        // 뿌리기 건의 대화방 사용자가 아닌 경우, NotExistInRoomException 처리
        long walletRoomId = wallet.getRoom().getId();
        Optional<UserRoom> user = userRoomRepository.findByUserIdAndRoomId(userId, walletRoomId);
        if (roomId != walletRoomId || user.isEmpty()) {
            throw new NotExistInRoomException();
        }

        // 해당 뿌리기 건을 이미 받은 사용자인 경우, DuplicateReceivedException 처리
        int duplicateCount = randomMoneyRepository.findByRandomMoneyWalletIdAndReceivedUserId(wallet.getId(), userId).size();
        if (duplicateCount > 0) {
            throw new DuplicateReceivedException();
        }

        // 뿌리기 목록 중 received_user_id가 할당되지 않은 맨처음 random money
        RandomMoney randomMoney = wallet
                                    .getRandomMoneyList()
                                    .stream()
                                    .filter(item -> item.getReceivedUserId() == null)
                                    .collect(Collectors.toList())
                                    .get(0);

        // 받는 user_id 할당
        randomMoney.setReceivedUserId(userId);

        // random money 받기 저장
        randomMoneyRepository.save(randomMoney);

        return PutResponse
                .builder()
                .amount(randomMoney.getAmount())
                .build();
    }

    /**
     * 뿌리기 받기 정보 조회
     * @param userId    뿌리기 받기 user id
     * @param roomId    대화방 room id
     * @param token     뿌리기 token
     * @return GetResponse
     */
    public GetResponse getRandomMoney(long userId, long roomId, String token) {
        // 유효하지 않은 token 인 경우, NotValidTokenException 처리
        if (token.length() != TOKEN_LENGTH) {
            throw new NotValidTokenException();
        }

        // RandomMoneyWallet 뿌리기 정보 조회
        RandomMoneyWallet wallet = randomMoneyWalletRepository.findByToken(token);

        // 조회 사용자와 뿌린 사용자가 다른 경우, NotValidUserException 처리
        if (wallet.getId() != userId) {
            throw new NotValidUserException();
        }

        // 생성 기간이 7일 넘어가는 경우, PeriodExpiredException 처리
        LocalDateTime nowDate = LocalDateTime.now();
        LocalDateTime compareDate = wallet.getCreatedAt().plusDays(7);
        if (nowDate.isAfter(compareDate)) {
            throw new PeriodExpiredException();
        }

        // RandomMoney 뿌리기 목록 정보 조회
        List<RandomMoney> randomMoneyList = randomMoneyRepository.findByRandomMoneyWalletIdAndReceivedUserIdNotNull(wallet.getId());

        AtomicInteger receivedAmount = new AtomicInteger();

        List<GetDetail> details = randomMoneyList.stream().map(item -> {
            receivedAmount.addAndGet(item.getAmount());
            return GetDetail
                    .builder()
                    .amount(item.getAmount())
                    .receivedUserId(item.getReceivedUserId())
                    .build();
        }).collect(Collectors.toList());

        return GetResponse
                .builder()
                .createAt(wallet.getCreatedAt())
                .totalAmount(wallet.getTotalAmount())
                .receivedAmount(receivedAmount.get())
                .infos(details)
                .build();
    }

    /**
     *
     * @param userId    뿌리기 요청 user id
     * @param roomId    뿌리기 대화방 room id
     * @return String
     */
    public String generateToken(long userId, long roomId) {
        TokenCryptoConverter converter = new TokenCryptoConverter();
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssms"));
        return converter.convertToDatabaseColumn(String.valueOf(userId) + String.valueOf(roomId) + now);
    }

}
