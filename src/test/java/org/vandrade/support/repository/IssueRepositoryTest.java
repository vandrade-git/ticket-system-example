package org.vandrade.support.repository;

import org.vandrade.support.generated.Tables;
import org.vandrade.support.generated.enums.IssuePriority;
import org.vandrade.support.generated.enums.IssueStatus;
import org.vandrade.support.generated.tables.pojos.Issue;
import org.vandrade.support.repository.impl.IssueRepository;
import com.github.paulcwarren.ginkgo4j.Ginkgo4jConfiguration;
import com.github.paulcwarren.ginkgo4j.Ginkgo4jSpringRunner;
import org.jooq.DSLContext;
import org.jooq.lambda.tuple.Tuple;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.AfterEach;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.BeforeEach;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Context;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.Describe;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.It;
import static com.github.paulcwarren.ginkgo4j.Ginkgo4jDSL.JustBeforeEach;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Author: Vitor Andrade
 * Date: 10/10/18
 * Time: 8:41 AM
 */

@SuppressWarnings("ALL")
@RunWith(Ginkgo4jSpringRunner.class)
@Ginkgo4jConfiguration(threads = 1)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class IssueRepositoryTest {
    // Fields >>
    @Autowired
    private DSLContext jooq;

    @Autowired
    private IssueRepository issueRepository;

    private Issue retSingle;
    private Collection<Issue> retCollection;
    private Boolean retExists;
    private Long retCount;
    private Exception retException;
    // << Fields

    {
        Describe("IssueRepository test", () -> {
            It("issueRepository bean should not be null", () -> {
                assertThat(issueRepository, notNullValue());
            });


            BeforeEach(() -> {
                jooq.insertInto(Tables.USER, Tables.USER.USER_NAME, Tables.USER.PASSWORD, Tables.USER.EMAIL, Tables.USER.FIRST_NAME, Tables.USER.LAST_NAME)
                        .values("username", "password", "username@mail.com", "first", "last")
                        .execute();
            });


            AfterEach(() -> {
                jooq.truncate(Tables.USER).restartIdentity().cascade().execute();
                jooq.truncate(Tables.ISSUE).restartIdentity().cascade().execute();
            });


            /*
             * Find One
             */
            Context("findOne <id: Long>", () -> {
                JustBeforeEach(() -> {
                    try {
                        retSingle = issueRepository.findOne(1L);
                    } catch (Exception exception) {
                        retException = exception;
                    }
                });

                Context("when a valid Issue entity exists", () -> {
                    BeforeEach(() -> {
                        jooq.insertInto(Tables.ISSUE, Tables.ISSUE.AUTHOR_ID, Tables.ISSUE.TITLE, Tables.ISSUE.BROWSER_INFO, Tables.ISSUE.SCREENSHOT, Tables.ISSUE.PAGE_URL, Tables.ISSUE.DESCRIPTION, Tables.ISSUE.PRIORITY)
                                .values(1L, "title", "browser_info", "screenshot", "page_url", "description", IssuePriority.NORMAL)
                                .execute();
                    });

                    It("should return a valid Issue entity", () -> {
                        assertThat(retSingle, notNullValue());
                        assertThat(retSingle.getId(), is(1L));
                        assertThat(retSingle.getAuthorId(), is(1L));
                        assertThat(retSingle.getTitle(), is("title"));
                        assertThat(retSingle.getBrowserInfo(), is("browser_info"));
                        assertThat(retSingle.getScreenshot(), is("screenshot"));
                        assertThat(retSingle.getPageUrl(), is("page_url"));
                        assertThat(retSingle.getDescription(), is("description"));
                        assertThat(retSingle.getStatus(), is(IssueStatus.NEW));
                        assertThat(retSingle.getPriority(), is(IssuePriority.NORMAL));
                    });
                });

                Context("when a valid Issue entity does not exist", () -> {
                    It("should throw a NullPointerException", () -> {
                        assertThat(retException, notNullValue());
                        assertThat(retException, instanceOf(NullPointerException.class));
                    });
                });
            });


            /*
             * Find All
             */
            Context("findAll <filters: Tuple3<IssueStatus, IssuePriority, Long>, pageable: Pageable>", () -> {
                Context("filters <null, null, null>", () -> {
                    JustBeforeEach(() -> {
                        try {
                            retCollection = issueRepository.findAll(Tuple.tuple(null, null, null), PageRequest.of(0, 20));
                        } catch (Exception exception) {
                            retException = exception;
                        }
                    });

                    Context("when a valid Issue entity exists", () -> {
                        BeforeEach(() -> {
                            jooq.insertInto(Tables.ISSUE, Tables.ISSUE.AUTHOR_ID, Tables.ISSUE.TITLE, Tables.ISSUE.BROWSER_INFO, Tables.ISSUE.SCREENSHOT, Tables.ISSUE.PAGE_URL, Tables.ISSUE.DESCRIPTION, Tables.ISSUE.PRIORITY)
                                    .values(1L, "title", "browser_info", "screenshot", "page_url", "description", IssuePriority.NORMAL)
                                    .execute();
                        });

                        It("should return a Page with 1 element", () -> {
                            assertThat(retCollection, notNullValue());
                            assertThat(retCollection.size(), is(1));
                        });
                    });

                    Context("when a valid Issue entity does not exist", () -> {
                        It("should return an empty Page", () -> {
                            assertThat(retCollection, notNullValue());
                            assertThat(retCollection.size(), is(0));
                        });
                    });
                });

                Context("filters <NEW, null, null>", () -> {
                    JustBeforeEach(() -> {
                        try {
                            retCollection = issueRepository.findAll(Tuple.tuple(IssueStatus.NEW, null, null), PageRequest.of(0, 20));
                        } catch (Exception exception) {
                            retException = exception;
                        }
                    });

                    Context("when a valid Issue entity exists", () -> {
                        BeforeEach(() -> {
                            jooq.insertInto(Tables.ISSUE, Tables.ISSUE.AUTHOR_ID, Tables.ISSUE.TITLE, Tables.ISSUE.BROWSER_INFO, Tables.ISSUE.SCREENSHOT, Tables.ISSUE.PAGE_URL, Tables.ISSUE.DESCRIPTION, Tables.ISSUE.PRIORITY)
                                    .values(1L, "title", "browser_info", "screenshot", "page_url", "description", IssuePriority.NORMAL)
                                    .execute();
                        });

                        It("should return a Page with 1 element", () -> {
                            assertThat(retCollection, notNullValue());
                            assertThat(retCollection.size(), is(1));
                        });
                    });

                    Context("when a valid Issue entity does not exist", () -> {
                        It("should return an empty Page", () -> {
                            assertThat(retCollection, notNullValue());
                            assertThat(retCollection.size(), is(0));
                        });
                    });
                });

                Context("filters <null, NORMAL, null>", () -> {
                    JustBeforeEach(() -> {
                        try {
                            retCollection = issueRepository.findAll(Tuple.tuple(null, IssuePriority.NORMAL, null), PageRequest.of(0, 20));
                        } catch (Exception exception) {
                            retException = exception;
                        }
                    });

                    Context("when a valid Issue entity exists", () -> {
                        BeforeEach(() -> {
                            jooq.insertInto(Tables.ISSUE, Tables.ISSUE.AUTHOR_ID, Tables.ISSUE.TITLE, Tables.ISSUE.BROWSER_INFO, Tables.ISSUE.SCREENSHOT, Tables.ISSUE.PAGE_URL, Tables.ISSUE.DESCRIPTION, Tables.ISSUE.PRIORITY)
                                    .values(1L, "title", "browser_info", "screenshot", "page_url", "description", IssuePriority.NORMAL)
                                    .execute();
                        });

                        It("should return a Page with 1 element", () -> {
                            assertThat(retCollection, notNullValue());
                            assertThat(retCollection.size(), is(1));
                        });
                    });

                    Context("when a valid Issue entity does not exist", () -> {
                        It("should return an empty Page", () -> {
                            assertThat(retCollection, notNullValue());
                            assertThat(retCollection.size(), is(0));
                        });
                    });
                });

                Context("filters <null, null, 1L>", () -> {
                    JustBeforeEach(() -> {
                        try {
                            retCollection = issueRepository.findAll(Tuple.tuple(null, null, 1L), PageRequest.of(0, 20));
                        } catch (Exception exception) {
                            retException = exception;
                        }
                    });

                    Context("when a valid Issue entity exists", () -> {
                        BeforeEach(() -> {
                            jooq.insertInto(Tables.ISSUE, Tables.ISSUE.AUTHOR_ID, Tables.ISSUE.TITLE, Tables.ISSUE.BROWSER_INFO, Tables.ISSUE.SCREENSHOT, Tables.ISSUE.PAGE_URL, Tables.ISSUE.DESCRIPTION, Tables.ISSUE.PRIORITY, Tables.ISSUE.ASSIGNED_TO)
                                    .values(1L, "title", "browser_info", "screenshot", "page_url", "description", IssuePriority.NORMAL, 1L)
                                    .execute();
                        });

                        It("should return a Page with 1 element", () -> {
                            assertThat(retCollection, notNullValue());
                            assertThat(retCollection.size(), is(1));
                        });
                    });

                    Context("when a valid Issue entity does not exist", () -> {
                        It("should return an empty Page", () -> {
                            assertThat(retCollection, notNullValue());
                            assertThat(retCollection.size(), is(0));
                        });
                    });
                });
            });


            /*
             * Save
             */
            Context("save <entity: Issue>", () -> {
                Context("when a valid Issue entity is provided", () -> {
                    Context("when the provided ID does not exist", () -> {
                        JustBeforeEach(() -> {
                            try {
                                retSingle = issueRepository.save(new Issue().setAuthorId(1L).setTitle("title").setBrowserInfo("browser_info").setScreenshot("screenshot").setPageUrl("page_url").setDescription("description").setPriority(IssuePriority.NORMAL));
                            } catch (Exception exception) {
                                retException = exception;
                            }
                        });

                        It("should create a new Issue entity and return it", () -> {
                            assertThat(retSingle, notNullValue());
                            assertThat(retSingle.getId(), is(1L));
                            assertThat(retSingle.getAuthorId(), is(1L));
                            assertThat(retSingle.getTitle(), is("title"));
                            assertThat(retSingle.getBrowserInfo(), is("browser_info"));
                            assertThat(retSingle.getScreenshot(), is("screenshot"));
                            assertThat(retSingle.getPageUrl(), is("page_url"));
                            assertThat(retSingle.getDescription(), is("description"));
                            assertThat(retSingle.getStatus(), is(IssueStatus.NEW));
                            assertThat(retSingle.getPriority(), is(IssuePriority.NORMAL));
                        });
                    });

                    Context("when the provided ID exists", () -> {
                        BeforeEach(() -> {
                            jooq.insertInto(Tables.ISSUE, Tables.ISSUE.AUTHOR_ID, Tables.ISSUE.TITLE, Tables.ISSUE.BROWSER_INFO, Tables.ISSUE.SCREENSHOT, Tables.ISSUE.PAGE_URL, Tables.ISSUE.DESCRIPTION, Tables.ISSUE.PRIORITY)
                                    .values(1L, "title", "browser_info", "screenshot", "page_url", "description", IssuePriority.NORMAL)
                                    .execute();
                        });

                        JustBeforeEach(() -> {
                            try {
                                retSingle = issueRepository.save(new Issue().setId(1L).setAuthorId(1L).setTitle("title").setBrowserInfo("browser_info").setScreenshot("screenshot").setPageUrl("page_url").setDescription("description").setStatus(IssueStatus.NEW).setPriority(IssuePriority.IMMEDIATE));
                            } catch (Exception exception) {
                                retException = exception;
                            }
                        });

                        It("should update the existing Issue entity and return it", () -> {
                            assertThat(retSingle, notNullValue());
                            assertThat(retSingle.getId(), is(1L));
                            assertThat(retSingle.getAuthorId(), is(1L));
                            assertThat(retSingle.getTitle(), is("title"));
                            assertThat(retSingle.getBrowserInfo(), is("browser_info"));
                            assertThat(retSingle.getScreenshot(), is("screenshot"));
                            assertThat(retSingle.getPageUrl(), is("page_url"));
                            assertThat(retSingle.getDescription(), is("description"));
                            assertThat(retSingle.getStatus(), is(IssueStatus.NEW));
                            assertThat(retSingle.getPriority(), is(IssuePriority.IMMEDIATE));
                        });
                    });
                });

                Context("when an invalid Issue entity is provided", () -> {
                    Context("when the provided ID does not exist", () -> {
                        JustBeforeEach(() -> {
                            try {
                                retSingle = issueRepository.save(new Issue().setTitle("title").setBrowserInfo("browser_info").setScreenshot("screenshot").setPageUrl("page_url").setDescription("description").setPriority(IssuePriority.NORMAL));
                            } catch (Exception exception) {
                                retException = exception;
                            }
                        });

                        It("should throw a DataIntegrityViolationException", () -> {
                            assertThat(retException, instanceOf(DataIntegrityViolationException.class));
                        });
                    });

                    Context("when the provided ID exists", () -> {
                        BeforeEach(() -> {
                            jooq.insertInto(Tables.ISSUE, Tables.ISSUE.AUTHOR_ID, Tables.ISSUE.TITLE, Tables.ISSUE.BROWSER_INFO, Tables.ISSUE.SCREENSHOT, Tables.ISSUE.PAGE_URL, Tables.ISSUE.DESCRIPTION, Tables.ISSUE.PRIORITY)
                                    .values(1L, "title", "browser_info", "screenshot", "page_url", "description", IssuePriority.NORMAL)
                                    .execute();
                        });

                        JustBeforeEach(() -> {
                            try {
                                retSingle = issueRepository.save(new Issue().setId(1L).setAuthorId(1L).setTitle(null).setBrowserInfo("browser_info").setScreenshot("screenshot").setPageUrl("page_url").setDescription("description").setPriority(IssuePriority.NORMAL));
                            } catch (Exception exception) {
                                retException = exception;
                            }
                        });

                        It("should throw a DataIntegrityViolationException", () -> {
                            assertThat(retException, instanceOf(DataIntegrityViolationException.class));
                        });
                    });
                });
            });


            /*
             * Exists
             */
            Context("exists <id: Long>", () -> {
                JustBeforeEach(() -> {
                    try {
                        retExists = issueRepository.exists(1L);
                    } catch (Exception exception) {
                        retException = exception;
                    }
                });

                Context("when a valid Issue entity exists", () -> {
                    BeforeEach(() -> {
                        jooq.insertInto(Tables.ISSUE, Tables.ISSUE.AUTHOR_ID, Tables.ISSUE.TITLE, Tables.ISSUE.BROWSER_INFO, Tables.ISSUE.SCREENSHOT, Tables.ISSUE.PAGE_URL, Tables.ISSUE.DESCRIPTION, Tables.ISSUE.PRIORITY)
                                .values(1L, "title", "browser_info", "screenshot", "page_url", "description", IssuePriority.NORMAL)
                                .execute();
                    });

                    It("should return true", () -> {
                        assertThat(retExists, is(true));
                    });
                });

                Context("when a valid Issue entity does not exist", () -> {
                    It("should return false", () -> {
                        assertThat(retExists, is(false));
                    });
                });
            });


            /*
             * Count
             */
            Context("count <>", () -> {
                JustBeforeEach(() -> {
                    try {
                        retCount = issueRepository.count();
                    } catch (Exception exception) {
                        retException = exception;
                    }
                });

                Context("when a valid Issue entity exists", () -> {
                    BeforeEach(() -> {
                        jooq.insertInto(Tables.ISSUE, Tables.ISSUE.AUTHOR_ID, Tables.ISSUE.TITLE, Tables.ISSUE.BROWSER_INFO, Tables.ISSUE.SCREENSHOT, Tables.ISSUE.PAGE_URL, Tables.ISSUE.DESCRIPTION, Tables.ISSUE.PRIORITY)
                                .values(1L, "title", "browser_info", "screenshot", "page_url", "description", IssuePriority.NORMAL)
                                .execute();
                    });

                    It("should return a correct count", () -> {
                        assertThat(retCount, is(1L));
                    });
                });

                Context("when a valid Issue entity does not exist", () -> {
                    It("should return 0", () -> {
                        assertThat(retCount, is(0L));
                    });
                });
            });
        });
    }

    @Test
    public void noop() {
        // NO-OP
    }
}
