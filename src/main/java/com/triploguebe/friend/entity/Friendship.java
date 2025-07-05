package com.triploguebe.friend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long friendshipId;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime requestDate;

    // 친구 요청 보낸 사람
    private Long userId;

    // 친구 요청 받은 사람
    private Long friendId;

    public enum Status {
        PENDING, ACCEPTED, REJECTED, DELETED
    }
}