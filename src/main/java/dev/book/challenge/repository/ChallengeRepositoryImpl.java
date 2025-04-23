package dev.book.challenge.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.book.challenge.dto.response.ChallengeReadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static dev.book.challenge.entity.QChallenge.challenge;
import static dev.book.challenge.user_challenge.entity.QUserChallenge.userChallenge;


@Repository
@RequiredArgsConstructor
public class ChallengeRepositoryImpl implements ChallengeJpaRepository {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public Page<ChallengeReadResponse> search(String title, String text, Pageable pageable) {
        List<ChallengeReadResponse> content = jpaQueryFactory.select(Projections.constructor(ChallengeReadResponse.class,
                        challenge.id,
                        challenge.title,
                        challenge.capacity,
                        userChallenge.count().intValue(),
                        challenge.status
                ))
                .from(challenge)
                .leftJoin(userChallenge).on(userChallenge.challenge.eq(challenge))
                .where(eqTitle(title),
                        eqText(text))
                .groupBy(
                        challenge.id,
                        challenge.title,
                        challenge.capacity,
                        challenge.status
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalCount = jpaQueryFactory.select(challenge.count())
                .from(challenge)
                .where(eqTitle(title),
                        eqText(text))
                .fetchOne();
        return new PageImpl<>(content, pageable, totalCount);

    }

    private BooleanExpression eqTitle(String title) {
        if (title == null) {
            return null;
        }
        return challenge.title.eq(title);
    }

    private BooleanExpression eqText(String text) {
        if (text == null) {
            return null;
        }
        return challenge.text.eq(text);
    }


}