
package org.xmlmatchers;

public class XmlMatchers {

	public static org.hamcrest.Matcher<java.lang.String> isEquivalentTo(
			java.lang.String xml) {
		return org.xmlmatchers.equivalence.IsEquivalentTo.isEquivalentTo(xml);
	}

	public static org.hamcrest.Matcher<java.lang.String> isEquivalentTo(
			javax.xml.transform.Source xml) {
		return org.xmlmatchers.equivalence.IsEquivalentTo.isEquivalentTo(xml);
	}

	public static org.hamcrest.Matcher<java.lang.String> isEquivalentTo(
			org.w3c.dom.Node xml) {
		return org.xmlmatchers.equivalence.IsEquivalentTo.isEquivalentTo(xml);
	}

	public static org.hamcrest.Matcher<java.lang.String> isEquivalentTo(
			org.xmlmatchers.transform.StringResult xml) {
		return org.xmlmatchers.equivalence.IsEquivalentTo.isEquivalentTo(xml);
	}

	public static org.hamcrest.Matcher<java.lang.String> equivalentTo(
			java.lang.String xml) {
		return org.xmlmatchers.equivalence.IsEquivalentTo.equivalentTo(xml);
	}

	public static org.hamcrest.Matcher<java.lang.String> equivalentTo(
			javax.xml.transform.Source xml) {
		return org.xmlmatchers.equivalence.IsEquivalentTo.equivalentTo(xml);
	}

	public static org.hamcrest.Matcher<java.lang.String> equivalentTo(
			org.w3c.dom.Node xml) {
		return org.xmlmatchers.equivalence.IsEquivalentTo.equivalentTo(xml);
	}

	public static org.hamcrest.Matcher<java.lang.String> equivalentTo(
			org.xmlmatchers.transform.StringResult xml) {
		return org.xmlmatchers.equivalence.IsEquivalentTo.equivalentTo(xml);
	}

	public static org.hamcrest.Matcher<org.xmlmatchers.transform.StringResult> resultHasXPath(
			java.lang.String xPath,
			javax.xml.namespace.NamespaceContext namespaceContext) {
		return org.xmlmatchers.xpath.StringResultHasXPath.hasXPath(xPath,
				namespaceContext);
	}

//	public static org.hamcrest.Matcher<org.xmlmatchers.transform.StringResult> resultHasXPath(
//			java.lang.String xPath,
//			org.hamcrest.Matcher<? super java.lang.String> valueMatcher) {
//		return org.xmlmatchers.xpath.StringResultHasXPath.hasXPath(xPath,
//				valueMatcher);
//	}

//	public static org.hamcrest.Matcher<org.xmlmatchers.transform.StringResult> resultHasXPath(
//			java.lang.String xPath,
//			org.hamcrest.Matcher<? super java.lang.String> valueMatcher,
//			javax.xml.namespace.NamespaceContext namespaceContext) {
//		return org.xmlmatchers.xpath.StringResultHasXPath.hasXPath(xPath,
//				valueMatcher, namespaceContext);
//	}

	public static org.hamcrest.Matcher<org.xmlmatchers.transform.StringResult> resultHasXPath(
			java.lang.String xPath) {
		return org.xmlmatchers.xpath.StringResultHasXPath.hasXPath(xPath);
	}

	public static org.hamcrest.Matcher<java.lang.String> stringHasXPath(
			java.lang.String xPath,
			javax.xml.namespace.NamespaceContext namespaceContext) {
		return org.xmlmatchers.xpath.StringHasXPath.hasXPath(xPath,
				namespaceContext);
	}

//	public static org.hamcrest.Matcher<java.lang.String> stringHasXPath(
//			java.lang.String xPath,
//			org.hamcrest.Matcher<? super java.lang.String> valueMatcher) {
//		return org.xmlmatchers.xpath.StringHasXPath.hasXPath(xPath,
//				valueMatcher);
//	}

//	public static org.hamcrest.Matcher<java.lang.String> stringHasXPath(
//			java.lang.String xPath,
//			org.hamcrest.Matcher<? super java.lang.String> valueMatcher,
//			javax.xml.namespace.NamespaceContext namespaceContext) {
//		return org.xmlmatchers.xpath.StringHasXPath.hasXPath(xPath,
//				valueMatcher, namespaceContext);
//	}

	public static org.hamcrest.Matcher<java.lang.String> stringHasXPath(
			java.lang.String xPath) {
		return org.xmlmatchers.xpath.StringHasXPath.hasXPath(xPath);
	}

	public static org.hamcrest.Matcher<javax.xml.transform.Source> sourceHasXPath(
			java.lang.String xPath,
			javax.xml.namespace.NamespaceContext namespaceContext) {
		return org.xmlmatchers.xpath.SourceHasXPath.hasXPath(xPath,
				namespaceContext);
	}

	public static org.hamcrest.Matcher<javax.xml.transform.Source> sourceHasXPath(
			java.lang.String xPath, org.hamcrest.Matcher<? super java.lang.String> valueMatcher) {
		return org.xmlmatchers.xpath.SourceHasXPath.hasXPath(xPath,
				valueMatcher);
	}

	public static org.hamcrest.Matcher<javax.xml.transform.Source> sourceHasXPath(
			java.lang.String xPath,
			javax.xml.namespace.NamespaceContext namespaceContext,
			org.hamcrest.Matcher<? super java.lang.String> valueMatcher) {
		return org.xmlmatchers.xpath.SourceHasXPath.hasXPath(xPath,
				namespaceContext, valueMatcher);
	}

	public static org.hamcrest.Matcher<javax.xml.transform.Source> sourceHasXPath(
			java.lang.String xPath, org.hamcrest.Matcher<? super java.lang.String> valueMatcher,
			javax.xml.namespace.NamespaceContext namespaceContext) {
		return org.xmlmatchers.xpath.SourceHasXPath.hasXPath(xPath,
				valueMatcher, namespaceContext);
	}

	public static org.hamcrest.Matcher<javax.xml.transform.Source> sourceHXPath(
			java.lang.String xPath) {
		return org.xmlmatchers.xpath.SourceHasXPath.hasXPath(xPath);
	}

	public static org.hamcrest.Matcher<java.lang.String> stringConformsTo(
			javax.xml.validation.Schema schema) {
		return org.xmlmatchers.validation.StringConformsToSchema
				.conformsTo(schema);
	}

	public static org.hamcrest.Matcher<org.xmlmatchers.transform.StringResult> resultConformsTo(
			javax.xml.validation.Schema schema) {
		return org.xmlmatchers.validation.StringResultConformsToSchema
				.conformsTo(schema);
	}

	public static org.hamcrest.Matcher<javax.xml.transform.Source> sourceConformsTo(
			javax.xml.validation.Schema schema) {
		return org.xmlmatchers.validation.SourceConformsToSchema
				.conformsTo(schema);
	}

	public static org.hamcrest.Matcher<org.w3c.dom.Node> nodeConformsTo(
			javax.xml.validation.Schema schema) {
		return org.xmlmatchers.validation.NodeConformsToSchema
				.conformsTo(schema);
	}

}
