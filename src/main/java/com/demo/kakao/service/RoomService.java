package com.demo.kakao.service;

import com.demo.kakao.entity.Room;
import com.demo.kakao.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    /**
     * room 정보 단건 조회
     * @param roomId
     * @return
     */
    public Room getRoom(Long roomId) {
        return roomRepository.findById(roomId).orElseThrow();
    }
}
