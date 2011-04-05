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
package org.xmlmatchers.namespace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;

/**
 * A simple, mutable {@link NamespaceContext} implementation.
 * 
 * @author David Ehringer
 * 
 */
public class SimpleNamespaceContext implements NamespaceContext {

	private Map<String, String> prefixToNamespaceUri = new HashMap<String, String>();

	// Always access through the getPrefixesSafely method
	private Map<String, List<String>> namespaceUriToPrefixes = new HashMap<String, List<String>>();

	private String defaultNamespaceUri = "";

	public String getNamespaceURI(String prefix) {
		if (XMLConstants.XML_NS_PREFIX.equals(prefix)) {
			return XMLConstants.XML_NS_URI;
		} else if (XMLConstants.XMLNS_ATTRIBUTE.equals(prefix)) {
			return XMLConstants.XMLNS_ATTRIBUTE_NS_URI;
		} else if (XMLConstants.DEFAULT_NS_PREFIX.equals(prefix)) {
			return defaultNamespaceUri;
		} else if (prefixToNamespaceUri.containsKey(prefix)) {
			return prefixToNamespaceUri.get(prefix);
		}
		return "";
	}

	public String getPrefix(String namespaceURI) {
		List<String> prefixes = getPrefixesSafely(namespaceURI);
		if (prefixes.size() == 0) {
			return null;
		}
		return prefixes.get(0);
	}

	@SuppressWarnings("rawtypes")
	public Iterator getPrefixes(String namespaceURI) {
		return getPrefixesSafely(namespaceURI).iterator();
	}

	private List<String> getPrefixesSafely(String namespaceURI) {
		List<String> prefixes = namespaceUriToPrefixes.get(namespaceURI);
		if (prefixes == null) {
			prefixes = new ArrayList<String>();
			namespaceUriToPrefixes.put(namespaceURI, prefixes);
		}
		return prefixes;
	}

	public static SimpleNamespaceContext aNamespaceContext() {
		return new SimpleNamespaceContext();
	}

	public void bind(String prefix, String namespaceURI) {
		prefixToNamespaceUri.put(prefix, namespaceURI);
		getPrefixesSafely(namespaceURI).add(prefix);
	}

	public void setBindings(Map<String, String> bindings) {
		for (Map.Entry<String, String> entry : bindings.entrySet()) {
			bind(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * A fluent interface for adding bindings.
	 * 
	 * @return
	 */
	public SimpleNamespaceContext withBinding(String prefix, String namespaceURI) {
		bind(prefix, namespaceURI);
		return this;
	}
}