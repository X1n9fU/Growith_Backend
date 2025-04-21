package dev.book.accountbook.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    TRANSFER("이체", CategoryType.INCOME),
    SALARY("급여", CategoryType.INCOME),
    SAVING_INVESTMENT("저축 / 투자", CategoryType.INCOME),

    FOOD("식비", CategoryType.SPEND),
    CAFE_SNACK("카페 / 간식", CategoryType.SPEND),
    CONVENIENCE_STORE("편의점 / 마트 / 잡화", CategoryType.SPEND),
    ALCOHOL_ENTERTAINMENT("술 / 유흥", CategoryType.SPEND),
    SHOPPING("쇼핑", CategoryType.SPEND),
    HOBBY("취미 / 여가", CategoryType.SPEND),
    HEALTH("의료 / 건강 / 피트니스", CategoryType.SPEND),
    HOUSING_COMMUNICATION("주거 / 통신", CategoryType.SPEND),
    FINANCE("보험 / 세금 / 기타금융", CategoryType.SPEND),
    BEAUTY("미용", CategoryType.SPEND),
    TRANSPORTATION("교통 / 자동차", CategoryType.SPEND),
    TRAVEL("여행 / 숙박", CategoryType.SPEND),
    EDUCATION("교육", CategoryType.SPEND),
    LIVING("생활", CategoryType.SPEND),
    DONATION("기부 / 후원", CategoryType.SPEND),
    CARD_PAYMENT("카드대금", CategoryType.SPEND),
    DEFERRED_PAYMENT("후불결제대금", CategoryType.SPEND),

    NONE("카테고리 미설정", CategoryType.SPEND);

    private final String koreanName;
    private final CategoryType type;

    @JsonCreator
    public static Category from(String value) {
        return Category.valueOf(value.toUpperCase());
    }
}
