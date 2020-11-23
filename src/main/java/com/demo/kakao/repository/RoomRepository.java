package com.demo.kakao.repository;

import com.demo.kakao.entity.Room;
import com.demo.kakao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
