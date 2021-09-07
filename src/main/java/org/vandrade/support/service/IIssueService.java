package org.vandrade.support.service;

import org.vandrade.support.dao.CreateIssueDAO;
import org.vandrade.support.dao.UpdateIssueDAO;
import org.vandrade.support.generated.enums.IssuePriority;
import org.vandrade.support.generated.enums.IssueStatus;
import org.vandrade.support.generated.tables.pojos.Issue;
import org.vandrade.support.generated.tables.pojos.IssueComment;
import org.jooq.lambda.tuple.Tuple3;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

/**
 * Author: Vitor Andrade
 * Date: 10/9/18
 * Time: 4:19 PM
 */

public interface IIssueService {
    /**
     * Retrieves an issue by its id.
     *
     * @param id must not be null.
     * @return the issue with the given id
     */
    Issue findOne(Long id);


    /**
     * Retrieves all the issues that respect the provided filters
     *
     * @param filters a tuple containing the filterable parameters
     * @param pageable a paging and sorting entity
     * @return a collection of the filtered and sorted issues
     */
    Collection<Issue> findAll(Tuple3<IssueStatus, IssuePriority, Long> filters, Pageable pageable);


    /**
     * Create a new issue
     *
     * @param createIssueDAO DAO containing the required fields
     * @return the create issue
     */
    Issue create(CreateIssueDAO createIssueDAO);


    /**
     * Update an existing issue
     *
     * @param updateIssueDAO
     * @return
     */
    Issue update(UpdateIssueDAO updateIssueDAO);


    Boolean exists(Long id);


    Long count();


    /**
     * Add a note to an issue (noteable entity)
     *
     * @param targetId the target issue for the note
     * @param IssueComment
     * @return
     */
    IssueComment addNote(Long targetId, IssueComment IssueComment);


    Issue close(Long id);
}
