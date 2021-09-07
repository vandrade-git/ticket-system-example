package org.vandrade.support.service.impl;

import org.vandrade.support.dao.CreateIssueDAO;
import org.vandrade.support.dao.UpdateIssueDAO;
import org.vandrade.support.generated.enums.IssuePriority;
import org.vandrade.support.generated.enums.IssueStatus;
import org.vandrade.support.generated.tables.pojos.Issue;
import org.vandrade.support.generated.tables.pojos.IssueComment;
import org.vandrade.support.repository.IIssueRepository;
import org.vandrade.support.repository.IIssueCommentRepository;
import org.vandrade.support.security.CustomUserDetails;
import org.vandrade.support.security.IAuthenticationFacade;
import org.vandrade.support.service.IIssueService;
import org.jooq.exception.DataAccessException;
import org.jooq.exception.MappingException;
import org.jooq.exception.TooManyRowsException;
import org.jooq.lambda.tuple.Tuple3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerErrorException;

import java.util.Collection;

/**
 * Author: Vitor Andrade
 * Date: 10/9/18
 * Time: 4:19 PM
 */

@Service
public class IssueService implements IIssueService {
    @Autowired
    public IssueService(IIssueRepository issueRepository, IIssueCommentRepository IssueCommentRepository, IAuthenticationFacade authenticationFacade) {
        this.issueRepository = issueRepository;
        this.IssueCommentRepository = IssueCommentRepository;
        this.authenticationFacade = authenticationFacade;
    }


    @Override
    public Issue findOne(Long id) {
        final Issue ret; // the target

        try {
            // find the entity
            ret = issueRepository.findOne(id);

            if (ret == null) { // this should never happen but just in case
                LOGGER.debug("Entity does not exist");
                throw new NullPointerException("Entity does not exist");
            }
        } catch (NullPointerException exception) {
            LOGGER.debug("Entity does not exist");
            throw new NullPointerException("Entity does not exist");
        } catch (MappingException exception) {
            LOGGER.debug("Error while mapping record to pojo");
            throw new ServerErrorException("Error while mapping record to pojo", exception);
        } catch (TooManyRowsException exception) {
            LOGGER.debug("Multiple rows with the same ID");
            throw new ServerErrorException("Multiple rows with the same ID", exception);
        } catch (DataAccessException exception) {
            LOGGER.debug("Error while executing SQL statement");
            throw new ServerErrorException("Error while executing SQL statement", exception);
        }

        return ret;
    }


    @Override
    public Collection<Issue> findAll(Tuple3<IssueStatus, IssuePriority, Long> filters, Pageable pageable) {
        final Collection<Issue> ret;

        try {
            // find the entities
            ret = issueRepository.findAll(filters, pageable);
        } catch (MappingException exception) {
            LOGGER.debug("Error while mapping record to pojo");
            throw new ServerErrorException("Error while mapping record to pojo", exception);
        } catch (DataAccessException exception) {
            LOGGER.debug("Error while executing SQL statement");
            throw new ServerErrorException("Error while executing SQL statement", exception);
        }

        return ret;
    }


    @Override
    public Issue create(CreateIssueDAO createIssueDAO) {
        final Issue ret; // the created issue

        try {
            // get the logged user
            CustomUserDetails userDetails = authenticationFacade.getCustomUserDetails();

            if (userDetails == null) { // this should never happen but just in case
                LOGGER.debug("Unauthorized");
                throw new SecurityException("Unauthorized user");
            }

            // save the entity
            ret = issueRepository.save(new Issue().setAuthorId(userDetails.getId()).setTitle(createIssueDAO.getTitle()).setBrowserInfo(createIssueDAO.getBrowserInfo()).setPageUrl(createIssueDAO.getPageUrl()).setScreenshot(createIssueDAO.getScreenshot()).setDescription(createIssueDAO.getDescription()).setPriority(createIssueDAO.getPriority()));
        } catch (NullPointerException exception) {
            LOGGER.debug("Entity does not exist");
            throw new NullPointerException("Entity does not exist");
        } catch (MappingException exception) {
            LOGGER.debug("Error while mapping record to pojo");
            throw new ServerErrorException("Error while mapping record to pojo", exception);
        } catch (TooManyRowsException exception) {
            LOGGER.debug("Multiple rows with the same ID");
            throw new ServerErrorException("Multiple rows with the same ID", exception);
        } catch (DataAccessException exception) {
            LOGGER.debug("Error while executing SQL statement");
            throw new ServerErrorException("Error while executing SQL statement", exception);
        }

        return ret;
    }


