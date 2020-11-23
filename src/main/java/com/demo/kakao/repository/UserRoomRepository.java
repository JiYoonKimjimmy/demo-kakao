package com.demo.kakao.repository;

import com.demo.kakao.entity.UserRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoomRepository extends JpaRepository<UserRoom, Long> {
    Optional<UserRoom> findByUserIdAndRoomId(long userId, long roomId);
}
