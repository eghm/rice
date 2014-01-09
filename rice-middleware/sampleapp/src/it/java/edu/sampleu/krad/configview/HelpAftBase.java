/**
 * Copyright 2005-2014 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.sampleu.krad.configview;

import org.kuali.rice.testtools.common.JiraAwareFailable;
import org.kuali.rice.testtools.selenium.WebDriverLegacyITBase;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class HelpAftBase extends WebDriverLegacyITBase {

    /**
     * /kr-krad/configuration-test-view-uif-controller?viewId=ConfigurationTestView&methodToCall=start&pageId=ConfigurationTestView-Help-Page
     */
    public static final String BOOKMARK_URL = "/kr-krad/configuration-test-view-uif-controller?viewId=ConfigurationTestView&methodToCall=start&pageId=ConfigurationTestView-Help-Page";
   
    @Override
    protected String getBookmarkUrl() {
        return BOOKMARK_URL;
    }

    protected void navigation() throws Exception {
        waitAndClickKRAD();
        waitAndClickByXpath(CONFIGURATION_VIEW_XPATH);
        switchToWindow(CONFIGURATION_VIEW_WINDOW_TITLE);
        waitAndClickByLinkText("Help");
        Thread.sleep(5000);
        selectFrameIframePortlet();
    }

    protected void testHelpNav(JiraAwareFailable failable) throws Exception {
        navigation();
        testViewHelp();
        navigation();
        testPageHelp();
        navigation();
        testTooltipHelp();
        navigation();
        testDisplayOnlyTooltipHelp();
        navigation();
        testMissingTooltipHelp();
        passed();
    }

    protected void testHelpBookmark(JiraAwareFailable failable) throws Exception {
        testViewHelp();
        testPageHelp();
        testTooltipHelp();
        testDisplayOnlyTooltipHelp();
        testMissingTooltipHelp();
        passed();
    }
}
