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

import static javax.xml.xpath.XPathConstants.BOOLEAN;
import static javax.xml.xpath.XPathConstants.NODE;
import static javax.xml.xpath.XPathConstants.NUMBER;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.xmlmatchers.equivalence.IsEquivalentTo.equivalentTo;
import static org.xmlmatchers.transform.StringSource.toSource;
import static org.xmlmatchers.xpath.SourceHasXPath.hasXPath;

import javax.xml.namespace.NamespaceContext;
import javax.xml.transform.Source;

import org.junit.Ignore;
import org.junit.Test;
import org.xmlmatchers.namespace.SimpleNamespaceContext;

/**
 * @author David Ehringer
 * 
 */
public class SourceHasXPathTest {

	// This is the XML from org.hamcrest.xml.HasXPathTest
	private Source xml = toSource(""
			+ "<root type='food'>\n"
			+ "  <something id='a'><cheese>Edam</cheese></something>\n"
			+ "  <something id='b'><cheese>Cheddar</cheese></something>\n"
			+ "  <f:foreignSomething xmlns:f=\"http://cheese.com\" milk=\"camel\">Caravane</f:foreignSomething>\n"
			+ "  <emptySomething />\n"
			+ "  <f:emptySomething xmlns:f=\"http://cheese.com\" f:calories=\"0\" />"
			+ "  <somethingElse calories=\"2\" good=\"false\" bad=\"0\" />"
			+ "</root>\n");

	private NamespaceContext usingNamespaces = new SimpleNamespaceContext()
			.withBinding("c", "http://cheese.com");

	@Test
	public void matchesNodesInTheXmlWhenNoNamespacesAreBound() {
		assertThat(xml, hasXPath("/root"));
		assertThat(xml, hasXPath("/root/something"));
		assertThat(xml, hasXPath("/root/something[2]/cheese"));
		assertThat(xml, hasXPath("/root/emptySomething"));
		assertThat(xml, not(hasXPath("/root/foreignSomething"))); // Move to its
																	// own test
	}

	@Test
	public void matchesFunctions() {
		assertThat(xml, hasXPath("count(/root/something)", equalTo("2")));
	}

	@Test
	public void matchesNodesInTheXmlWhenNamespacesAreBound() {
		assertThat(xml, hasXPath("/root", usingNamespaces));
		assertThat(xml, hasXPath("/root/something", usingNamespaces));
		assertThat(xml, hasXPath("/root/something[2]/cheese", usingNamespaces));
		assertThat(xml, hasXPath("/root/emptySomething", usingNamespaces));
		assertThat(xml, not(hasXPath("/root/foreignSomething")));
		assertThat(xml, hasXPath("/root/c:foreignSomething", usingNamespaces));
	}

	@Test
	public void matchesAttributesInTheXmlWhenNoNamespacesAreBound() {
		assertThat(xml, hasXPath("/root/@type"));
	}

	@Test
	public void matchesAttributesInTheXmlWhenNamespacesAreBound() {
		assertThat(xml,
				hasXPath("/root/c:emptySomething/@c:calories", usingNamespaces));
	}

	@Test
	public void theResultOfTheXpathCanMatchedUsingEquivalentToWhenTheResultIsAnXmlFragment() {
		assertThat(
				xml,
				hasXPath(NODE, "/root/something[@id='a']/cheese",
						equivalentTo("<cheese>Edam</cheese>")));
	}

	@Test
	public void theResultOfTheXpathCanMatchedUsingEquivalentToWhenTheResultIsAString() {
		assertThat(
				xml,
				hasXPath("/root/c:emptySomething/@c:calories", usingNamespaces,
						equalTo("0")));
	}

	@Test
	public void theResultOfTheXpathCanMatchedUsingEquivalentToWhenTheResultIsANumber() {
		assertThat(xml,
				hasXPath(NUMBER, "/root/somethingElse/@calories", equalTo(2.0)));
		assertThat(
				xml,
				hasXPath(NUMBER, "/root/somethingElse/@calories", closeTo(4, 2)));
	}

	@Test
	@Ignore("not sure if this is a good idea")
	public void theResultOfTheXpathCanMatchedUsingEquivalentToWhenTheResultIsABoolean() {
		assertThat(xml,
				hasXPath(BOOLEAN, "/root/somethingElse/@good", equalTo(false)));
	}

	@Test
	public void theResultOfTheXpathCanMatchedUsingStringContains() {
		assertThat(
				xml,
				hasXPath("/root/something[@id='a']/cheese",
						containsString("Edam")));
	}

	@Test
	public void matchingNodesCanBeTestedForEquivalence() {
		assertThat(
				xml,
				hasXPath(NODE, "/root/something[@id='a']/cheese",
						equivalentTo("<cheese><!-- some comment -->Edam</cheese>")));
	}
}
