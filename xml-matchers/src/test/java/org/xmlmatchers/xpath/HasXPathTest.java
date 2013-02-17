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

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.xmlmatchers.transform.XmlConverters.the;
import static org.xmlmatchers.xpath.HasXPath.hasXPath;
import static org.xmlmatchers.xpath.XpathReturnType.returningABoolean;
import static org.xmlmatchers.xpath.XpathReturnType.returningANumber;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Collection;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xmlmatchers.namespace.SimpleNamespaceContext;
import org.xmlmatchers.transform.IdentityTransformer;
import org.xmlmatchers.transform.StringResult;
import org.xmlmatchers.transform.StringSource;

/**
 * @author David Ehringer
 */
@RunWith(Parameterized.class)
public class HasXPathTest {

	private static final String EXAMPLE_XML = "<mountains type='big'>\n"
			+ "  <mountain id='a' altname=''><name>Everest</name></mountain>\n"
			+ "  <mountain id='b'><name>K2</name></mountain>\n"
			+ "  <f:range xmlns:f=\"http://mountains.com\" milk=\"camel\">Caravane</f:range>\n"
			+ "  <oceanRidge />\n"
			+ "  <f:oceanRidge xmlns:f=\"http://mountains.com\" f:depth='-5000' />"
			+ "  <volcanoe eruptions='2' good='false' bad='0' />"
			+ "</mountains>\n";

	private Source xml;

	private NamespaceContext usingNamespaces = new SimpleNamespaceContext()
			.withBinding("m", "http://mountains.com")//
			.withBinding("r", "http://rivers.com");

	public HasXPathTest(Source xml) {
		this.xml = xml;
	}

	// Test multiple source representations of XML
	@Parameters
	public static Collection<Source[]> data() throws Exception {
		return Arrays.asList(new Source[][] {//
				{ the(EXAMPLE_XML) },//
						{ the(nodeVersionOf(EXAMPLE_XML)) },//
						{ the(stringResultVersionOf(EXAMPLE_XML)) },//
				});
	}

	private static StringResult stringResultVersionOf(String exampleXml) {
		IdentityTransformer identity = new IdentityTransformer();
		StringResult result = new StringResult();
		Source source = StringSource.toSource(exampleXml);
		identity.transform(source, result);
		return result;
	}

	private static Node nodeVersionOf(String exampleXml) throws Exception {
		Element element = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder()
				.parse(new ByteArrayInputStream(exampleXml.getBytes()))
				.getDocumentElement();
		return element;
	}

	@Test
	public void matchesNodesInTheXmlWhenNoNamespacesAreBound() {
		assertThat(xml, hasXPath("/mountains"));
		assertThat(xml, hasXPath("/mountains/mountain"));
		assertThat(xml, hasXPath("/mountains/mountain[2]/name"));
		assertThat(xml, hasXPath("/mountains/oceanRidge"));
		assertThat(xml, hasXPath("/mountains/mountain[@id='a']/@altname"));
	}

	@Test
	public void ifANodeIsPrefixedByANamespaceAndNamespacesAreNotBoundXpathWillNotFindIt() {
		assertThat(xml, not(hasXPath("/mountains/range")));
	}

	@Test
	public void functionsCanBeUsedToReturnDataThatCanBeMatchedAgainst() {
		assertThat(xml, hasXPath("count(/mountains/mountain)", equalTo("2")));
		assertThat(xml,//
				hasXPath("count(/mountains/mountain)", //
						returningANumber(), //
						greaterThanOrEqualTo(2d)));
		assertThat(xml,//
				hasXPath("count(/mountains/mountain)", //
						returningANumber(), //
						not(lessThanOrEqualTo(1d))));
	}

