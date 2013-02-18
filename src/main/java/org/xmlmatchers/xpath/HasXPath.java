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
import javax.xml.xpath.XPathFactoryConfigurationException;

import net.sf.saxon.lib.NamespaceConstant;

import org.hamcrest.Description;
import org.hamcrest.Factory;
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
public class HasXPath extends TypeSafeMatcher<Source> {
	// TODO change to extending TypeSafeDiagnosingMatcher

	private static final IdentityTransformer IDENTITY = new IdentityTransformer();

	private final String xPathString;
	private final XPathExpression compiledXPath;
	private final XpathReturnType<?> xPathReturnType;

	private final Matcher<?> valueMatcher;

	protected HasXPath(String xPathExpression) {
		this(xPathExpression, null, null);
	}

	protected HasXPath(String xPathExpression, Matcher<? super String> valueMatcher,
			NamespaceContext namespaceContext) {
		this(xPathExpression, valueMatcher, namespaceContext, returningAString());
	}

 // This works when using the Eclipse compiler but now with with Sun's.  Not sure why
//	public <R> HasXPath(String xPathExpression, Matcher<? super R> valueMatcher,
//			NamespaceContext namespaceContext, XpathReturnType<? super R> xPathReturnType) {

	public HasXPath(String xPathExpression, Matcher<?> valueMatcher,
			NamespaceContext namespaceContext, XpathReturnType<?> xPathReturnType) {
		try {
			XPath xPath = buildXPath();
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

	private XPath buildXPath() {
		System.setProperty("javax.xml.xpath.XPathFactory:"
				+ NamespaceConstant.OBJECT_MODEL_SAXON,
				"net.sf.saxon.xpath.XPathFactoryImpl");
		try {
			return XPathFactory.newInstance(NamespaceConstant.OBJECT_MODEL_SAXON).newXPath();
		} catch (XPathFactoryConfigurationException e) {
			throw new UnsupportedOperationException(
					"Saxon is incorrectly configured or not available on the classpath",
					e);
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
	public boolean matchesSafely(Source source) {
		try {
			Node node = convert(source);
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

	private Node convert(Source source) {
		DOMResult dom = new DOMResult();
		IDENTITY.transform(source, dom);
		return dom.getNode();
	}

	private boolean evaluateXPathForExistence(Node node)
			throws TransformerException, XPathExpressionException {
		return compiledXPath.evaluate(node, XPathConstants.NODE) == null ? false
				: true;
	}
	
	/**
	 * @see {@link XpathReturnType#returningAnXmlNode()} about implementation details.
	 */
	private Object evaluateXPath(Node node) throws TransformerException,
			XPathExpressionException {
		if (XPathConstants.NODE == xPathReturnType.evaluationMode()) {
			// We need a special case for XML results so that we actually get back the XML in a format useful for chaining other matchers
			Node result = (Node) compiledXPath.evaluate(node,
					XPathConstants.NODE);
			return new DOMSource(result);
		}
		return compiledXPath.evaluate(node, xPathReturnType.evaluationMode());
	}

	@Factory
	public static Matcher<Source> hasXPath(String xPath) {
		return new HasXPath(xPath);
	}

	@Factory
	public static Matcher<Source> hasXPath(String xPath,
			NamespaceContext namespaceContext) {
		return new HasXPath(xPath, null, namespaceContext);
	}

	@Factory
	public static Matcher<Source> hasXPath(String xPath,
			Matcher<? super String> valueMatcher) {
		return new HasXPath(xPath, valueMatcher, null);
	}

	@Factory
	public static Matcher<Source> hasXPath(String xPath,
			NamespaceContext namespaceContext, Matcher<? super String> valueMatcher) {
		return new HasXPath(xPath, valueMatcher, namespaceContext);
	}

	@Factory
	public static <T> Matcher<Source> hasXPath(String xPath,
			XpathReturnType<? super T> xpathReturnType,
			Matcher<? super T> valueMatcher) {
		return new HasXPath(xPath, valueMatcher, null, xpathReturnType);
	}

	@Factory
	public static <T> Matcher<Source> hasXPath(String xPath,
			NamespaceContext namespaceContext,
			XpathReturnType<? super T> xpathReturnType,
			Matcher<? super T> valueMatcher) {
		return new HasXPath(xPath, valueMatcher, namespaceContext,
				xpathReturnType);
	}

	@Factory
	public static Matcher<Source> hasXPath(String xPath,
			Matcher<? super String> valueMatcher, NamespaceContext namespaceContext) {
		return new HasXPath(xPath, valueMatcher, namespaceContext);
	}
}
