package dev.book.challenge.rank.dto.response;

public class RankResponse {
    private final String name;
    private final Long totalSpend;

    public RankResponse(String name, Long totalSpend) {
        this.name = name;
        this.totalSpend = totalSpend;
    }

    public String getName() {
       return name;
    }

    public Long getTotalSpend () {
        return totalSpend;
    }
}
