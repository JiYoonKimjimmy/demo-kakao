package com.demo.kakao;

import com.demo.kakao.entity.RandomMoneyWallet;
import com.demo.kakao.entity.Room;
import com.demo.kakao.entity.User;
import com.demo.kakao.entity.UserRoom;
import com.demo.kakao.repository.RoomRepository;
import com.demo.kakao.repository.UserRepository;
import com.demo.kakao.repository.UserRoomRepository;
import com.demo.kakao.service.RandomMoneyService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.List;

@SpringBootApplication
@EnableJpaAuditing  // JPA Auditing 활성화
public class DemoKakaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoKakaoApplication.class, args);
    }

    @Bean
    public CommandLineRunner mappingUser(UserRepository userRepository,
                                         RoomRepository roomRepository,
                                         UserRoomRepository userRoomRepository,
                                         RandomMoneyService randomMoneyService) {
        return args -> {
            final int ROOM_SIZE = 2;
            final int USER_SIZE = 5;

            for (int i = 0; i < ROOM_SIZE; i++) {
                roomRepository.save(new Room());
            }

            for (int i = 1; i <= USER_SIZE; i++) {
                userRepository.save(new User());

                User user = userRepository.findById((long) i).orElseThrow();
                List<Room> rooms = roomRepository.findAll();
                UserRoom room1 = UserRoom.builder().user(user).room(rooms.get(0)).build();
                UserRoom room2 = UserRoom.builder().user(user).room(rooms.get(1)).build();

                userRoomRepository.save(room1);
                if (i < 3) {
                    userRoomRepository.save(room2);
                }
            }
            RandomMoneyWallet wallet = RandomMoneyWallet.builder().totalAmount(7500).totalCount(7).build();
            randomMoneyService.spreadRandomMoney(1, 1, wallet);
        };

    }


}
