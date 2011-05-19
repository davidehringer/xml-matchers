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
 * @author David Ehringer
 */
public class XmlConverters {

	private XmlConverters(){
		
	}

	public static Source the(Source xml) {
		return xml;
	}
	
	public static Source the(String xml) {
		return StringSource.toSource(xml);
	}
	
	public static Source the(Node node) {
		return new DOMSource(node);
	}
	
	public static Source the(StringResult xml) {
		return StringSource.toSource(xml.toString());
	}
}
