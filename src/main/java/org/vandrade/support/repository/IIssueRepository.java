package org.vandrade.support.repository;

import org.vandrade.support.generated.enums.IssuePriority;
import org.vandrade.support.generated.enums.IssueStatus;
import org.vandrade.support.generated.tables.pojos.Issue;
import org.jooq.lambda.tuple.Tuple3;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * Author: Vitor Andrade
 * Date: 10/9/18
 * Time: 12:31 PM
 */

public interface IIssueRepository extends BaseRepository<Issue, Long, Tuple3<IssueStatus, IssuePriority, Long>> {
    @Transactional(readOnly = true)
    Collection<Issue> findByAuthor(Long authorId);
}
