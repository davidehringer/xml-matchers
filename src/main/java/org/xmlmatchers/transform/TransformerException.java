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

/**
 * An exception resulting from issues while attempting to perform an XML
 * transformation.
 * 
 * @author David Ehringer
 */
@SuppressWarnings("serial")
public class TransformerException extends RuntimeException {

	public TransformerException() {
		super();
	}

	public TransformerException(String message, Throwable t) {
		super(message, t);
	}

	public TransformerException(String message) {
		super(message);
	}

	public TransformerException(Throwable t) {
		super(t);
	}

}
