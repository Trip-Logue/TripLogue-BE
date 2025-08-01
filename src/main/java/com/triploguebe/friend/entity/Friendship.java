package com.triploguebe.friend.entity;

import com.triploguebe.user.entity.User;
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
    private FriendshipStatus status;

    private LocalDateTime requestDate;

    // 친구 요청 보낸 사람
    private Long userId;

    // 친구 요청 받은 사람
    private Long friendId;

    // friendship.getRequester()로 요청 보낸 사람 User 객체에 접근 가능
    // 요청 보낸 사람 User 객체 (연관관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false) // userId 필드가 DB 컬럼으로 이미 있어 중복 매핑 방지용
    private User requester;

    // 요청 받은 사람 User 객체 (연관관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friendId", insertable = false, updatable = false)
    private User receiver;

}