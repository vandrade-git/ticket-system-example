package org.vandrade.support.controller;

import org.vandrade.support.generated.enums.IssuePriority;
import org.vandrade.support.generated.tables.pojos.Issue;
import org.vandrade.support.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author: Vitor Andrade
 * Date: 10/11/18
 * Time: 9:20 AM
 */

@Controller
public class HomeController {
    //@GetMapping({"/", "/home"})
    public ModelAndView displayArticle(Map<String, Object> model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        model.put("user", customUserDetails);
        model.put("title", "Hello");

        List<Issue> list = new ArrayList<>();
        list.add(new Issue().setId(1L).setAuthorId(1L).setTitle("title").setBrowserInfo("browser_info").setScreenshot("screenshot").setPageUrl("page_url").setDescription("description").setPriority(IssuePriority.NORMAL));
        list.add(new Issue().setId(2L).setAuthorId(1L).setTitle("title").setBrowserInfo("browser_info").setScreenshot("screenshot").setPageUrl("page_url").setDescription("description").setPriority(IssuePriority.NORMAL));
        list.add(new Issue().setId(3L).setAuthorId(1L).setTitle("title").setBrowserInfo("browser_info").setScreenshot("screenshot").setPageUrl("page_url").setDescription("description").setPriority(IssuePriority.NORMAL));
        list.add(new Issue().setId(4L).setAuthorId(1L).setTitle("title").setBrowserInfo("browser_info").setScreenshot("screenshot").setPageUrl("page_url").setDescription("description").setPriority(IssuePriority.NORMAL));
        list.add(new Issue().setId(5L).setAuthorId(1L).setTitle("title").setBrowserInfo("browser_info").setScreenshot("screenshot").setPageUrl("page_url").setDescription("description").setPriority(IssuePriority.NORMAL));
        list.add(new Issue().setId(6L).setAuthorId(1L).setTitle("title").setBrowserInfo("browser_info").setScreenshot("screenshot").setPageUrl("page_url").setDescription("description").setPriority(IssuePriority.NORMAL));
        list.add(new Issue().setId(7L).setAuthorId(1L).setTitle("title").setBrowserInfo("browser_info").setScreenshot("screenshot").setPageUrl("page_url").setDescription("description").setPriority(IssuePriority.NORMAL));
        list.add(new Issue().setId(8L).setAuthorId(1L).setTitle("title").setBrowserInfo("browser_info").setScreenshot("screenshot").setPageUrl("page_url").setDescription("description").setPriority(IssuePriority.NORMAL));
        list.add(new Issue().setId(9L).setAuthorId(1L).setTitle("title").setBrowserInfo("browser_info").setScreenshot("screenshot").setPageUrl("page_url").setDescription("description").setPriority(IssuePriority.NORMAL));
        list.add(new Issue().setId(10L).setAuthorId(1L).setTitle("title").setBrowserInfo("browser_info").setScreenshot("screenshot").setPageUrl("page_url").setDescription("description").setPriority(IssuePriority.NORMAL));

        model.put("issues", list);

        return new ModelAndView("index2", model);
    }

    @GetMapping(path = "/")
    public String dashboard() {
        return "dashboard";
    }
}
