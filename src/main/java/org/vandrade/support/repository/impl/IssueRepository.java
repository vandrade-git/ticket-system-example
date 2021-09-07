package org.vandrade.support.repository.impl;

import org.vandrade.support.generated.Tables;
import org.vandrade.support.generated.enums.IssuePriority;
import org.vandrade.support.generated.enums.IssueStatus;
import org.vandrade.support.generated.tables.pojos.Issue;
import org.vandrade.support.repository.IIssueRepository;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.SortField;
import org.jooq.impl.DSL;
import org.jooq.lambda.tuple.Tuple3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.jooq.impl.DSL.noCondition;

/**
 * Author: Vitor Andrade
 * Date: 10/9/18
 * Time: 12:43 PM
 */

@Repository
public class IssueRepository implements IIssueRepository {
    @Autowired
    public IssueRepository(DSLContext jooq) {
        this.jooq = jooq;
    }

    // Basic methods >>
    @Override
    public Issue findOne(Long id) {
        return jooq.select(Tables.ISSUE.fields())
                .from(Tables.ISSUE)
                .where(Tables.ISSUE.ID.eq(id))
                .fetchOne()
                .into(Issue.class);
    }

    @Override
    public Collection<Issue> findAll() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Collection<Issue> findAll(Tuple3<IssueStatus, IssuePriority, Long> filters, Pageable pageable) {
        List<Condition> filterParams = new ArrayList<>();
        filterParams.add(filters.v1 != null ? Tables.ISSUE.STATUS.eq(filters.v1) : noCondition());
        filterParams.add(filters.v2 != null ? Tables.ISSUE.PRIORITY.eq(filters.v2) : noCondition());
        filterParams.add(filters.v3 != null ? Tables.ISSUE.ASSIGNED_TO.eq(filters.v3) : noCondition());

////                case "text_search":
////                    filterParams.add(condition("ticket_system_issue ==> ?::zdbquery::json", filter.get(aux)));
////                    break;

        List<SortField<?>> sortParams = new ArrayList<>();
        for (Sort.Order aux: pageable.getSort()) {
            Field<?> temp = Tables.ISSUE.field(aux.getProperty());
            if (aux.getDirection() == Sort.Direction.ASC) {
                sortParams.add(temp.asc());
            } else {
                sortParams.add(temp.desc());
            }
        }

        return jooq.select(Tables.ISSUE.fields())
                .from(Tables.ISSUE)
                .where(filterParams)
                .orderBy(sortParams)
                .limit(pageable.getPageSize())
                .offset(pageable.getPageNumber() * pageable.getPageSize())
                .fetch()
                .into(Issue.class);
    }

    @Override
    public Collection<Issue> findAll(Collection<Long> ids) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Issue save(Issue entity) {
        return jooq.insertInto(Tables.ISSUE, Tables.ISSUE.ID, Tables.ISSUE.AUTHOR_ID, Tables.ISSUE.TITLE, Tables.ISSUE.BROWSER_INFO, Tables.ISSUE.SCREENSHOT, Tables.ISSUE.PAGE_URL, Tables.ISSUE.DESCRIPTION, Tables.ISSUE.PRIORITY)
                .values(entity.getId(), entity.getAuthorId(), entity.getTitle(), entity.getBrowserInfo(), entity.getScreenshot(), entity.getPageUrl(), entity.getDescription(), entity.getPriority())
                .onDuplicateKeyUpdate()
                .set(Tables.ISSUE.TITLE, entity.getTitle())
                .set(Tables.ISSUE.BROWSER_INFO, entity.getBrowserInfo())
                .set(Tables.ISSUE.SCREENSHOT, entity.getScreenshot())
                .set(Tables.ISSUE.PAGE_URL, entity.getPageUrl())
                .set(Tables.ISSUE.DESCRIPTION, entity.getDescription())
                .set(Tables.ISSUE.STATUS, entity.getStatus())
                .set(Tables.ISSUE.PRIORITY, entity.getPriority())
                .set(Tables.ISSUE.ASSIGNED_TO, entity.getAssignedTo())
                .set(Tables.ISSUE.CLOSED_BY, entity.getClosedBy())
                .returning(Tables.ISSUE.fields())
                .fetchOne()
                .into(Issue.class);
    }

    @Override
    public Collection<Issue> save(Collection<Issue> entities) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Boolean exists(Long id) {
        return jooq.fetchExists(jooq.selectOne()
                .from(Tables.ISSUE)
                .where(Tables.ISSUE.ID.eq(id)));
    }

    @Override
    public Long count() {
        return jooq.select(DSL.count(Tables.ISSUE.ID))
                .from(Tables.ISSUE)
                .fetchOne(0, Long.class);
    }

    @Override
    public Integer delete(Long id) {
        throw new UnsupportedOperationException("Not supported");
    }
    // << Basic methods

    // Specific methods >>
    @Override
    public Collection<Issue> findByAuthor(Long authorId) {
        return jooq.select(Tables.ISSUE.fields())
                .from(Tables.ISSUE)
                .where(Tables.ISSUE.AUTHOR_ID.eq(authorId))
                .fetch()
                .into(Issue.class);
    }
    // << Specific methods

    // Fields >>
    private final DSLContext jooq;
    // << Fields
}
