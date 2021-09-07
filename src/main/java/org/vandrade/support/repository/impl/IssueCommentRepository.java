package org.vandrade.support.repository.impl;

import org.vandrade.support.generated.Tables;
import org.vandrade.support.generated.tables.pojos.IssueComment;
import org.vandrade.support.repository.IIssueCommentRepository;
import org.jooq.DSLContext;
import org.jooq.lambda.tuple.Tuple0;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Author: Vitor Andrade
 * Date: 10/11/18
 * Time: 1:43 PM
 */

@Repository
public class IssueCommentRepository implements IIssueCommentRepository {
    @Autowired
    public IssueCommentRepository(DSLContext jooq) {
        this.jooq = jooq;
    }

    // Basic methods >>
    @Override
    public IssueComment findOne(Long id) {
        return jooq.select(Tables.ISSUE_COMMENT.fields())
                .from(Tables.ISSUE_COMMENT)
                .where(Tables.ISSUE_COMMENT.ID.eq(id))
                .fetchOne()
                .into(IssueComment.class);
    }

    @Override
    public Collection<IssueComment> findAll() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Collection<IssueComment> findAll(Tuple0 filters, Pageable pageable) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Collection<IssueComment> findAll(Collection<Long> ids) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public IssueComment save(IssueComment entity) {
        return jooq.insertInto(Tables.ISSUE_COMMENT, Tables.ISSUE_COMMENT.ID, Tables.ISSUE_COMMENT.AUTHOR_ID, Tables.ISSUE_COMMENT.ISSUE_ID, Tables.ISSUE_COMMENT.BODY)
                .values(entity.getId(), entity.getAuthorId(), entity.getIssueId(), entity.getBody())
                .onDuplicateKeyUpdate()
                .set(Tables.ISSUE_COMMENT.BODY, entity.getBody())
                .returning(Tables.ISSUE_COMMENT.fields())
                .fetchOne()
                .into(IssueComment.class);
    }

    @Override
    public Collection<IssueComment> save(Collection<IssueComment> entities) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Boolean exists(Long id) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Long count() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Integer delete(Long id) {
        throw new UnsupportedOperationException("Not supported");
    }
    // << Basic methods

    // Specific methods >>
    @Override
    public Collection<IssueComment> findByNoteable(Long noteableId) {
        return jooq.select(Tables.ISSUE_COMMENT.fields())
                .from(Tables.ISSUE_COMMENT)
                .where(Tables.ISSUE_COMMENT.ISSUE_ID.eq(noteableId))
                .fetch()
                .into(IssueComment.class);
    }
    // << Specific methods

    // Fields >>
    private final DSLContext jooq;
    // << Fields
}
