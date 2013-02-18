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

import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Node;

/**
 * Convenience methods for converting various forms of XML into {@link Source}
 * objects, the representation used by the XML Matcher interfaces. The method
 * names {code}the{code} and {code}xml{code} where chosen to provide a fluent
 * feel when you chain the matchers together. Example usage:
 * 
 * {@code
 * 		assertThat(
				the(xml),
				hasXPath("/mountains/mountain[@id='a']/name",
						returningAnXmlNode(),
						equivalentTo(xml("<name>Everest</name>"))));
 * }
 * 
 * 
 * @author David Ehringer
 */
public class XmlConverters {

	private XmlConverters() {

	}

	public static Source the(Source xml) {
		return xml;
	}

	public static Source the(String xml) {
		return StringSource.toSource(xml);
	}

	public static Source the(final Node node) {
		return new DOMSource(node) {
			@Override
			public String toString() {
				IdentityTransformer transformer = new IdentityTransformer();
				DOMSource source = new DOMSource(node);
				StringResult result = new StringResult();
				transformer.transform(source, result);
				return result.toString();
			}
		};
	}

	public static Source the(StringResult xml) {
		return StringSource.toSource(xml.toString());
	}

	public static Source xml(Source xml) {
		return the(xml);
	}

	public static Source xml(String xml) {
		return the(xml);
	}

	public static Source xml(Node node) {
		return the(node);
	}

	public static Source xml(StringResult xml) {
		return the(xml);
	}
}
