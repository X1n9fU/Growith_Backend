package dev.book.challenge.type;

public enum Release {
    PUBLIC("전체공개"),
    PRIVATE("비공개");

    private final String desc;

    Release(String desc) {
        this.desc = desc;
    }
}
