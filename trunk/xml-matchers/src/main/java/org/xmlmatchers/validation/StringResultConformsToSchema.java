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
package org.xmlmatchers.validation;

import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.xmlmatchers.transform.StringSource;
import org.xmlmatchers.transform.IdentityTransformer;
import org.xmlmatchers.transform.StringResult;

/**
 * @author David Ehringer
 * 
 */
public class StringResultConformsToSchema extends
		ConformsToSchema<StringResult> {

	private IdentityTransformer transformer = new IdentityTransformer();

	public StringResultConformsToSchema(Schema schema) {
		super(schema);
	}

	@Override
	protected DOMSource convert(StringResult stringResult) {
		DOMResult result = new DOMResult();
		Source source = StringSource.toSource(stringResult.toString());
		transformer.transform(source, result);
		return new DOMSource(result.getNode());
	}

	@Factory
	public static Matcher<StringResult> conformsTo(Schema schema) {
		return new StringResultConformsToSchema(schema);
	}
}
