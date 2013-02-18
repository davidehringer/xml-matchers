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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;

/**
 * A facility for tranforming XML from one representation to another without
 * changing the actual XML. In other words, it performs and identity transform.
 * 
 * @author David Ehringer
 */
public class IdentityTransformer {

	private TransformerFactory transformerFactory;
	private Map<String, Object> parameters = new HashMap<String, Object>();
	private Map<String, String> outputProperties = new HashMap<String, String>();

	public IdentityTransformer() {
		this.transformerFactory = newTransformerFactory();
	}

	public IdentityTransformer(TransformerFactory transformerFactory) {
		if (transformerFactory == null) {
			throw new IllegalArgumentException(
					"The TransformerFactory cannot be null");
		}
		this.transformerFactory = transformerFactory;
	}

	public IdentityTransformer(TransformerFactory transformerFactory,
			Map<String, Object> parameters) {
		this(transformerFactory);
		this.parameters.putAll(parameters);
	}

	private TransformerFactory newTransformerFactory() {
		return newTransformerFactory(null);
	}

	private TransformerFactory newTransformerFactory(
			Class<TransformerFactory> transformerFactoryClass) {
		if (transformerFactoryClass != null) {
			try {
				return (TransformerFactory) transformerFactoryClass
						.newInstance();
			} catch (Exception ex) {
				throw new TransformerFactoryConfigurationError(ex,
						"Could not instantiate TransformerFactory");
			}
		} else {
			return TransformerFactory.newInstance();
		}
	}

	/**
	 * Creates a new {@link javax.xml.transform.Transformer}.
	 * <em>Note: javax.xml.transform.Transformers are not
	 * thread-safe.</em>
	 * 
	 */
	private final javax.xml.transform.Transformer createTransformer()
			throws TransformerConfigurationException {
		javax.xml.transform.Transformer transformer = transformerFactory
				.newTransformer();
		for (Entry<String, Object> entry : parameters.entrySet()) {
			transformer.setParameter(entry.getKey(), entry.getValue());
		}
		for (Entry<String, String> entry : outputProperties.entrySet()) {
			transformer.setOutputProperty(entry.getKey(), entry.getValue());
		}
		return transformer;
	}

	/**
	 * Transforms the given {@link Source} to the given {@link Result}.
	 * 
	 * @param source
	 *            the source to transform from
	 * @param result
	 *            the result to transform to
	 */
	public final void transform(Source source, Result result)
			throws TransformerException {
		try {
			javax.xml.transform.Transformer transformer = createTransformer();
			transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			throw new TransformerException(e);
		} catch (javax.xml.transform.TransformerException e) {
			throw new TransformerException(e);
		}
	}

	/**
	 * Transforms the given {@link Source} to the given {@link Result}.
	 * 
	 * @param source
	 *            the source to transform from
	 * @param result
	 *            the result to transform to
	 * @param The
	 *            Map of parameters to apply to the transform
	 */
	public final void transform(Source source, Result result,
			Map<String, Object> parameters) throws TransformerException {
		try {
			javax.xml.transform.Transformer transformer = createTransformer();
			if (parameters != null) {
				for (Entry<String, Object> entry : parameters.entrySet()) {
					transformer.setParameter(entry.getKey(), entry.getValue());
				}
			}
			transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			throw new TransformerException(e);
		} catch (javax.xml.transform.TransformerException e) {
			throw new TransformerException(e);
		}
	}
}
