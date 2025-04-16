package dev.book.challenge.type;

public enum Category {
    FOOD("음식"), CAFE("카페"), SHOPPING("쇼핑"), TRANSPORTATION("교통"),ENTERTAINMENT("오락"),
    NONE("미설정");
    private final String desc;

    Category(String desc) {
        this.desc = desc;
    }
}
