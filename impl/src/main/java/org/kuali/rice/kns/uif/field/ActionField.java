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
package org.kuali.rice.kns.uif.field;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kns.uif.UifConstants;
import org.kuali.rice.kns.uif.UifParameters;
import org.kuali.rice.kns.uif.UifPropertyPaths;
import org.kuali.rice.kns.uif.container.View;

/**
 * Field that presents an action that can be taken on the UI such as submitting
 * the form or invoking a script
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class ActionField extends FieldBase {
	private static final long serialVersionUID = 1025672792657238829L;

	private String methodToCall;
	private String navigateToPageId;

	private boolean clientSideCall;
	private boolean scriptFormSubmit;

	private String actionLabel;
	private ImageField actionImageField;

	private Map<String, String> actionParameters;

	public ActionField() {
		clientSideCall = false;
		scriptFormSubmit = false;

		actionParameters = new HashMap<String, String>();
	}

	/**
	 * <p>
	 * The following initialization is performed:
	 * 
	 * <ul>
	 * <li>Set the actionLabel if blank to the Field label</li>
	 * </ul>
	 * </p>
	 * 
	 * @see org.kuali.rice.kns.uif.ComponentBase#performInitialization(org.kuali.rice.kns.uif.container.View)
	 */
	@Override
	public void performInitialization(View view) {
		super.performInitialization(view);

		if (StringUtils.isBlank(actionLabel)) {
			actionLabel = this.getLabel();
		}
	}

	/**
	 * <p>
	 * The following finalization is performed:
	 * 
	 * <ul>
	 * <li>Add methodToCall action parameter if set and setup event code for
	 * setting action parameters</li>
	 * </ul>
	 * </p>
	 * 
	 * @see org.kuali.rice.kns.uif.ComponentBase#performFinalize(org.kuali.rice.kns.uif.container.View,
	 *      java.lang.Object)
	 */
	@Override
	public void performFinalize(View view, Object model) {
		super.performFinalize(view, model);

		if (StringUtils.isNotBlank(navigateToPageId)) {
			actionParameters.put(UifParameters.NAVIGATE_TO_PAGE_ID, navigateToPageId);
			if (StringUtils.isBlank(methodToCall)) {
				actionParameters.put(UifConstants.CONTROLLER_METHOD_DISPATCH_PARAMETER_NAME,
						UifConstants.MethodToCallNames.NAVIGATE);
			}
		}

		if (!actionParameters.containsKey(UifConstants.CONTROLLER_METHOD_DISPATCH_PARAMETER_NAME)
				&& StringUtils.isNotBlank(methodToCall) && !clientSideCall) {
			actionParameters.put(UifConstants.CONTROLLER_METHOD_DISPATCH_PARAMETER_NAME, methodToCall);
		}

		if (!actionParameters.isEmpty()) {
			String prefixScript = this.getOnClickScript();
			if (prefixScript == null) {
				prefixScript = "";
			}

			String writeParamsScript = "";
			for (String key : actionParameters.keySet()) {
				String parameterPath = key;
				if (!key.equals(UifConstants.CONTROLLER_METHOD_DISPATCH_PARAMETER_NAME)) {
					parameterPath = UifPropertyPaths.ACTION_PARAMETERS + "[" + key + "]";
				}

				writeParamsScript = writeParamsScript + "writeHiddenToForm('" + parameterPath + "' , '"
						+ actionParameters.get(key) + "'); ";
			}

			String postScript = "";
			if (scriptFormSubmit) {
				postScript = "submitForm();";
			}

			this.setOnClickScript(prefixScript + writeParamsScript + postScript);
		}
	}

	/**
	 * Name of the method that should be called when the action is selected
	 * 
	 * <p>
	 * For a server side call (clientSideCall is false), gives the name of the
	 * method in the mapped controller that should be invoked when the action is
	 * selected. For client side calls gives the name of the script function
	 * that should be invoked when the action is selected
	 * </p>
	 * 
	 * @return String name of method to call
	 */
	public String getMethodToCall() {
		return this.methodToCall;
	}

	/**
	 * Setter for the actions method to call
	 * 
	 * @param methodToCall
	 */
	public void setMethodToCall(String methodToCall) {
		this.methodToCall = methodToCall;
	}

	/**
	 * Indicates whether the action invokes a client side function or a server
	 * side. For server side calls this will typically be a form post
	 * 
	 * @return boolean true if action makes a client side call, false if the
	 *         call is server side
	 */
	public boolean isClientSideCall() {
		return this.clientSideCall;
	}

	/**
	 * Setter for the client side call indicator
	 * 
	 * @param clientSideCall
	 */
	public void setClientSideCall(boolean clientSideCall) {
		this.clientSideCall = clientSideCall;
	}

	/**
	 * Builds up the client side JavaScript code for calling the action's method
	 * 
	 * @return String JavaScript handing code
	 */
	public String getClientSideEventCode() {
		String eventCode = "";

		// TODO: need client side method parameters once el support is in
		if (clientSideCall) {
			eventCode = methodToCall + "();";
		}

		return eventCode;
	}

	/**
	 * Indicates whether the form should be submitted with a JavaScript call
	 * (for instance in cases where we have an action link)
	 * 
	 * @return boolean true if form should be submitted with script, false if
	 *         not (regular input or image submit)
	 */
	public boolean isScriptFormSubmit() {
		return this.scriptFormSubmit;
	}

	/**
	 * Setter for the script submit indicator
	 * 
	 * @param scriptFormSubmit
	 */
	public void setScriptFormSubmit(boolean scriptFormSubmit) {
		this.scriptFormSubmit = scriptFormSubmit;
	}

	/**
	 * Label text for the action
	 * 
	 * <p>
	 * The label text is used by the template renderers to give a human readable
	 * label for the action. For buttons this generally is the button text,
	 * while for an action link it would be the links displayed text
	 * </p>
	 * 
	 * @return String label for action
	 */
	public String getActionLabel() {
		return this.actionLabel;
	}

	/**
	 * Setter for the actions label
	 * 
	 * @param actionLabel
	 */
	public void setActionLabel(String actionLabel) {
		this.actionLabel = actionLabel;
	}

	/**
	 * Image to use for the action
	 * 
	 * <p>
	 * When the action image field is set (and render is true) the image will be
	 * used to present the action as opposed to the default (input submit). For
	 * action link templates the image is used for the link instead of the
	 * action link text
	 * </p>
	 * 
	 * @return ImageField action image
	 */
	public ImageField getActionImageField() {
		return this.actionImageField;
	}

	/**
	 * Setter for the action image field
	 * 
	 * @param actionImageField
	 */
	public void setActionImageField(ImageField actionImageField) {
		this.actionImageField = actionImageField;
	}

	/**
	 * For an <code>ActionField</code> that is part of a
	 * <code>NavigationGroup</code, the navigate to page id can be set to
	 * configure the page that should be navigated to when the action is
	 * selected
	 * 
	 * <p>
	 * Support exists in the <code>UifControllerBase</code> for handling
	 * navigation between pages
	 * </p>
	 * 
	 * @return String id of page that should be rendered when the action item is
	 *         selected
	 */
	public String getNavigateToPageId() {
		return this.navigateToPageId;
	}

	/**
	 * Setter for the navigate to page id
	 * 
	 * @param navigateToPageId
	 */
	public void setNavigateToPageId(String navigateToPageId) {
		this.navigateToPageId = navigateToPageId;
		actionParameters.put(UifParameters.NAVIGATE_TO_PAGE_ID, navigateToPageId);
		this.methodToCall = UifConstants.MethodToCallNames.NAVIGATE;
	}

	/**
	 * Parameters that should be sent when the action is invoked
	 * 
	 * <p>
	 * Action renderer will decide how the parameters are sent for the action
	 * (via script generated hiddens, or script parameters, ...)
	 * </p>
	 * 
	 * <p>
	 * Can be set by other components such as the <code>CollectionGroup</code>
	 * to provide the context the action is in (such as the collection name and
	 * line the action applies to)
	 * </p>
	 * 
	 * @return Map<String, String> action parameters
	 */
	public Map<String, String> getActionParameters() {
		return this.actionParameters;
	}

	/**
	 * Setter for the action parameters
	 * 
	 * @param actionParameters
	 */
	public void setActionParameters(Map<String, String> actionParameters) {
		this.actionParameters = actionParameters;
	}

	/**
	 * Convenience method to add a parameter to the action parameters Map
	 * 
	 * @param parameterName
	 *            - name of parameter to add
	 * @param parameterValue
	 *            - value of parameter to add
	 */
	public void addActionParameter(String parameterName, String parameterValue) {
		if (actionParameters == null) {
			this.actionParameters = new HashMap<String, String>();
		}

		this.actionParameters.put(parameterName, parameterValue);
	}

	/**
	 * @see org.kuali.rice.kns.uif.ComponentBase#getSupportsOnClick()
	 */
	@Override
	public boolean getSupportsOnClick() {
		return true;
	}

}
