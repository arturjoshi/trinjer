package com.arturjoshi;

import com.arturjoshi.ticket.TicketPriority;
import com.arturjoshi.ticket.TicketStatus;

/**
 * Created by ajoshi on 31-Jan-17.
 */
public interface TestConst {
    String X_AUTH_TOKEN_HEADER = "X-Auth-Token";
    String TMP_SUFFIX = "_tmp";

    String ACCOUNT_PASSWORD = "testpassword";
    String ACCOUNT_USERNAME = "testusername";
    String ACCOUNT_EMAIL = "testemail";

    String PROJECT_NAME = "testprojectname";
    String PROJECT_NAME_UPDATED = "updatedprojectname";
    boolean VISIBLE_PROJECT = true;
    boolean NON_VISIBLE_PROJECT = false;

    String SPRINT_DESCRIPTION = "testdescription";

    String STORY_DESCRIPTION = "storydescription";
    String STORY_SUMMARY = "storysummary";
    String STORY_ACCEPTANCE_CRITERIA = "storyacceptancecriteria";
    TicketPriority STORY_PRIORITY = TicketPriority.CRITICAL;
    TicketStatus STORY_STATUS = TicketStatus.TODO;
    Integer STORY_ESTIMATE = 1;

}
