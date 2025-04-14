package dev.book.challenge.dummy;

import dev.book.user_challenge.entity.UserChallenge;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DummyUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "user")
    private List<UserChallenge> userChallenges;

    private DummyUser(String name) {
        this.name = name;
    }

    public static DummyUser of(String name) {
        return new DummyUser(name);
    }

}
