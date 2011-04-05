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

import javax.xml.namespace.NamespaceContext;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import org.hamcrest.Matcher;
import org.w3c.dom.Node;

/**
 * @author David Ehringer
 */
public class NodeHasXPath extends HasXPath<Node> {

	public NodeHasXPath(String xPathExpression,
			Matcher<String> valueMatcher,
			NamespaceContext namespaceContext) {
		super(xPathExpression, valueMatcher, namespaceContext);
	}

	public NodeHasXPath(String xPathExpression) {
		super(xPathExpression);
	}

	@Override
	protected Source convertToSource(Node node) throws IllegalArgumentException {
		return new DOMSource(node);
	}
	// TODO Factory
}
