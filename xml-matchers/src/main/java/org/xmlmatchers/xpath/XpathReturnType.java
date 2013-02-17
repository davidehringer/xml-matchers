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
package org.xmlmatchers.xpath;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathConstants;

/**
 * @author David Ehringer
 */
public abstract class XpathReturnType<T> {
	
	protected abstract QName evaluationMode();

	public static XpathReturnType<Double> returningANumber() {
		return new XpathReturnType<Double>() {
			@Override
			protected QName evaluationMode() {
				return XPathConstants.NUMBER;
			}};
	}

	public static XpathReturnType<String> returningAnXmlNode() {
		return new XpathReturnType<String>() {
			@Override
			protected QName evaluationMode() {
				return XPathConstants.NODE;
			}};
	}

	public static XpathReturnType<String> returningAString() {
		return new XpathReturnType<String>() {
			@Override
			protected QName evaluationMode() {
				return XPathConstants.STRING;
			}};
	}
	public static XpathReturnType<Boolean> returningABoolean() {
		return new XpathReturnType<Boolean>() {
			@Override
			protected QName evaluationMode() {
				return XPathConstants.BOOLEAN;
			}};
	}
}
