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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.xmlmatchers.equivalence.IsEquivalentTo.equivalentTo;
import static org.xmlmatchers.equivalence.IsEquivalentTo.isEquivalentTo;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * @author David Ehringer
 * 
 */
public class StringIsEquivalentToTest {

	@Test
	public void stringReflexiveEquivalence() {
		String xml = "<mountains><mountain>K2</mountain></mountains>";
		assertThat(xml, isEquivalentTo(xml));
	}

	@Test
	public void nodeReflexiveEquivalence() throws SAXException, IOException,
			ParserConfigurationException {
		String xml = "<mountains><mountain>K2</mountain></mountains>";
		Element element = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder()
				.parse(new ByteArrayInputStream(xml.getBytes()))
				.getDocumentElement();
		assertThat(xml, isEquivalentTo(element));
	}

	@Test
	public void elementTextDiffers() {
		String k2Xml = "<mountains><mountain>K2</mountain></mountains>";
		String everestXml = "<mountains><mountain>Everest</mountain></mountains>";
		assertThat(k2Xml, is(not(equivalentTo(everestXml))));
	}
	
	@Test
	public void xmlWithWhiteSpaceBetweenElements() {
		String xml = "<mountains><mountain>K2</mountain></mountains>";
		String xmlWithWhiteSpace = "<mountains>   <mountain>K2</mountain>\n\t</mountains>";
		assertThat(xml, isEquivalentTo(xmlWithWhiteSpace));
	}
	
	@Test
	public void xmlWithWhiteSpaceBeforeAndAfterText() {
		String xml = "<mountains><mountain>K2</mountain></mountains>";
		String xmlWithSpaceWrappingText = "<mountains><mountain>\n\tK2\n\t </mountain></mountains>";
		assertThat(xml, isEquivalentTo(xmlWithSpaceWrappingText));
	}
	
	@Test
	public void xmlWithComments() {
		String xml = "<mountains><mountain>K2</mountain></mountains>";
		String xmlWithComments = "<mountains><!-- 28,251 ft. --><mountain>K2</mountain></mountains>";
		assertThat(xml, isEquivalentTo(xmlWithComments));
	}
	
	@Test
	public void emptyNodes() {
		String xml = "<mountains></mountains>";
		String singleElement = "<mountains />";
		assertThat(xml, isEquivalentTo(singleElement));
	}
	
	@Test
	public void attributesCanBeInADifferentOrder() {
		String xml = "<mountains><mountain height=\"28,251\" firstClimbed=\"1954\">K2</mountain></mountains>";
		String xmlWithComments = "<mountains><mountain firstClimbed=\"1954\" height=\"28,251\">K2</mountain></mountains>";;
		assertThat(xml, isEquivalentTo(xmlWithComments));
	}
	
	@Test
	public void attributesValuesNotEqual() {
		String xml = "<mountains><mountain height=\"28,251\" firstClimbed=\"1954\">K2</mountain></mountains>";
		String wrongDate = "<mountains><mountain height=\"28,251\" firstClimbed=\"1953\">K2</mountain></mountains>";;
		assertThat(xml, is(not(equivalentTo(wrongDate))));
	}

	@Test
	public void differentAttributes() {
		String xml = "<mountains><mountain height=\"28,251\" firstClimbed=\"1954\">K2</mountain></mountains>";
		String alternative = "<mountains><mountain range=\"Baltoro Karakoram\" firstClimbed=\"1954\">K2</mountain></mountains>";;
		assertThat(xml, is(not(equivalentTo(alternative))));
	}
	
	@Test
	public void textAndCdataAreEquivalent() {
		String xml = "<mountains><mountain>K2</mountain></mountains>";
		String xmlWithCdata = "<mountains><mountain><![CDATA[K2]]></mountain></mountains>";
		assertThat(xml, isEquivalentTo(xmlWithCdata));
	}
	
	@Test
	public void ifTheTextInACdataSectionIsNotEqualToTheRegularTextTheXmlIsNotEquivalent() {
		String xml = "<mountains><mountain>K2</mountain></mountains>";
		String xmlWithCdata = "<mountains><mountain><![CDATA[Everest]]></mountain></mountains>";
		assertThat(xml, is(not(isEquivalentTo(xmlWithCdata))));
	}

	@Test
	public void textInTheCdataSectionCanContainWhitespace() {
		String xml = "<mountains><mountain><![CDATA[K2]]></mountain></mountains>";
		String xmlWithCdata = "<mountains><mountain><![CDATA[K2\t]]></mountain></mountains>";
		assertThat(xml, isEquivalentTo(xmlWithCdata));
	}
}
