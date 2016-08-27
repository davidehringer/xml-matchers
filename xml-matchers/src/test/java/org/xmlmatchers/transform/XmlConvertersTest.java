/*
 * Copyright 2011 the original author or authors.
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
package org.xmlmatchers.transform;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.xmlmatchers.transform.XmlConverters.the;
import static org.xmlmatchers.xpath.XpathReturnType.returningAnXmlNode;
import static org.xmlmatchers.XmlMatchers.*;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;

import org.junit.Test;
import org.w3c.dom.Element;

/**
 * @author David Ehringer
 */
public class XmlConvertersTest {

	private final String xml = "<Lhotse></Lhotse>";

	@Test
	public void theConvertsASourceToASource() {
		Source source = StringSource.toSource(xml);
		assertNotNull(the(source));
	}

	@Test
	public void theConvertsAStringToASource() {
		assertNotNull(the(xml));
	}

	@Test
	public void theConvertsANodeToASource() throws Exception {
		Element element = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder()
				.parse(new ByteArrayInputStream(xml.getBytes()))
				.getDocumentElement();

		assertNotNull(the(element));
	}
	
	@Test
	public void theConvertsANodeToASourceHasAFriendlyToString() throws Exception {
		Element element = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder()
				.parse(new ByteArrayInputStream(xml.getBytes()))
				.getDocumentElement();

		Source source = the(element);
		assertThat(the(source.toString()), isEquivalentTo(the(xml)));
	}
	
	@Test
	public void theConvertsAStringResultToASource() throws Exception {
		IdentityTransformer identity = new IdentityTransformer();
		StringResult result = new StringResult();
		Source source = StringSource.toSource(xml);
		identity.transform(source, result);

		assertNotNull(the(result));
	}
	
	@Test
    public void thatXmlCanBeCreatedFromNonUTF8() throws Exception {
        String xml = new String("<?xml version=\"1.0\"?><this><is><xml>t\u00E9st</xml></is></this>".getBytes("ISO-8859-1"), "ISO-8859-1");
        assertThat(the(xml, "ISO-8859-1"), hasXPath("/this/is/xml", returningAnXmlNode(), isEquivalentTo(the("<xml>tést</xml>"))));
    }
    
}
