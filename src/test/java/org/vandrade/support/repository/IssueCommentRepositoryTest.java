package org.vandrade.support.repository;

import org.vandrade.support.generated.Tables;
import org.vandrade.support.generated.enums.IssuePriority;
import org.vandrade.support.generated.tables.pojos.IssueComment;
import org.vandrade.support.repository.impl.IssueCommentRepository;
import com.github.paulcwarren.ginkgo4j.Ginkgo4jConfiguration;
import com.github.paulcwarren.ginkgo4j.Ginkgo4jSpringRunner;
import org.jooq.DSLContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
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
 * Date: 10/12/18
 * Time: 9:22 AM
 */

@SuppressWarnings("ALL")
@RunWith(Ginkgo4jSpringRunner.class)
@Ginkgo4jConfiguration(threads = 1)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class IssueCommentRepositoryTest {
    // Fields >>
    @Autowired
    private DSLContext jooq;

    @Autowired
    private IssueCommentRepository IssueCommentRepository;

    private IssueComment retSingle;
    private Collection<IssueComment> retCollection;
    private Boolean retExists;
    private Long retCount;
    private Exception retException;
    // << Fields

    {
        Describe("IssueCommentRepository test", () -> {
            It("IssueCommentRepository bean should not be null", () -> {
                assertThat(IssueCommentRepository, notNullValue());
            });


            BeforeEach(() -> {
                jooq.insertInto(Tables.USER, Tables.USER.USER_NAME, Tables.USER.PASSWORD, Tables.USER.EMAIL, Tables.USER.FIRST_NAME, Tables.USER.LAST_NAME)
                        .values("username", "password", "username@mail.com", "first", "last")
                        .execute();
                jooq.insertInto(Tables.ISSUE, Tables.ISSUE.AUTHOR_ID, Tables.ISSUE.TITLE, Tables.ISSUE.BROWSER_INFO, Tables.ISSUE.SCREENSHOT, Tables.ISSUE.PAGE_URL, Tables.ISSUE.DESCRIPTION, Tables.ISSUE.PRIORITY)
                        .values(1L, "title", "browser_info", "screenshot", "page_url", "description", IssuePriority.NORMAL)
                        .execute();
            });


            AfterEach(() -> {
                jooq.truncate(Tables.USER).restartIdentity().cascade().execute();
                jooq.truncate(Tables.ISSUE).restartIdentity().cascade().execute();
                jooq.truncate(Tables.ISSUE_COMMENT).restartIdentity().cascade().execute();
            });


            /*
             * Find One
             */
            Context("findOne <id: Long>", () -> {
                JustBeforeEach(() -> {
                    try {
                        retSingle = IssueCommentRepository.findOne(1L);
                    } catch (Exception exception) {
                        retException = exception;
                    }
                });

                Context("when a valid IssueComment entity exists", () -> {
                    BeforeEach(() -> {
                        jooq.insertInto(Tables.ISSUE_COMMENT, Tables.ISSUE_COMMENT.AUTHOR_ID, Tables.ISSUE_COMMENT.ISSUE_ID, Tables.ISSUE_COMMENT.BODY)
                                .values(1L, 1L, "body")
                                .execute();
                    });

                    It("should return a valid IssueComment entity", () -> {
                        assertThat(retSingle, notNullValue());
                        assertThat(retSingle.getId(), is(1L));
                        assertThat(retSingle.getAuthorId(), is(1L));
                        assertThat(retSingle.getIssueId(), is(1L));
                        assertThat(retSingle.getBody(), is("body"));
                    });
                });

                Context("when a valid IssueComment entity does not exist", () -> {
                    It("should throw a NullPointerException", () -> {
                        assertThat(retException, notNullValue());
                        assertThat(retException, instanceOf(NullPointerException.class));
                    });
                });
            });


            /*
             * Save
             */
            Context("save <entity: IssueComment>", () -> {
                Context("when a valid IssueComment entity is provided", () -> {
                    Context("when the provided ID does not exist", () -> {
                        JustBeforeEach(() -> {
                            try {
                                retSingle = IssueCommentRepository.save(new IssueComment().setAuthorId(1L).setIssueId(1L).setBody("body"));
                            } catch (Exception exception) {
                                retException = exception;
                            }
                        });

                        It("should create a new IssueComment entity and return it", () -> {
                            assertThat(retSingle, notNullValue());
                            assertThat(retSingle.getId(), is(1L));
                            assertThat(retSingle.getAuthorId(), is(1L));
                            assertThat(retSingle.getIssueId(), is(1L));
                            assertThat(retSingle.getBody(), is("body"));
                        });
                    });

                    Context("when the provided ID exists", () -> {
                        BeforeEach(() -> {
                            jooq.insertInto(Tables.ISSUE_COMMENT, Tables.ISSUE_COMMENT.AUTHOR_ID, Tables.ISSUE_COMMENT.ISSUE_ID, Tables.ISSUE_COMMENT.BODY)
                                    .values(1L, 1L, "body")
                                    .execute();
                        });

                        JustBeforeEach(() -> {
                            try {
                                retSingle = IssueCommentRepository.save(new IssueComment().setId(1L).setAuthorId(1L).setIssueId(1L).setBody("alternative_body"));
                            } catch (Exception exception) {
                                retException = exception;
                            }
                        });

                        It("should update the existing IssueComment entity and return it", () -> {
                            assertThat(retSingle, notNullValue());
                            assertThat(retSingle.getId(), is(1L));
                            assertThat(retSingle.getAuthorId(), is(1L));
                            assertThat(retSingle.getIssueId(), is(1L));
                            assertThat(retSingle.getBody(), is("alternative_body"));
                        });
                    });
                });

                Context("when an invalid IssueComment entity is provided", () -> {
                    Context("when the provided ID does not exist", () -> {
                        JustBeforeEach(() -> {
                            try {
                                retSingle = IssueCommentRepository.save(new IssueComment().setIssueId(1L).setBody("body"));
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
                            jooq.insertInto(Tables.ISSUE_COMMENT, Tables.ISSUE_COMMENT.AUTHOR_ID, Tables.ISSUE_COMMENT.ISSUE_ID, Tables.ISSUE_COMMENT.BODY)
                                    .values(1L, 1L, "body")
                                    .execute();
                        });

                        JustBeforeEach(() -> {
                            try {
                                retSingle = IssueCommentRepository.save(new IssueComment().setId(1L).setAuthorId(1L).setIssueId(1L).setBody(null));
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
             * Find By Noteable
             */
            Context("findByNoteable <noteableId: Long>", () -> {
                JustBeforeEach(() -> {
                    try {
                        retCollection = IssueCommentRepository.findByNoteable(1L);
                    } catch (Exception exception) {
                        retException = exception;
                    }
                });

                Context("when a valid IssueComment entity exists", () -> {
                    BeforeEach(() -> {
                        jooq.insertInto(Tables.ISSUE_COMMENT, Tables.ISSUE_COMMENT.AUTHOR_ID, Tables.ISSUE_COMMENT.ISSUE_ID, Tables.ISSUE_COMMENT.BODY)
                                .values(1L, 1L, "body")
                                .execute();
                    });

                    It("should return a Collection with 1 element", () -> {
                        assertThat(retCollection, notNullValue());
                        assertThat(retCollection.size(), is(1));
                    });
                });

                Context("when a valid IssueComment entity does not exist", () -> {
                    It("should return an empty Collection", () -> {
                        assertThat(retCollection, notNullValue());
                        assertThat(retCollection.size(), is(0));
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
