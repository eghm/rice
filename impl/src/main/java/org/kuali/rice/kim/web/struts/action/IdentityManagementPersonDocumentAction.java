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
package org.kuali.rice.kim.web.struts.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.kuali.rice.core.util.RiceConstants;
import org.kuali.rice.kim.bo.entity.KimPrincipal;
import org.kuali.rice.kim.bo.entity.impl.KimEntityImpl;
import org.kuali.rice.kim.bo.group.KimGroup;
import org.kuali.rice.kim.bo.types.impl.KimTypeAttributeImpl;
import org.kuali.rice.kim.bo.ui.PersonDocumentAddress;
import org.kuali.rice.kim.bo.ui.PersonDocumentAffiliation;
import org.kuali.rice.kim.bo.ui.PersonDocumentCitizenship;
import org.kuali.rice.kim.bo.ui.PersonDocumentEmail;
import org.kuali.rice.kim.bo.ui.PersonDocumentEmploymentInfo;
import org.kuali.rice.kim.bo.ui.PersonDocumentGroup;
import org.kuali.rice.kim.bo.ui.PersonDocumentName;
import org.kuali.rice.kim.bo.ui.PersonDocumentPhone;
import org.kuali.rice.kim.bo.ui.PersonDocumentRole;
import org.kuali.rice.kim.bo.ui.PersonDocumentRolePrncpl;
import org.kuali.rice.kim.bo.ui.PersonDocumentRoleQualifier;
import org.kuali.rice.kim.document.IdentityManagementPersonDocument;
import org.kuali.rice.kim.service.KIMServiceLocator;
import org.kuali.rice.kim.service.support.KimTypeService;
import org.kuali.rice.kim.service.support.impl.KimTypeServiceBase;
import org.kuali.rice.kim.web.struts.form.IdentityManagementPersonDocumentForm;
import org.kuali.rice.kns.util.KNSConstants;
import org.kuali.rice.kns.web.struts.action.KualiDocumentActionBase;

/**
 * This is a description of what this class does - shyu don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (kuali-rice@googlegroups.com)
 *
 */
public class IdentityManagementPersonDocumentAction extends KualiDocumentActionBase {

	
    @Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ActionForward forward =  super.execute(mapping, form, request, response);
		// move the following to service
		// get set up person document
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        IdentityManagementPersonDocument personDoc = (IdentityManagementPersonDocument)personDocumentForm.getDocument();
		if (StringUtils.isNotBlank(request.getParameter("command")) && request.getParameter("command").equals("initiate") && StringUtils.isNotBlank(request.getParameter("principalId"))) {
	        KimPrincipal principal = KIMServiceLocator.getIdentityManagementService().getPrincipal(request.getParameter("principalId"));
	        personDoc.setPrincipalId(principal.getPrincipalId());
	        personDoc.setPrincipalName(principal.getPrincipalName());
	        personDoc.setPassword(principal.getPassword());
			KimEntityImpl entity = (KimEntityImpl)KIMServiceLocator.getIdentityManagementService().getEntity(principal.getEntityId());
			KIMServiceLocator.getUiDocumentService().loadEntityToPersonDoc(personDoc, entity);
			List<? extends KimGroup> groups = KIMServiceLocator.getIdentityManagementService().getGroupsForPrincipal(principal.getPrincipalId());
			KIMServiceLocator.getUiDocumentService().loadGroupToPersonDoc(personDoc, groups);
		}
		if (StringUtils.isNotBlank(request.getParameter("command")) && request.getParameter("command").equals("displayDocSearchView")) {
			for (PersonDocumentRole role : personDoc.getRoles()) {
		        KimTypeService kimTypeService = (KimTypeServiceBase)KIMServiceLocator.getService(role.getKimRoleType().getKimTypeServiceName());
				role.setDefinitions(kimTypeService.getAttributeDefinitions(role.getKimRoleType()));
				// TODO : refactor qualifier key to connect between defn & qualifier
	        	for (PersonDocumentRolePrncpl principal : role.getRolePrncpls()) {
	        		for (PersonDocumentRoleQualifier qualifier : principal.getQualifiers()) {
	    		        for (KimTypeAttributeImpl attrDef : role.getKimRoleType().getAttributeDefinitions()) {
	    		        	if (qualifier.getKimAttrDefnId().equals(attrDef.getKimAttributeId())) {
	    		        		qualifier.setQualifierKey(attrDef.getSortCode());
	    		        	}
	    		        }
	        			
	        		}
	        	}
	        	// when post again, it will need this during populate
	            role.setNewRolePrncpl(new PersonDocumentRolePrncpl());
	            for (String key : role.getDefinitions().keySet()) {
	            	PersonDocumentRoleQualifier qualifier = new PersonDocumentRoleQualifier();
	            	qualifier.setQualifierKey(key);
	            	role.getNewRolePrncpl().getQualifiers().add(qualifier);
	            }

		        KIMServiceLocator.getUiDocumentService().setAttributeEntry(role);

			}
		}

