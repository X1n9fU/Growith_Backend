package dev.book.user.user_category;

import dev.book.global.entity.Category;
import dev.book.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public UserCategory(UserEntity user, Category category) {
        this.user = user;
        this.category = category;
    }

    public void setUser(UserEntity user){
        this.user = user;
    }
}
