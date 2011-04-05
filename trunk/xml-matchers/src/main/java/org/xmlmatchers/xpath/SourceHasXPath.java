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

/**
 * @author David Ehringer
 * 
 */
public class SourceHasXPath extends HasXPath<Source> {

	public SourceHasXPath(String xPathExpression) {
		super(xPathExpression);
	}

	public SourceHasXPath(String xPathExpression, Matcher<? super String> valueMatcher,
			NamespaceContext namespaceContext) {
		super(xPathExpression, valueMatcher, namespaceContext);
	}

	public <T> SourceHasXPath(String xPathExpression,
			Matcher<? super T> valueMatcher, NamespaceContext namespaceContext,
			XpathReturnType<? super T> xpathReturnType) {
		super(xPathExpression, valueMatcher, namespaceContext, xpathReturnType);
	}

	@Override
	protected Source convertToSource(Source source)
			throws IllegalArgumentException {
		return source;
	}

	@Factory
	public static Matcher<Source> hasXPath(String xPath) {
		return new SourceHasXPath(xPath);
	}

	@Factory
	public static Matcher<Source> hasXPath(String xPath,
			NamespaceContext namespaceContext) {
		return new SourceHasXPath(xPath, null, namespaceContext);
	}

	@Factory
	public static Matcher<Source> hasXPath(String xPath,
			Matcher<? super String> valueMatcher) {
		return new SourceHasXPath(xPath, valueMatcher, null);
	}

	@Factory
	public static Matcher<Source> hasXPath(String xPath,
			NamespaceContext namespaceContext, Matcher<? super String> valueMatcher) {
		return new SourceHasXPath(xPath, valueMatcher, namespaceContext);
	}

	@Factory
	public static <T> Matcher<Source> hasXPath(String xPath,
			XpathReturnType<? super T> xpathReturnType,
			Matcher<? super T> valueMatcher) {
		return new SourceHasXPath(xPath, valueMatcher, null, xpathReturnType);
	}

	@Factory
	public static <T> Matcher<Source> hasXPath(String xPath,
			NamespaceContext namespaceContext,
			XpathReturnType<? super T> xpathReturnType,
			Matcher<? super T> valueMatcher) {
		return new SourceHasXPath(xPath, valueMatcher, namespaceContext,
				xpathReturnType);
	}

	@Factory
	public static Matcher<Source> hasXPath(String xPath,
			Matcher<? super String> valueMatcher, NamespaceContext namespaceContext) {
		return new SourceHasXPath(xPath, valueMatcher, namespaceContext);
	}

}
