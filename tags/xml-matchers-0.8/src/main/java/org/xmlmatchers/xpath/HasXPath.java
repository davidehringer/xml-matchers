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

import static org.xmlmatchers.xpath.XpathReturnType.returningAString;

import javax.xml.namespace.NamespaceContext;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.w3c.dom.Node;
import org.xmlmatchers.transform.IdentityTransformer;
import org.xmlmatchers.transform.StringResult;

/**
 * 
 * 
 * @author David Ehringer
 * @param <T>
 *            The type to execute the XPath against
 */
public abstract class HasXPath<T> extends TypeSafeMatcher<T> {

	private static final IdentityTransformer IDENTITY = new IdentityTransformer();

	private final String xPathString;
	private final XPathExpression compiledXPath;	
	private final XpathReturnType<?>  xPathReturnType;

	private final Matcher<?> valueMatcher;

	protected HasXPath(String xPathExpression) {
		this(xPathExpression, null, null);
	}

	protected HasXPath(String xPathExpression, Matcher<? super String> valueMatcher,
			NamespaceContext namespaceContext) {
		this(xPathExpression, valueMatcher, namespaceContext, returningAString());
	}
	
	public <R> HasXPath(String xPathExpression, Matcher<? super R> valueMatcher,
			NamespaceContext namespaceContext, XpathReturnType<? super R> xPathReturnType) {
		try {
			XPath xPath = XPathFactory.newInstance().newXPath();
			if (namespaceContext != null) {
				xPath.setNamespaceContext(namespaceContext);
			}
			compiledXPath = xPath.compile(xPathExpression);
			this.xPathString = xPathExpression;
			this.valueMatcher = valueMatcher;
		} catch (XPathExpressionException e) {
			throw new IllegalArgumentException("Invalid XPath : "
					+ xPathExpression, e);
		}
		if (xPathReturnType == null) {
			this.xPathReturnType = returningAString();
		} else {
			this.xPathReturnType = xPathReturnType;
		}
	}

	public void describeTo(Description description) {
		description.appendText("an XML document with XPath " + xPathString);
		if (valueMatcher != null) {
			description.appendText(" matching ");
			valueMatcher.describeTo(description);
		}
	}

	@Override
	public boolean matchesSafely(T t) {
		try {
			Node node = convert(t);
			if (valueMatcher == null) {
				return evaluateXPathForExistence(node);
			}
			Object xPathResult = evaluateXPath(node);
			return valueMatcher.matches(xPathResult);
		} catch (XPathExpressionException e) {
			return false;
		} catch (TransformerException e) {
			return false;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	private Node convert(T t) {
		Source source = convertToSource(t);
		DOMResult dom = new DOMResult();
		IDENTITY.transform(source, dom);
		return dom.getNode();
	}

	protected abstract Source convertToSource(T t)
			throws IllegalArgumentException;

	private boolean evaluateXPathForExistence(Node node)
			throws TransformerException, XPathExpressionException {
		return compiledXPath.evaluate(node, XPathConstants.NODE) == null ? false
				: true;
	}

	private Object evaluateXPath(Node node) throws TransformerException,
			XPathExpressionException {
		if (XPathConstants.NODE == xPathReturnType.evaluationMode()) {
			// We need a special case for XML results so that we actually get back the XML
			Node result = (Node) compiledXPath.evaluate(node,
					XPathConstants.NODE);
			DOMSource domSource = new DOMSource(result);
			StringResult stringResult = new StringResult();
			IDENTITY.transform(domSource, stringResult);
			return stringResult.toString();
		}
		return compiledXPath.evaluate(node, xPathReturnType.evaluationMode());
	}
}
