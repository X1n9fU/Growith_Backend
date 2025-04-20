package dev.book.challenge.type;

import dev.book.accountbook.type.Category;

import java.util.List;

public enum ChallengeCategory {
    FOOD("음식"), CAFE("카페"), SHOPPING("쇼핑"), TRANSPORTATION("교통"),ENTERTAINMENT("오락"),
    NONE("미설정");
    private final String desc;

    ChallengeCategory(String desc) {
        this.desc = desc;
    }

    public List<Category> getRelatedSpendingCategories() {
        return switch (this) {
            case FOOD -> List.of(Category.FOOD, Category.CONVENIENCE_STORE);
            case CAFE -> List.of(Category.CAFE_SNACK);
            case SHOPPING -> List.of(Category.SHOPPING, Category.BEAUTY);
            case TRANSPORTATION -> List.of(Category.TRANSPORTATION);
            case ENTERTAINMENT -> List.of(Category.ALCOHOL_ENTERTAINMENT, Category.HOBBY);
            case NONE -> List.of();
        };
    }


}
