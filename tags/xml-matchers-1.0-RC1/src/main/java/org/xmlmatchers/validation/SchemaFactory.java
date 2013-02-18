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

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;
import static javax.xml.XMLConstants.RELAXNG_NS_URI;
import static javax.xml.XMLConstants.XML_DTD_NS_URI;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.transform.Source;
import javax.xml.validation.Schema;

import org.xml.sax.SAXException;
import org.xmlmatchers.transform.StringSource;

/**
 * <em>Anything besides the W3C support is still experimental.</em>
 * 
 * @author David Ehringer
 */
public class SchemaFactory {

	private static final String RELAX_NG_FACTORY_KEY = "javax.xml.validation.SchemaFactory:"
			+ RELAXNG_NS_URI;
	private static final String DEFAULT_RELAX_NG_FACTORY = "org.iso_relax.verifier.jaxp.validation.RELAXNGSchemaFactoryImpl";
//	private static final String DEFAULT_RELAX_NG_FACTORY = "com.thaiopensource.relaxng.jaxp.CompactSyntaxSchemaFactory";
//	private static final String DEFAULT_RELAX_NG_FACTORY = "com.thaiopensource.relaxng.jaxp.XMLSyntaxSchemaFactory";

	private SchemaFactory() {

	}

	public static SchemaFactory newInstance(String language) {
		return null;
	}
	
	public Schema from(URL url){
		return null;
	}
	// TODO create a public constructor that allows passing in the language and optionally an implementation class for the schemaFactory.

	public static Schema w3cXmlSchemaFromString(String schemaContents)
			throws SAXException {
		Source source = StringSource.toSource(schemaContents);
		return schemaFrom(source, W3C_XML_SCHEMA_NS_URI);
	}

	public static Schema w3cXmlSchemaFromUrl(String urlString)
			throws SAXException, MalformedURLException {
		URL url = new URL(urlString);
		return w3cXmlSchemaFrom(url);
	}

	public static Schema w3cXmlSchemaFromClasspath(String resource)
			throws SAXException {
		URL url = classPathResourceToURL(resource);
		return w3cXmlSchemaFrom(url);
	}

	public static Schema w3cXmlSchemaFrom(File file) throws SAXException {
		return schemaFrom(file, W3C_XML_SCHEMA_NS_URI);
	}

	public static Schema w3cXmlSchemaFrom(URL url) throws SAXException {
		return schemaFrom(url, W3C_XML_SCHEMA_NS_URI);
	}

	public static Schema dtdSchemaFromClasspath(String resource)
			throws SAXException {
		URL url = classPathResourceToURL(resource);
		return dtdSchemaFrom(url);
	}

	public static Schema dtdSchemaFrom(URL url) throws SAXException {
		return schemaFrom(url, XML_DTD_NS_URI);
	}

	public static Schema relaxNGSchemaFromClasspath(String resource)
			throws SAXException {
		URL url = classPathResourceToURL(resource);
		return relaxNGSchemaFrom(url);
	}

	public static Schema relaxNGSchemaFrom(URL url) throws SAXException {
		if (System.getProperty(RELAX_NG_FACTORY_KEY) == null) {
			System.setProperty(RELAX_NG_FACTORY_KEY, DEFAULT_RELAX_NG_FACTORY);
		}
		return schemaFrom(url, RELAXNG_NS_URI);
	}

	private static Schema schemaFrom(URL url, String schemaLanguage)
			throws SAXException {
		javax.xml.validation.SchemaFactory factory = javax.xml.validation.SchemaFactory
				.newInstance(schemaLanguage);
		return factory.newSchema(url);
	}

	private static Schema schemaFrom(File file, String schemaLanguage)
			throws SAXException {
		javax.xml.validation.SchemaFactory factory = javax.xml.validation.SchemaFactory
				.newInstance(schemaLanguage);
		return factory.newSchema(file);
	}

	private static Schema schemaFrom(Source source, String schemaLanguage)
			throws SAXException {
		javax.xml.validation.SchemaFactory factory = javax.xml.validation.SchemaFactory
				.newInstance(schemaLanguage);
		return factory.newSchema(source);
	}

	private static URL classPathResourceToURL(String resource) {
		return Thread.currentThread().getContextClassLoader()
				.getResource(resource);
	}
}