		return forward;
    }
    
	public ActionForward addAffln(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        PersonDocumentAffiliation newAffln = personDocumentForm.getNewAffln();
        // handling radio for dflt - not a final soln yet
//        if ((personDocumentForm.getAfflnDflt() != null && personDocumentForm.getAfflnDflt().equals(new Integer(-1))) || personDocumentForm.getPersonDocument().getAffiliations().isEmpty()) {
//        	newAffln.setDflt(true);
//        	personDocumentForm.setAfflnDflt(personDocumentForm.getPersonDocument().getAffiliations().size());
//        }
        newAffln.setDocumentNumber(personDocumentForm.getPersonDocument().getDocumentNumber());
        personDocumentForm.getPersonDocument().getAffiliations().add(newAffln);
        personDocumentForm.setNewAffln(new PersonDocumentAffiliation());
        
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }
    public ActionForward deleteAffln(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        personDocumentForm.getPersonDocument().getAffiliations().remove(getLineToDelete(request));

        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }
    public ActionForward addCitizenship(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        PersonDocumentCitizenship newCitizenship = personDocumentForm.getNewCitizenship();
        personDocumentForm.getPersonDocument().getCitizenships().add(newCitizenship);
        personDocumentForm.setNewCitizenship(new PersonDocumentCitizenship());
        
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }
    public ActionForward deleteCitizenship(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        personDocumentForm.getPersonDocument().getCitizenships().remove(getLineToDelete(request));

        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

    public ActionForward addEmpInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        IdentityManagementPersonDocument personDOc = personDocumentForm.getPersonDocument();
        PersonDocumentAffiliation affiliation = personDOc.getAffiliations().get(getSelectedLine(request));
        
        PersonDocumentEmploymentInfo newempInfo = affiliation.getNewEmpInfo();
        newempInfo.setDocumentNumber(personDOc.getDocumentNumber());
        newempInfo.setVersionNumber(new Long(1));
        affiliation.getEmpInfos().add(newempInfo);
        affiliation.setNewEmpInfo(new PersonDocumentEmploymentInfo());
        
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }
    public ActionForward deleteEmpInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        String selectedIndexes = getSelectedParentChildIdx(request);
        if (selectedIndexes != null) {
	        String [] indexes = StringUtils.split(selectedIndexes,":");
	        PersonDocumentAffiliation affiliation = personDocumentForm.getPersonDocument().getAffiliations().get(Integer.parseInt(indexes[0]));
	        affiliation.getEmpInfos().remove(Integer.parseInt(indexes[1]));
        }
        // TODO : error msg if not found ?
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }
    
    private String getSelectedParentChildIdx(HttpServletRequest request) {
    	String lineNumber = null;
        String parameterName = (String) request.getAttribute(KNSConstants.METHOD_TO_CALL_ATTRIBUTE);
        if (StringUtils.isNotBlank(parameterName)) {
            lineNumber = StringUtils.substringBetween(parameterName, ".line", ".");
        }

        return lineNumber;
    }

    public ActionForward addName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        PersonDocumentName newName = personDocumentForm.getNewName();
        newName.setDocumentNumber(personDocumentForm.getDocument().getDocumentNumber());
        personDocumentForm.getPersonDocument().getNames().add(newName);
        personDocumentForm.setNewName(new PersonDocumentName());
        
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }
    public ActionForward deleteName(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        personDocumentForm.getPersonDocument().getNames().remove(getLineToDelete(request));

        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

    public ActionForward addAddress(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        PersonDocumentAddress newAddress = personDocumentForm.getNewAddress();
        newAddress.setDocumentNumber(personDocumentForm.getDocument().getDocumentNumber());
        personDocumentForm.getPersonDocument().getAddrs().add(newAddress);
        personDocumentForm.setNewAddress(new PersonDocumentAddress());
        
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }
    public ActionForward deleteAddress(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        personDocumentForm.getPersonDocument().getAddrs().remove(getLineToDelete(request));

        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

    public ActionForward addPhone(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        PersonDocumentPhone newPhone = personDocumentForm.getNewPhone();
        newPhone.setDocumentNumber(personDocumentForm.getDocument().getDocumentNumber());
        personDocumentForm.getPersonDocument().getPhones().add(newPhone);
        personDocumentForm.setNewPhone(new PersonDocumentPhone());
        
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }
    public ActionForward deletePhone(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        personDocumentForm.getPersonDocument().getPhones().remove(getLineToDelete(request));

        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

    public ActionForward addEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        PersonDocumentEmail newEmail = personDocumentForm.getNewEmail();
        newEmail.setDocumentNumber(personDocumentForm.getDocument().getDocumentNumber());
        personDocumentForm.getPersonDocument().getEmails().add(newEmail);
        personDocumentForm.setNewEmail(new PersonDocumentEmail());
        
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }
    public ActionForward deleteEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        personDocumentForm.getPersonDocument().getEmails().remove(getLineToDelete(request));

        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

    public ActionForward addGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        PersonDocumentGroup newGroup = personDocumentForm.getNewGroup();
        personDocumentForm.getPersonDocument().getGroups().add(newGroup);
        personDocumentForm.setNewGroup(new PersonDocumentGroup());
        
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }
    public ActionForward deleteGroup(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        personDocumentForm.getPersonDocument().getGroups().remove(getLineToDelete(request));

        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

    public ActionForward addRole(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        PersonDocumentRole newRole = personDocumentForm.getNewRole();
        KimTypeService kimTypeService = (KimTypeServiceBase)KIMServiceLocator.getService(newRole.getKimRoleType().getKimTypeServiceName());
        //AttributeDefinitionMap definitions = kimTypeService.getAttributeDefinitions();
        // role type populated from form is not a complete record
        newRole.getKimRoleType().setKimTypeId(newRole.getKimTypeId());
        newRole.getKimRoleType().refreshReferenceObject("attributeDefinitions");
        newRole.setDefinitions(kimTypeService.getAttributeDefinitions(newRole.getKimRoleType()));
        PersonDocumentRolePrncpl newRolePrncpl = new PersonDocumentRolePrncpl();
        
        for (String key : newRole.getDefinitions().keySet()) {
        	PersonDocumentRoleQualifier qualifier = new PersonDocumentRoleQualifier();
        	qualifier.setQualifierKey(key);
        	newRolePrncpl.getQualifiers().add(qualifier);
        }
        newRole.setNewRolePrncpl(newRolePrncpl);
        KIMServiceLocator.getUiDocumentService().setAttributeEntry(newRole);
        personDocumentForm.getPersonDocument().getRoles().add(newRole);
        personDocumentForm.setNewRole(new PersonDocumentRole());
        
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }
    
    public ActionForward deleteRole(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        personDocumentForm.getPersonDocument().getRoles().remove(getLineToDelete(request));

        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

    public ActionForward addRoleQualifier(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        IdentityManagementPersonDocument personDOc = personDocumentForm.getPersonDocument();
        PersonDocumentRole role = personDOc.getRoles().get(getSelectedLine(request));
        PersonDocumentRolePrncpl newRolePrncpl = role.getNewRolePrncpl();
        role.getRolePrncpls().add(newRolePrncpl);
        role.setNewRolePrncpl(new PersonDocumentRolePrncpl());
        for (String key : role.getDefinitions().keySet()) {
        	PersonDocumentRoleQualifier qualifier = new PersonDocumentRoleQualifier();
        	qualifier.setQualifierKey(key);
        	role.getNewRolePrncpl().getQualifiers().add(qualifier);
        }

        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }

    public ActionForward deleteRoleQualifier(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        String selectedIndexes = getSelectedParentChildIdx(request);
        if (selectedIndexes != null) {
	        String [] indexes = StringUtils.split(selectedIndexes,":");
	        PersonDocumentRole role = personDocumentForm.getPersonDocument().getRoles().get(Integer.parseInt(indexes[0]));
	        role.getRolePrncpls().remove(Integer.parseInt(indexes[1]));
        }
        // TODO : error msg if not found ?
        return mapping.findForward(RiceConstants.MAPPING_BASIC);
    }
	@Override
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

        IdentityManagementPersonDocumentForm personDocumentForm = (IdentityManagementPersonDocumentForm) form;
        IdentityManagementPersonDocument personDoc = personDocumentForm.getPersonDocument();
		if (StringUtils.isBlank(personDoc.getPrivacy().getDocumentNumber())) {
			personDoc.getPrivacy().setDocumentNumber(personDoc.getDocumentNumber());
		}
		// TODO : refactor this, also probably move to service ?
		for (PersonDocumentRole role : personDoc.getRoles()) {
			for(PersonDocumentRolePrncpl rolePrncpl : role.getRolePrncpls()) {
				rolePrncpl.setDocumentNumber(personDoc.getDocumentNumber());
				for (PersonDocumentRoleQualifier qualifier : rolePrncpl.getQualifiers()) {
					qualifier.setDocumentNumber(personDoc.getDocumentNumber());	
					qualifier.setKimTypId(role.getKimTypeId());
					// TO TO : need rework to set attributedefid
					for (KimTypeAttributeImpl attr : role.getKimRoleType().getAttributeDefinitions()) {
						if (attr.getSortCode().equals(qualifier.getQualifierKey())) {
							qualifier.setKimAttrDefnId(attr.getKimAttributeId());
						}
					}
				}
			}
		}
		return super.save(mapping, form, request, response);
	}

}
