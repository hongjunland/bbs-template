package com.hongjunland.bbstemplate.post.infrastructure;

import com.hongjunland.bbstemplate.board.domain.QBoard;
import com.hongjunland.bbstemplate.post.domain.QComment;
import com.hongjunland.bbstemplate.post.domain.QPost;
import com.hongjunland.bbstemplate.post.domain.QPostLike;
import com.hongjunland.bbstemplate.post.dto.PostSummaryResponse;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostJpaRepositoryImpl implements PostJpaRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<PostSummaryResponse> findPostSummaryList(Long boardId, Long userId, Pageable pageable) {
        QPost post = QPost.post;
        QBoard board = QBoard.board;
        QPostLike postLike = QPostLike.postLike;
        QComment comment = QComment.comment; // 댓글 QueryDSL

        // 사용자가 좋아요 눌렀는지 여부 표현식 (비회원이면 false)
        Expression<Boolean> isLikedExpression = userId == null
                ? Expressions.constant(false)
                : JPAExpressions.selectOne()
                .from(postLike)
                .where(postLike.post.eq(post), postLike.user.id.eq(userId))
                .exists();
        // 댓글 수 표현식 (서브쿼리)
        Expression<Long> commentCountExpression = JPAExpressions
                .select(comment.id.countDistinct())
                .from(comment)
                .where(comment.post.eq(post));
        List<PostSummaryResponse> content = queryFactory
                .select(Projections.constructor(
                        PostSummaryResponse.class,
                        post.id,                              // Long id
                        post.board.id,                        // Long boardId
                        post.title,                           // String title
                        post.author,                          // String author
                        post.content.substring(0, 100),       // String contentSnippet (예: 미리보기 100자)
                        postLike.id.countDistinct(),          // long likeCount
                        isLikedExpression,                    // boolean isLiked
                        commentCountExpression,               // long commentCount
                        post.updatedAt                        // LocalDateTime updatedAt
                ))
                .from(post)
                .leftJoin(post.board, board)
                .leftJoin(postLike).on(postLike.post.eq(post))
                .where(board.id.eq(boardId))
                .groupBy(post.id, post.board.id, post.title, post.content)
                .orderBy(post.createdAt.desc()) // 최신순 정렬
                .offset(pageable.getOffset()) // Offset 적용
                .limit(pageable.getPageSize()) // 페이지 크기 적용
                .fetch();
        long totalCount = Optional.ofNullable(
                queryFactory
                        .select(post.count())
                        .from(post)
                        .where(post.board.id.eq(boardId))
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(content, pageable, totalCount);
    }

}
