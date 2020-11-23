package com.demo.kakao.service;

import com.demo.kakao.entity.User;
import com.demo.kakao.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * User 정보 다건 조회
     * @return
     */
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    /**
     * User 정보 단건 조회
     * @param userId
     * @return
     */
    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow();
    }

    /**
     * User 정보 단건 저장
     * @return
     */
    public User save() {
        User user = new User();
        return userRepository.save(user);
    }
}
