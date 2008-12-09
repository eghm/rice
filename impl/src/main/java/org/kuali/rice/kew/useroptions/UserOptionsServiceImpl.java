/*
 * Copyright 2005-2007 The Kuali Foundation.
 *
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.kew.useroptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.kuali.rice.kew.useroptions.dao.UserOptionsDAO;
import org.kuali.rice.kew.util.KEWConstants;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public class UserOptionsServiceImpl implements UserOptionsService {

    private UserOptionsDAO userOptionsDAO;

    private static final Properties defaultProperties = new Properties();

    static {
        defaultProperties.setProperty(KEWConstants.EMAIL_RMNDR_KEY, KEWConstants.EMAIL_RMNDR_WEEK_VAL);
    }

    private Long getNewOptionIdForActionList() {
		return getUserOptionsDAO().getNewOptionIdForActionList();
	}

    public List<UserOptions> findByUserQualified(String principalId, String likeString) {
        if ((principalId == null)) {
            return new ArrayList<UserOptions>(0);
        }
        return this.getUserOptionsDAO().findByUserQualified(principalId, likeString);
    }

    public UserOptions findByOptionId(String optionId, String principalId) {
        if (optionId == null || "".equals(optionId) || principalId == null || "".equals(principalId)) {
            return null;
        }
        return this.getUserOptionsDAO().findByOptionId(optionId, principalId);
    }

    public Collection<UserOptions> findByOptionValue(String optionId, String optionValue){
        return this.getUserOptionsDAO().findByOptionValue(optionId, optionValue);
    }

    public Collection<UserOptions> findByWorkflowUser(String principalId) {
        return this.getUserOptionsDAO().findByWorkflowUser(principalId);
    }

    public void save(UserOptions userOptions) {
        this.getUserOptionsDAO().save(userOptions);
    }

    public void deleteUserOptions(UserOptions userOptions) {
        this.getUserOptionsDAO().deleteUserOptions(userOptions);
    }

    public void save(String principalId, String optionId, String optionValue) {
        UserOptions option = findByOptionId(optionId, principalId);
        if (option == null) {
            option = new UserOptions();
            option.setWorkflowId(principalId);
        }
        option.setOptionId(optionId);
        option.setOptionVal(optionValue);
        getUserOptionsDAO().save(option);
    }

    public boolean refreshActionList(String principalId) {
        List<UserOptions> options = findByUserQualified(principalId, KEWConstants.RELOAD_ACTION_LIST + "%");
        boolean refresh = ! options.isEmpty();
        if (refresh && principalId != null && !"".equals(principalId)) {
            getUserOptionsDAO().deleteByUserQualified(principalId, KEWConstants.RELOAD_ACTION_LIST + "%");
        }
        return refresh;
    }

    public void saveRefreshUserOption(String principalId) {
        save(principalId, KEWConstants.RELOAD_ACTION_LIST + new Date().getTime() + getNewOptionIdForActionList(), "true");
    }

    public UserOptionsDAO getUserOptionsDAO() {
        return userOptionsDAO;
    }

    public void setUserOptionsDAO(UserOptionsDAO optionsDAO) {
        userOptionsDAO = optionsDAO;
    }


}