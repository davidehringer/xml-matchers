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
package org.xmlmatchers.equivalence;

import java.io.IOException;

import javax.xml.transform.Source;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.xml.sax.SAXException;
import org.xmlmatchers.transform.IdentityTransformer;
import org.xmlmatchers.transform.StringResult;

/**
 * <p>
 * Matchers for comparing XML for "equivalence" and "similarity." Two documents
 * are considered to be "equivalent" if they contain the same elements in the
 * same order. Also, namespace prefixes must be the same. Attribute order,
 * comments, whitespace, CDATA vs. text usage is not considered.
 * <p>
 * </p>
 * Two documents are considered to be "similar" if the the content of the nodes
 * in the documents are the same, but minor differences exist e.g. sequencing of
 * sibling elements, values of namespace prefixes, use of implied attribute
 * values.
 * </p>
 * 
 * @author David Ehringer
 */
public class IsEquivalentTo extends TypeSafeMatcher<Source> {
	// TODO change to extending TypeSafeDiagnosingMatcher

	private final IdentityTransformer identity = new IdentityTransformer();

	private final String control;
	private final boolean onlySimilar;

	private IsEquivalentTo(Source control) {
		this(control, false);
	}

	private IsEquivalentTo(Source control, boolean onlySimilar) {
		this.control = convertToString(control);
		this.onlySimilar = onlySimilar;
	}

	@Override
	public boolean matchesSafely(Source source) {
		String test = convertToString(source);
		XMLUnit.setIgnoreAttributeOrder(true);
		XMLUnit.setIgnoreComments(true);
		XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true);
		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setNormalize(true);
		XMLUnit.setNormalizeWhitespace(true);
		try {
			if (onlySimilar) {
				return new Diff(control, test).similar();
			}
			return new Diff(control, test).identical();
		} catch (SAXException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}

	private String convertToString(Source source) {
		StringResult result = new StringResult();
		identity.transform(source, result);
		return result.toString();
	}

	public void describeTo(Description description) {
		description.appendText("an XML document equivalent to " + control);
	}

	@Factory
	public static Matcher<Source> isEquivalentTo(Source control) {
		return new IsEquivalentTo(control);
	}

	@Factory
	public static Matcher<Source> equivalentTo(Source control) {
		return new IsEquivalentTo(control);
	}

	@Factory
	public static Matcher<Source> isSimilarTo(Source control) {
		return new IsEquivalentTo(control, true);
	}

	@Factory
	public static Matcher<Source> similarTo(Source control) {
		return new IsEquivalentTo(control, true);
	}
}
