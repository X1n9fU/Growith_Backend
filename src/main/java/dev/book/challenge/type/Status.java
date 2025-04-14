package dev.book.challenge.type;

public enum Status {
    RECRUITING("모집중"),
    RECRUITED("모집완료"),
    IN_PROGRESS("진행중"),
    COMPLETED("종료");

    private final String desc;

    Status(String desc) {
        this.desc = desc;
    }

}
