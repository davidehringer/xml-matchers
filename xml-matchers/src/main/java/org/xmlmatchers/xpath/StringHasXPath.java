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
import org.xmlmatchers.transform.StringSource;

/**
 * @author David Ehringer
 * 
 */
public class StringHasXPath extends HasXPath<String> {

	public StringHasXPath(String xPathExpression,
			Matcher<? super String> valueMatcher,
			NamespaceContext namespaceContext) {
		super(xPathExpression, valueMatcher, namespaceContext);
	}

	public StringHasXPath(String xPathExpression) {
		super(xPathExpression);
	}

	@Override
	protected Source convertToSource(String t) {
		return StringSource.toSource(t);
	}

	@Factory
	public static Matcher<String> hasXPath(String xPath) {
		return new StringHasXPath(xPath);
	}

	@Factory
	public static Matcher<String> hasXPath(String xPath,
			NamespaceContext namespaceContext) {
		return new StringHasXPath(xPath, null, namespaceContext);
	}

	@Factory
	public static Matcher<String> hasXPath(String xPath,
			Matcher<? super String> valueMatcher) {
		return new StringHasXPath(xPath, valueMatcher, null);
	}

	@Factory
	public static Matcher<String> hasXPath(String xPath,
			Matcher<? super String> valueMatcher,
			NamespaceContext namespaceContext) {
		return new StringHasXPath(xPath, valueMatcher, namespaceContext);
	}
}