    @Override
    public Issue update(UpdateIssueDAO updateIssueDAO) {
        return null;
    }


    @Override
    public Boolean exists(Long id) {
        final Boolean ret; // the target

        try {
            // check if entity exists
            ret = issueRepository.exists(id);
        } catch (MappingException exception) {
            LOGGER.debug("Error while mapping record to pojo");
            throw new ServerErrorException("Error while mapping record to pojo", exception);
        } catch (TooManyRowsException exception) {
            LOGGER.debug("Multiple rows with the same ID");
            throw new ServerErrorException("Multiple rows with the same ID", exception);
        } catch (DataAccessException exception) {
            LOGGER.debug("Error while executing SQL statement");
            throw new ServerErrorException("Error while executing SQL statement", exception);
        }

        return ret;
    }

    @Override
    public Long count() {
        final Long ret; // the target

        try {
            // count the entities
            ret = issueRepository.count();
        } catch (MappingException exception) {
            LOGGER.debug("Error while mapping record to pojo");
            throw new ServerErrorException("Error while mapping record to pojo", exception);
        } catch (TooManyRowsException exception) {
            LOGGER.debug("Multiple rows with the same ID");
            throw new ServerErrorException("Multiple rows with the same ID", exception);
        } catch (DataAccessException exception) {
            LOGGER.debug("Error while executing SQL statement");
            throw new ServerErrorException("Error while executing SQL statement", exception);
        }

        return ret;
    }

    @Override
    public IssueComment addNote(Long targetId, IssueComment IssueComment) {
        final Issue target;
        final IssueComment ret;

        try {
            // get the logged user
            CustomUserDetails userDetails = authenticationFacade.getCustomUserDetails();

            if (userDetails == null) { // this should never happen but just in case
                LOGGER.debug("Unauthorized");
                throw new SecurityException("Unauthorized user");
            }

            // find the target
            target = issueRepository.findOne(targetId);

            // can only react to an entity if it actually exists
            if (target == null) { // this should never happen but just in case
                LOGGER.debug("Can't react to a null entity");
                throw new NullPointerException("Target entity does not exist");
            }

            // save the note
            ret = IssueCommentRepository.save(new IssueComment().setAuthorId(userDetails.getId()).setIssueId(targetId).setBody(IssueComment.getBody()));
        } catch (NullPointerException exception) {
            LOGGER.debug("Entity does not exist");
            throw new NullPointerException("Entity does not exist");
        } catch (MappingException exception) {
            LOGGER.debug("Error while mapping record to pojo");
            throw new ServerErrorException("Error while mapping record to pojo", exception);
        } catch (TooManyRowsException exception) {
            LOGGER.debug("Multiple rows with the same ID");
            throw new ServerErrorException("Multiple rows with the same ID", exception);
        } catch (DataAccessException exception) {
            LOGGER.debug("Error while executing SQL statement");
            throw new ServerErrorException("Error while executing SQL statement", exception);
        }

        return ret;
    }

    @Override
    public Issue close(Long id) {
        final Issue ret; // the target

        try {
            // get the logged user
            CustomUserDetails userDetails = authenticationFacade.getCustomUserDetails();

            if (userDetails == null) { // this should never happen but just in case
                LOGGER.debug("Unauthorized");
                throw new SecurityException("Unauthorized user");
            }

            // find the entity
            Issue aux = issueRepository.findOne(id);

            if (aux == null) { // this should never happen but just in case
                LOGGER.debug("Entity does not exist");
                throw new NullPointerException("Entity does not exist");
            }

            ret = issueRepository.save(aux.setStatus(IssueStatus.CLOSED).setClosedBy(userDetails.getId()));
        } catch (NullPointerException exception) {
            LOGGER.debug("Entity does not exist");
            throw new NullPointerException("Entity does not exist");
        } catch (MappingException exception) {
            LOGGER.debug("Error while mapping record to pojo");
            throw new ServerErrorException("Error while mapping record to pojo", exception);
        } catch (TooManyRowsException exception) {
            LOGGER.debug("Multiple rows with the same ID");
            throw new ServerErrorException("Multiple rows with the same ID", exception);
        } catch (DataAccessException exception) {
            LOGGER.debug("Error while executing SQL statement");
            throw new ServerErrorException("Error while executing SQL statement", exception);
        }

        return ret;
    }

    // Fields >>
    private static final Logger LOGGER = LoggerFactory.getLogger(IssueService.class);

    private final IIssueRepository issueRepository;
    private final IIssueCommentRepository IssueCommentRepository;
    private final IAuthenticationFacade authenticationFacade;
    // << Fields
}
