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
package org.xmlmatchers.xpath;

import javax.xml.namespace.NamespaceContext;
import javax.xml.transform.Source;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.xmlmatchers.transform.StringResult;
import org.xmlmatchers.transform.StringSource;

/**
 * @author David Ehringer
 */
public class StringResultHasXPath extends HasXPath<StringResult> {
	
	public StringResultHasXPath(String xPathExpression,
			Matcher<String> valueMatcher,
			NamespaceContext namespaceContext) {
		super(xPathExpression, valueMatcher, namespaceContext);
	}

	public StringResultHasXPath(String xPathExpression) {
		super(xPathExpression);
	}

	@Override
	protected Source convertToSource(StringResult result) {
		return StringSource.toSource(result.toString());
	}

	@Factory
	public static Matcher<StringResult> hasXPath(String xPath) {
		return new StringResultHasXPath(xPath);
	}

	@Factory
	public static Matcher<StringResult> hasXPath(String xPath,
			NamespaceContext namespaceContext) {
		return new StringResultHasXPath(xPath, null, namespaceContext);
	}

	@Factory
	public static Matcher<StringResult> hasXPath(String xPath,
			Matcher<String> valueMatcher) {
		return new StringResultHasXPath(xPath, valueMatcher, null);
	}

	@Factory
	public static Matcher<StringResult> hasXPath(String xPath,
			Matcher<String> valueMatcher,
			NamespaceContext namespaceContext) {
		return new StringResultHasXPath(xPath, valueMatcher, namespaceContext);
	}
}
