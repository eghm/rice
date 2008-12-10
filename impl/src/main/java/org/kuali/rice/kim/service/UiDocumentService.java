/*
 * Copyright 2007 The Kuali Foundation
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
package org.kuali.rice.kim.service;

import java.util.List;

import org.kuali.rice.kim.bo.entity.impl.KimEntityImpl;
import org.kuali.rice.kim.bo.group.KimGroup;
import org.kuali.rice.kim.bo.group.impl.KimGroupImpl;
import org.kuali.rice.kim.bo.ui.PersonDocumentRole;
import org.kuali.rice.kim.document.IdentityManagementPersonDocument;

/**
 * This is a description of what this class does - shyu don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (kuali-rice@googlegroups.com)
 *
 */
public interface UiDocumentService {
	/**
	 * 
	 * This method to populate Entity tables from person document pending tables when it is approved.
	 * 	  
	 * @param identityManagementPersonDocument
	 */
    public void saveEntityPerson(IdentityManagementPersonDocument identityManagementPersonDocument);
    
    /**
     * 
     * This method is to set up the DD attribute entry map for role qualifiers, so it can be rendered.
     * 
     * @param personDocRole
     */
	public void setAttributeEntry(PersonDocumentRole personDocRole);
	
	/**
	 * 
	 * This method is to load entity to person document pending Bos when user 'initiate' a document for 'editing' entity.
	 * 
	 * @param identityManagementPersonDocument
	 * @param kimEntity
	 */
	public void loadEntityToPersonDoc(IdentityManagementPersonDocument identityManagementPersonDocument, KimEntityImpl kimEntity);
	
	/**
	 * 
	 * This method load related group data to pending document when usert initiate the 'edit'.
	 * 
	 * @param identityManagementPersonDocument
	 * @param groups
	 */
	public void loadGroupToPersonDoc(IdentityManagementPersonDocument identityManagementPersonDocument, List<? extends KimGroup> groups);
	
	/**
	 * 
	 * This method is to get groups for this principal.  It return a list of 'KimGroupImpl'
	 * IdentityManagementService has a similar method, but it return a list of DTO (GroupInfo)
	 * which does not have 'groupMembers' data. 
	 * 
	 * @param principalId
	 * @return
	 */
	public List <KimGroupImpl> getGroupsForPrincipal(String principalId);

}
