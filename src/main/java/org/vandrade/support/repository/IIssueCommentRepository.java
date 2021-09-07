package org.vandrade.support.repository;

import org.vandrade.support.generated.tables.pojos.IssueComment;
import org.jooq.lambda.tuple.Tuple0;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * Author: Vitor Andrade
 * Date: 10/9/18
 * Time: 12:31 PM
 */

public interface IIssueCommentRepository extends BaseRepository<IssueComment, Long, Tuple0> {
    @Transactional(readOnly = true)
    Collection<IssueComment> findByNoteable(Long noteableId);
}
