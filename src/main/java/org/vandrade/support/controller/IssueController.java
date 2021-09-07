package org.vandrade.support.controller;

import org.vandrade.support.dao.CreateIssueDAO;
import org.vandrade.support.generated.enums.IssuePriority;
import org.vandrade.support.generated.enums.IssueStatus;
import org.vandrade.support.generated.tables.pojos.Issue;
import org.vandrade.support.service.IIssueService;
import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ServerErrorException;

import java.util.Collection;

/**
 * Author: Vitor Andrade
 * Date: 10/9/18
 * Time: 4:56 PM
 */

@Controller
@RequestMapping(path = "/issues")
public class IssueController {
    @Autowired
    public IssueController(IIssueService issueService) {
        this.issueService = issueService;
    }

    @GetMapping(path = "/{id}")
    public String findOne(Model model, @PathVariable("id") Long id) {
        try {
            Issue issue = issueService.findOne(id);
            model.addAttribute("issue", issue);
            return "issues/view_issue";
        } catch (SecurityException exception) {
            return "errors/error403";
        } catch (NullPointerException exception) {
            return "errors/error404";
        } catch (ServerErrorException exception) {
            return "errors/error500";
        }
    }

    @GetMapping
    public String findAll(Model model, @RequestParam(value = "status", required = false) IssueStatus status, @RequestParam(value = "priority", required = false) IssuePriority priority, @RequestParam(value = "assigned_to", required = false) Long assignedTo, Pageable pageable) {
        try {
            Tuple3<IssueStatus, IssuePriority, Long> requestParams = Tuple.tuple(status, priority, assignedTo);
            Collection<Issue> issues = issueService.findAll(requestParams, pageable);
            model.addAttribute("issues", issues);
            return "issues/view_issues";
        } catch (SecurityException exception) {
            return "errors/error403";
        } catch (NullPointerException exception) {
            return "errors/error404";
        } catch (ServerErrorException exception) {
            return "errors/error500";
        }
    }

    @PostMapping
    public String create(@ModelAttribute CreateIssueDAO createIssueDAO) {
        try {
            Issue issue = issueService.create(createIssueDAO);
            return "redirect:/issues/" + issue.getId();
        } catch (SecurityException exception) {
            return "errors/error403";
        } catch (NullPointerException exception) {
            return "errors/error404";
        } catch (ServerErrorException exception) {
            return "errors/error500";
        }
    }

//    @PostMapping
//    public String update(@ModelAttribute UpdateIssueDAO updateIssueDAO) {
//        try {
//            Issue issue = issueService.update(updateIssueDAO);
//            return "redirect:/issues/" + issue.getId();
//        } catch (SecurityException exception) {
//            return "errors/error403";
//        } catch (NullPointerException exception) {
//            return "errors/error404";
//        } catch (ServerErrorException exception) {
//            return "errors/error500";
//        }
//    }

    @PostMapping(path = "/{id}/add-note")
    public String addNote(@PathVariable("id") Long targetId, @ModelAttribute Issue Issue) {
        return "";
    }

    // Fields >>
    private static final Logger LOGGER = LoggerFactory.getLogger(IssueController.class);

    private final IIssueService issueService;
    // << Fields
}
