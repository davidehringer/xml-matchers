/*
 * Copyright 2013 the original author or authors.
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

import static org.hamcrest.Matchers.equalTo;
import static org.xmlmatchers.transform.XmlConverters.the;
import static org.junit.Assert.assertThat;
import static org.xmlmatchers.xpath.HasXPath.hasXPath;

import org.junit.Test;

/**
 * @author David Ehringer
 */
public class HasXPath2Test {

	private static final String EXAMPLE_XML = "<stuff>\n"
			+ "	<thing>hat</thing>\n" + "	<thing>cat</thing>\n"
			+ "	<thing>car</thing>\n" + "</stuff>";

	// some trivial tests to see if XPath 2.0 is available
	
	@Test
	public void theEndsWithFunctionIsAvailable() {
		assertThat(the(EXAMPLE_XML),
				hasXPath("count(//thing[ends-with(., 'at')])", equalTo("2")));
	}
}
