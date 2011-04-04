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

import static org.xmlmatchers.validation.SchemaFactory.*;

import java.util.Arrays;
import java.util.Collection;

import javax.xml.validation.Schema;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.xml.sax.SAXException;

/**
 * @author David Ehringer
 * 
 */
@RunWith(Parameterized.class)
public abstract class AbstractConformsToSchemaTest {

	protected Schema schema;

	public AbstractConformsToSchemaTest(Schema schema) {
		this.schema = schema;
	}

	@Parameters
	public static Collection<Schema[]> data() throws SAXException {
		return Arrays
				.asList(new Schema[][] {
						{ w3cXmlSchemaFromClasspath("org/xmlmatchers/validation/example.xsd") },//
						{ relaxNGSchemaFromClasspath("org/xmlmatchers/validation/example.rng") },//
//						{ relaxNGSchemaFromClasspath("org/xmlmatchers/validation/example.rnc") } //
				});
	}
}