	@Test
	public void matchesNodesInTheXmlWhenNamespacesAreBound() {
		assertThat(xml, hasXPath("/mountains", usingNamespaces));
		assertThat(xml, hasXPath("/mountains/mountain", usingNamespaces));
		assertThat(xml,
				hasXPath("/mountains/mountain[2]/name", usingNamespaces));
		assertThat(xml, hasXPath("/mountains/oceanRidge", usingNamespaces));
		assertThat(xml, not(hasXPath("/mountains/range")));
		assertThat(xml, hasXPath("/mountains/m:range", usingNamespaces));
	}

	@Test
	public void matchesAttributesInTheXmlWhenNoNamespacesAreBound() {
		assertThat(xml, hasXPath("/mountains/@type"));
	}

	@Test
	public void matchesAttributesInTheXmlWhenNamespacesAreBound() {
		assertThat(xml,
				hasXPath("/mountains/m:oceanRidge/@m:depth", usingNamespaces));
	}

	@Test
	@Ignore("Crap. Using a Source breaks this use case")
	public void theResultOfTheXpathCanMatchedUsingEquivalentToWhenTheResultIsAnXmlFragment() {
//		assertThat(
//				xml,
//				hasXPath("/mountains/mountain[@id='a']/name",
//						returningAnXmlNode(),
//						equivalentTo("<name>Everest</name>")));
	}

	@Test
	@Ignore("Crap. Using a Source breaks this use case")
	public void matchingNodesCanBeTestedForEquivalence() {
//		assertThat(
//				xml,
//				hasXPath(
//						"/mountains/mountain[@id='a']/name",
//						returningAnXmlNode(),
//						equivalentTo("<name><!-- some comment -->Everest</name>")));
	}

	@Test
	public void theResultOfTheXpathCanMatchedUsingEquivalentToWhenTheResultIsAString() {
		assertThat(
				xml,
				hasXPath("/mountains/m:oceanRidge/@m:depth", usingNamespaces,
						equalTo("-5000")));
		assertThat(xml,
				hasXPath("/mountains/mountain[@id='a']/@altname", equalTo("")));
		assertThat(
				xml,
				hasXPath("/mountains/mountain[@id='a']/name",
						equalToIgnoringCase("EVEREST")));
		assertThat(
				xml,
				hasXPath("/mountains/mountain[@id='a']/name/text()",
						equalToIgnoringCase("EvErEsT")));
	}

	@Test
	public void theResultOfTheXpathCanMatchedUsingEquivalentToWhenTheResultIsANumber() {
		assertThat(
				xml,
				hasXPath("/mountains/volcanoe/@eruptions", returningANumber(),
						equalTo(2.0)));
		assertThat(
				xml,
				hasXPath("/mountains/volcanoe/@eruptions", returningANumber(),
						closeTo(4, 2)));
		assertThat(
				xml,
				hasXPath("/mountains/volcanoe/@eruptions", returningANumber(),
						lessThan(5d)));
		assertThat(
				xml,
				hasXPath("/mountains/volcanoe/@eruptions", returningANumber(),
						lessThanOrEqualTo(5.0)));
		assertThat(
				xml,
				hasXPath("count(/mountains/mountain)", returningANumber(),
						equalTo(2d)));
	}

	@Test
	public void attributeOrNodeValuesCannotBeConvertedToBooleans() {
		assertThat(xml, //
				not(hasXPath("/mountains/volcanoe/@good", //
						returningABoolean(),//
						equalTo(false))));
	}

	@Test
	public void theResultOfTheXpathCanMatchedUsingEquivalentToWhenTheResultIsABoolean() {
		assertThat(xml,//
				hasXPath("/mountains/volcanoe/@good", //
						returningABoolean(),//
						equalTo(true)));
		assertThat(xml,//
				hasXPath("not(/mountains/climber)", //
						returningABoolean(),//
						equalTo(true)));
		assertThat(xml,//
				hasXPath("not(/mountains/volcanoe/@good)", //
						returningABoolean(),//
						equalTo(false)));
	}

	@Test
	public void theResultOfTheXpathCanMatchedUsingStringContains() {
		assertThat(
				xml,
				hasXPath("/mountains/mountain[@id='a']/name",
						containsString("Everest")));
	}
}
