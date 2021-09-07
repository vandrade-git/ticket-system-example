package org.vandrade.support.dao;

import org.vandrade.support.generated.enums.IssuePriority;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Author: Vitor Andrade
 * Date: 10/15/18
 * Time: 11:47 AM
 */

public class CreateIssueDAO {
    // Fields >>
    @NotBlank
    private String title;

    @NotBlank
    private String browserInfo;

    @NotBlank
    private String pageUrl;

    private String screenshot;

    @NotNull
    private IssuePriority priority;

    @NotBlank
    private String description;
    // << Fields

    // Getters and Setters >>
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrowserInfo() {
        return browserInfo;
    }

    public void setBrowserInfo(String browserInfo) {
        this.browserInfo = browserInfo;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(String screenshot) {
        this.screenshot = screenshot;
    }

    public IssuePriority getPriority() {
        return priority;
    }

    public void setPriority(IssuePriority priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    // << Getters and Setters
}
