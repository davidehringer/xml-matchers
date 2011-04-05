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
import javax.xml.transform.dom.DOMSource;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xmlmatchers.transform.IdentityTransformer;
import org.xmlmatchers.transform.StringResult;

/**
 * @author David Ehringer
 */
public class IsEquivalentTo extends TypeSafeMatcher<String> {
	
	// TODO support other types besides String?
	
	private String xml;

	private IsEquivalentTo(StringResult xml) {
		this.xml = xml.toString();
	}

	private IsEquivalentTo(String xml) {
		this.xml = xml;
	}

	@Override
	public boolean matchesSafely(String source) {
		try {
			XMLUnit.setIgnoreAttributeOrder(true);
			XMLUnit.setIgnoreComments(true);
			XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true);
			XMLUnit.setIgnoreWhitespace(true);
			XMLUnit.setNormalize(true);
			XMLUnit.setNormalizeWhitespace(true);
			return new Diff(source, xml).identical();
		} catch (IOException e) {
			return false;
		} catch (SAXException e) {
			return false;
		}
	}

	public void describeTo(Description description) {
		description.appendText("an XML document equivalent to "
				+ xml.toString());
	}

	@Factory
	public static Matcher<String> isEquivalentTo(StringResult xml) {
		return new IsEquivalentTo(xml);
	}

	@Factory
	public static Matcher<String> isEquivalentTo(String xml) {
		return new IsEquivalentTo(xml);
	}

	@Factory
	public static Matcher<String> isEquivalentTo(Source xml) {
		StringResult result = new StringResult();
		IdentityTransformer transformer = new IdentityTransformer();
		transformer.transform(xml, result);
		return new IsEquivalentTo(result.toString());
	}

	@Factory
	public static Matcher<String> isEquivalentTo(Node xml) {
		DOMSource source = new DOMSource(xml);
		StringResult result = new StringResult();
		IdentityTransformer transformer = new IdentityTransformer();
		transformer.transform(source, result);
		return new IsEquivalentTo(result.toString());
	}	

	@Factory
	public static Matcher<String> equivalentTo(StringResult xml) {
		return isEquivalentTo(xml);
	}

	@Factory
	public static Matcher<String> equivalentTo(String xml) {
		return isEquivalentTo(xml);
	}
	
	@Factory
	public static Matcher<String> equivalentTo(Source xml) {
		return isEquivalentTo(xml);
	}

	@Factory
	public static Matcher<String> equivalentTo(Node xml) {
		return isEquivalentTo(xml);
	}
}
