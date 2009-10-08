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
package mocks;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import org.kuali.rice.kew.actionrequest.service.impl.DocumentRequeuerImpl;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * a DocumentRequeuerImpl extension that keeps track of the ids for docs that were requeued
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public class MockDocumentRequeuerImpl extends DocumentRequeuerImpl {

	private static final Set<Long> requeuedDocumentIds = new HashSet<Long>();
	private static Lock lock;
	
	public static Set<Long> getRequeuedDocumentIds() {
		return Collections.unmodifiableSet(requeuedDocumentIds);
	}
	
	public static void clearRequeuedDocumentIds() {
		requeuedDocumentIds.clear();
	}

	/**
	 * @see org.kuali.rice.kew.actionrequest.service.impl.DocumentRequeuerImpl#requeueDocument(java.lang.Long)
	 */
	@Override
	public void requeueDocument(Long documentId) {
		requeuedDocumentIds.add(documentId);
		super.requeueDocument(documentId);
	}
}
