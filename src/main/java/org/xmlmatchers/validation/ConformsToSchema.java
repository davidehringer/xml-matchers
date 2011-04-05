/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xmlmatchers.validation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * possible names: adheres, complies, conforms
 * 
 * @author David Ehringer
 */
public abstract class ConformsToSchema<T> extends
		TypeSafeDiagnosingMatcher<T> {

	private Schema schema;

	public ConformsToSchema(Schema schema) {
		this.schema = schema;
	}

	public void describeTo(Description description) {
		description.appendText("an XML document valid as per the given schema");
	}

	@Override
	protected boolean matchesSafely(T t, Description mismatchDescription) {
		boolean isValid = true;
		DOMSource source = convert(t);
		Validator validator = schema.newValidator();
		ValidationErrorHandler errorCollector = new ValidationErrorHandler();
		validator.setErrorHandler(errorCollector);
		try {
			validator.validate(source);
		} catch (SAXException e) {
			isValid = false;
		} catch (IOException e) {
			isValid = false;
		}
		if (errorCollector.hasErrors()) {
			errorCollector.updateDiscription(mismatchDescription);
			isValid = false;
		}
		return isValid;
	}

	protected abstract DOMSource convert(T t);

	private static class ValidationErrorHandler implements ErrorHandler {

		private List<String> errorMessages = new ArrayList<String>();

		public void error(SAXParseException exception) throws SAXException {
			errorMessages.add(toMessage(exception));
		}

		public void fatalError(SAXParseException exception) throws SAXException {
			errorMessages.add(toMessage(exception));
		}

		public void warning(SAXParseException exception) throws SAXException {
			// we don't care about warnings at the moment
		}

		private String toMessage(SAXParseException exception) {
			StringBuilder message = new StringBuilder();
			message.append(exception.getMessage());
			message.append(" (line: ");
			message.append(exception.getLineNumber());
			message.append(" , column: ");
			message.append(exception.getColumnNumber());
			message.append(")");
			return message.toString();
		}

		public boolean hasErrors() {
			return !errorMessages.isEmpty();
		}

		public void updateDiscription(Description mismatchDescription) {
			if (!hasErrors()) {
				return;
			}
			mismatchDescription.appendText("has validation errors [");
			Iterator<String> messages = errorMessages.iterator();
			while (messages.hasNext()) {
				String message = messages.next();
				mismatchDescription.appendText(message);
				if (messages.hasNext()) {
					mismatchDescription.appendText(", ");
				}
			}
			mismatchDescription.appendText("]");
		}
	}
}
