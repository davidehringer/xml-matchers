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
package org.xmlmatchers.transform;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Document;

/**
 * A convenience class for creating {@link Source} objects from a String. 
 * 
 * @author David Ehringer
 * @see DOMSource
 */
public class StringSource extends DOMSource {
	
	private final String xml;
	
	private StringSource(String xml) throws RuntimeException {
		this.xml = xml;
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
					.newInstance();
			documentBuilderFactory.setNamespaceAware(true);
			DocumentBuilder documentBuilder = documentBuilderFactory
					.newDocumentBuilder();
			Document dom = documentBuilder.parse(new ByteArrayInputStream(xml
					.getBytes()));
			setNode(dom);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static DOMSource toSource(String xml) throws RuntimeException {
		return new StringSource(xml);
	}

	@Override
	public String toString() {
		return xml;
	}
}
