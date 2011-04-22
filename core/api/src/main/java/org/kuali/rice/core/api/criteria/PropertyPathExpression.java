/*
 * Copyright 2011 The Kuali Foundation
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
package org.kuali.rice.core.api.criteria;


/**
 * An expression which contains a property path.  The property path is used
 * to identify what portion of an object model that the expression applies
 * to in the case of a {@link Criteria} construct.
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface PropertyPathExpression extends Expression {
	
	/**
	 * Returns the property path for this expression which represents the
	 * portion of the object model to which the expression applies.
	 * 
	 * @return the property path
	 */
	String getPropertyPath();
		    
}
