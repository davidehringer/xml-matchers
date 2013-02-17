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
import static org.xmlmatchers.equivalence.IsEquivalentTo.isSimilarTo;
import static org.xmlmatchers.transform.XmlConverters.*;

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
		assertThat(the(xml), isEquivalentTo(the(xml)));
	}

	@Test
	public void nodeReflexiveEquivalence() throws SAXException, IOException,
			ParserConfigurationException {
		String xml = "<mountains><mountain>K2</mountain></mountains>";
		Element element = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder()
				.parse(new ByteArrayInputStream(xml.getBytes()))
				.getDocumentElement();
		assertThat(the(xml), isEquivalentTo(the(element)));
	}

	@Test
	public void elementTextDiffers() {
		String k2Xml = "<mountains><mountain>K2</mountain></mountains>";
		String everestXml = "<mountains><mountain>Everest</mountain></mountains>";
		assertThat(the(k2Xml), is(not(equivalentTo(the(everestXml)))));
	}
	
	@Test
	public void xmlWithWhiteSpaceBetweenElements() {
		String xml = "<mountains><mountain>K2</mountain></mountains>";
		String xmlWithWhiteSpace = "<mountains>   <mountain>K2</mountain>\n\t</mountains>";
		assertThat(the(xml), isEquivalentTo(the(xmlWithWhiteSpace)));
	}
	
	@Test
	public void xmlWithWhiteSpaceBeforeAndAfterText() {
		String xml = "<mountains><mountain>K2</mountain></mountains>";
		String xmlWithSpaceWrappingText = "<mountains><mountain>\n\tK2\n\t </mountain></mountains>";
		assertThat(the(xml), isEquivalentTo(the(xmlWithSpaceWrappingText)));
	}
	
	@Test
	public void xmlWithComments() {
		String xml = "<mountains><mountain>K2</mountain></mountains>";
		String xmlWithComments = "<mountains><!-- 28,251 ft. --><mountain>K2</mountain></mountains>";
		assertThat(the(xml), isEquivalentTo(the(xmlWithComments)));
	}
	
	@Test
	public void emptyNodes() {
		String xml = "<mountains></mountains>";
		String singleElement = "<mountains />";
		assertThat(the(xml), isEquivalentTo(the(singleElement)));
	}
	
	@Test
	public void attributesCanBeInADifferentOrder() {
		String xml = "<mountains><mountain height=\"28,251\" firstClimbed=\"1954\">K2</mountain></mountains>";
		String xmlWithComments = "<mountains><mountain firstClimbed=\"1954\" height=\"28,251\">K2</mountain></mountains>";;
		assertThat(the(xml), isEquivalentTo(the(xmlWithComments)));
	}
	
	@Test
	public void attributesValuesNotEqual() {
		String xml = "<mountains><mountain height=\"28,251\" firstClimbed=\"1954\">K2</mountain></mountains>";
		String wrongDate = "<mountains><mountain height=\"28,251\" firstClimbed=\"1953\">K2</mountain></mountains>";;
		assertThat(the(xml), is(not(equivalentTo(the(wrongDate)))));
	}

	@Test
	public void differentAttributes() {
		String xml = "<mountains><mountain height=\"28,251\" firstClimbed=\"1954\">K2</mountain></mountains>";
		String alternative = "<mountains><mountain range=\"Baltoro Karakoram\" firstClimbed=\"1954\">K2</mountain></mountains>";;
		assertThat(the(xml), is(not(equivalentTo(the(alternative)))));
	}
	
	@Test
	public void textAndCdataAreEquivalent() {
		String xml = "<mountains><mountain>K2</mountain></mountains>";
		String xmlWithCdata = "<mountains><mountain><![CDATA[K2]]></mountain></mountains>";
		assertThat(the(xml), isEquivalentTo(the(xmlWithCdata)));
	}
	
	@Test
	public void ifTheTextInACdataSectionIsNotEqualToTheRegularTextTheXmlIsNotEquivalent() {
		String xml = "<mountains><mountain>K2</mountain></mountains>";
		String xmlWithCdata = "<mountains><mountain><![CDATA[Everest]]></mountain></mountains>";
		assertThat(the(xml), is(not(isEquivalentTo(the(xmlWithCdata)))));
	}

	@Test
	public void textInTheCdataSectionCanContainWhitespace() {
		String xml = "<mountains><mountain><![CDATA[K2]]></mountain></mountains>";
		String xmlWithCdata = "<mountains><mountain><![CDATA[K2\t]]></mountain></mountains>";
		assertThat(the(xml), isEquivalentTo(the(xmlWithCdata)));
	}
	
    @Test
    // http://code.google.com/p/xml-matchers/issues/detail?id=7 submitted by headcrashing
    public final void shallIgnoreOrder() {
        final String expXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><D:multistatus xmlns:D=\"DAV:\"><D:response><D:href>%s</D:href><D:propstat><D:prop><D:displayname>Teams</D:displayname></D:prop><D:status>HTTP/1.1 200 OK</D:status></D:propstat></D:response><D:response><D:href>http://localhost:9998/quipsy/teams/123</D:href><D:propstat><D:prop><D:displayname/><D:getcontenttype>application/quipsy-team+xml</D:getcontenttype></D:prop><D:status>HTTP/1.1 200 OK</D:status></D:propstat></D:response></D:multistatus>";
        final String actXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><D:multistatus xmlns:D=\"DAV:\"><D:response><D:href>%s</D:href><D:propstat><D:prop><D:displayname>Teams</D:displayname></D:prop><D:status>HTTP/1.1 200 OK</D:status></D:propstat></D:response><D:response><D:href>http://localhost:9998/quipsy/teams/123</D:href><D:propstat><D:prop><D:getcontenttype>application/quipsy-team+xml</D:getcontenttype><D:displayname/></D:prop><D:status>HTTP/1.1 200 OK</D:status></D:propstat></D:response></D:multistatus>";
        assertThat(the(actXml), isSimilarTo(the(expXml)));
    }

    @Test
 // http://code.google.com/p/xml-matchers/issues/detail?id=7 submitted by headcrashing
    public final void shallIgnoreCaseOfPrefix() {
        final String expXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><D:multistatus xmlns:D=\"DAV:\"><D:response><D:href>%s</D:href><D:propstat><D:prop><D:displayname>Teams</D:displayname></D:prop><D:status>HTTP/1.1 200 OK</D:status></D:propstat></D:response><D:response><D:href>http://localhost:9998/quipsy/teams/123</D:href><D:propstat><D:prop><D:displayname/><D:getcontenttype>application/quipsy-team+xml</D:getcontenttype></D:prop><D:status>HTTP/1.1 200 OK</D:status></D:propstat></D:response></D:multistatus>";
        final String actXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><d:multistatus xmlns:d=\"DAV:\"><d:response><d:href>%s</d:href><d:propstat><d:prop><d:displayname>Teams</d:displayname></d:prop><d:status>HTTP/1.1 200 OK</d:status></d:propstat></d:response><d:response><d:href>http://localhost:9998/quipsy/teams/123</d:href><d:propstat><d:prop><d:displayname/><d:getcontenttype>application/quipsy-team+xml</d:getcontenttype></d:prop><d:status>HTTP/1.1 200 OK</d:status></d:propstat></d:response></d:multistatus>";
        assertThat(the(actXml), isSimilarTo(the(expXml)));
    }
    
    @Test
 // http://code.google.com/p/xml-matchers/issues/detail?id=7 submitted by headcrashing
    public final void shallIgnoreNameOfPrefix() {
        final String expXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><D:multistatus xmlns:D=\"DAV:\"><D:response><D:href>%s</D:href><D:propstat><D:prop><D:displayname>Teams</D:displayname></D:prop><D:status>HTTP/1.1 200 OK</D:status></D:propstat></D:response><D:response><D:href>http://localhost:9998/quipsy/teams/123</D:href><D:propstat><D:prop><D:displayname/><D:getcontenttype>application/quipsy-team+xml</D:getcontenttype></D:prop><D:status>HTTP/1.1 200 OK</D:status></D:propstat></D:response></D:multistatus>";
        final String actXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><P:multistatus xmlns:P=\"DAV:\"><P:response><P:href>%s</P:href><P:propstat><P:prop><P:displayname>Teams</P:displayname></P:prop><P:status>HTTP/1.1 200 OK</P:status></P:propstat></P:response><P:response><P:href>http://localhost:9998/quipsy/teams/123</P:href><P:propstat><P:prop><P:displayname/><P:getcontenttype>application/quipsy-team+xml</P:getcontenttype></P:prop><P:status>HTTP/1.1 200 OK</P:status></P:propstat></P:response></P:multistatus>";
        assertThat(the(actXml), isSimilarTo(the(expXml)));
    }

    @Test
 // http://code.google.com/p/xml-matchers/issues/detail?id=7 submitted by headcrashing
    public final void shallIgnoreDefaultPrefix() {
        final String expXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><D:multistatus xmlns:D=\"DAV:\"><D:response><D:href>%s</D:href><D:propstat><D:prop><D:displayname>Teams</D:displayname></D:prop><D:status>HTTP/1.1 200 OK</D:status></D:propstat></D:response><D:response><D:href>http://localhost:9998/quipsy/teams/123</D:href><D:propstat><D:prop><D:displayname/><D:getcontenttype>application/quipsy-team+xml</D:getcontenttype></D:prop><D:status>HTTP/1.1 200 OK</D:status></D:propstat></D:response></D:multistatus>";
        final String actXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><multistatus xmlns=\"DAV:\"><response><href>%s</href><propstat><prop><displayname>Teams</displayname></prop><status>HTTP/1.1 200 OK</status></propstat></response><response><href>http://localhost:9998/quipsy/teams/123</href><propstat><prop><displayname/><getcontenttype>application/quipsy-team+xml</getcontenttype></prop><status>HTTP/1.1 200 OK</status></propstat></response></multistatus>";
        assertThat(the(actXml), isSimilarTo(the(expXml)));
    }
}
