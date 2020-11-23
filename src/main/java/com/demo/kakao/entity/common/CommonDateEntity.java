package com.demo.kakao.entity.common;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass                               // 'createdAt' column 으로 인식
@EntityListeners(AuditingEntityListener.class)  // Auditing 기능 추가
public abstract class CommonDateEntity {
    @CreatedDate
    private LocalDateTime createdAt;
}