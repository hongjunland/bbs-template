package com.hongjunland.bbstemplate.post.infrastructure;

import com.hongjunland.bbstemplate.common.response.CursorPage;
import com.hongjunland.bbstemplate.post.domain.Comment;
import com.hongjunland.bbstemplate.post.domain.QComment;
import com.hongjunland.bbstemplate.post.domain.QCommentLike;
import com.hongjunland.bbstemplate.post.dto.ReplyCommentListResponse;
import com.hongjunland.bbstemplate.post.dto.RootCommentListResponse;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class CommentJpaRepositoryImpl implements CommentJpaRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    @Override
    public Page<RootCommentListResponse> findRootCommentsByPostId(Long postId, Long userId, Pageable pageable) {
        QComment comment = QComment.comment;
        QCommentLike commentLike = QCommentLike.commentLike;

        // 좋아요 여부 (해당 사용자가 좋아요를 눌렀는지)
        Expression<Boolean> isLikedExpression = userId == null
                ? Expressions.constant(false) // 비회원이면 false
                : JPAExpressions.selectOne()
                .from(commentLike)
                .where(commentLike.comment.eq(comment), commentLike.user.id.eq(userId))
                .exists();

        // 좋아요 개수
        Expression<Long> likeCountExpression = JPAExpressions
                .select(commentLike.id.countDistinct())
                .from(commentLike)
                .where(commentLike.comment.eq(comment));
        // 댓글 수 표현식 (서브쿼리)
        Expression<Long> replyCountExpression = JPAExpressions
                .select(comment.id.countDistinct())
                .from(comment)
                .where(comment.parent.id.eq(comment.id));
        // QueryDSL을 이용한 루트 댓글 조회
        List<RootCommentListResponse> content = queryFactory
                .select(Projections.constructor(
                        RootCommentListResponse.class,
                        comment.id,
                        comment.post.id,
                        comment.parent.id,
                        likeCountExpression, // 좋아요 개수
                        replyCountExpression, // 대댓글 개수
                        isLikedExpression, // 좋아요 여부
                        comment.author,
                        comment.content,
                        comment.updatedAt
                ))
                .from(comment)
                .where(
                        comment.post.id.eq(postId),
                        comment.parent.isNull() // 루트 댓글만 조회
                )
                .orderBy(comment.createdAt.desc()) // 최신순 정렬
                .offset(pageable.getOffset()) // Offset 적용
                .limit(pageable.getPageSize()) // 페이지 크기 적용
                .fetch();

        // 전체 댓글 개수 조회
        long totalCount = Optional.ofNullable(
                queryFactory
                        .select(comment.count())
                        .from(comment)
                        .where(comment.post.id.eq(postId), comment.parent.isNull())
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(content, pageable, totalCount);
    }
    @Override
    public CursorPage<ReplyCommentListResponse> findReplyListByParentCommentId(Long commentId, Long userId, LocalDateTime cursor, int offset) {
        QComment comment = QComment.comment;
        QCommentLike commentLike = QCommentLike.commentLike;

        // 댓글 수 표현식 (서브쿼리)
        Expression<Long> replyCountExpression = JPAExpressions
                .select(comment.id.countDistinct())
                .from(comment)
                .where(comment.parent.id.eq(comment.id));
        // 좋아요 여부 (사용자가 좋아요를 눌렀는지)
        Expression<Boolean> isLikedExpression = userId == null
                ? Expressions.constant(false) // 비회원은 false
                    : JPAExpressions.selectOne()
                    .from(commentLike)
                    .where(commentLike.comment.eq(comment), commentLike.user.id.eq(userId))
                    .exists();

        // 좋아요 개수 (대댓글 기준으로 count)
        Expression<Long> likeCountExpression = JPAExpressions
                .select(commentLike.id.countDistinct())
                .from(commentLike)
                .where(commentLike.comment.eq(comment));

        List<ReplyCommentListResponse> replies = queryFactory
                .select(Projections.constructor(
                        ReplyCommentListResponse.class,
                        comment.id,
                        comment.post.id,
                        comment.parent.id,
                        likeCountExpression, // 좋아요 개수
                        replyCountExpression, // 대댓글 개수
                        isLikedExpression, // 좋아요 여부
                        comment.author,
                        comment.content,
                        comment.createdAt,
                        comment.updatedAt
                ))
                .from(comment)
                .where(
                        comment.parent.id.eq(commentId), // 대댓글만 조회
                        cursor != null ? comment.createdAt.lt(cursor) : null // ✅ 커서 적용
                )
                .orderBy(comment.createdAt.desc()) // 최신순 정렬
                .limit(offset)
                .fetch();
        LocalDateTime nextCursor = replies.isEmpty() ? null : replies.get(replies.size() - 1).createdAt();
        return new CursorPage<>(replies, nextCursor);
    }

}
