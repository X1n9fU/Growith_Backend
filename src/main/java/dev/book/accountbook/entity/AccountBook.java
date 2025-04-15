package dev.book.accountbook.entity;

import dev.book.accountbook.type.Category;
import dev.book.accountbook.type.CategoryType;
import dev.book.accountbook.type.Frequency;
import dev.book.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AccountBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Enumerated(value = EnumType.STRING)
    private Category category;
    @Enumerated(value = EnumType.STRING)
    private CategoryType type;
    private int amount;
    @CreatedDate
    private LocalDateTime createDate;
    @LastModifiedDate
    private LocalDateTime modifyDate;
    private LocalDateTime endDate;
    private String memo;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    private Frequency frequency;
    private Integer month;
    private Integer day;

    public void modifyTitle(String title) {
        this.title = title;
    }

    public void modifyCategory(Category category) {
        this.category = category;
    }

    public void modifyAmount(int amount) {
        this.amount = amount;
    }

    public void modifyDate() {
        this.modifyDate = LocalDateTime.now();
    }

    public void modifyMemo(String memo) {
        this.memo = memo;
    }

    public void modifyFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public void modifyMonth(Integer month) {
        this.month = month;
    }

    public void modifyDay(Integer day) {
        this.day = day;
    }

    public void modifyEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}
