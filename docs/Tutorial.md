# Introduction #

Xml-matchers provides three categories of XML-based [Hamcrest](http://hamcrest.org/) matchers: XPath, Equivalence, Schema Compliance.  The matchers natively support common XML formats in Java such as [org.w3c.dom.Node](http://download.oracle.com/javase/6/docs/api/org/w3c/dom/Node.html), [javax.xml.transform.Source](http://download.oracle.com/javase/6/docs/api/javax/xml/transform/Source.html), [javax.xml.transform.Result](http://download.oracle.com/javase/6/docs/api/javax/xml/transform/Result.html), [javax.xml.soap.SOAPElement](http://download.oracle.com/javase/6/docs/api/javax/xml/soap/SOAPElement.html), and basic Strings.

[Hamcrest](http://hamcrest.org/) version 1.2 is a required dependency.

```
<dependency>
     <groupId>org.hamcrest</groupId>
     <artifactId>hamcrest-core</artifactId>
     <version>1.2</version>
</dependency>
```

Use can use [org.xmlmatchers.XmlMatchers](../xml-matchers/src/main/java/org/xmlmatchers/XmlMatchers.java) for static importing of all the matchers.

# XPath #

The XPath matchers support:
  * Basic Matching
  * Namespaces
  * Feeding the result of an XPath expression into another Matcher
  * Multiple return types for XPath expressions

**XML Formats Supported**

See the [XmlConverters](XmlConverters.md) page for sugar methods to convert all the types of XML listed above into a Source object, the native format for the matchers.

**Basic Matching**
```
assertThat(xml, hasXPath("/mountains/mountain"));
assertThat(xml, hasXPath("/mountains/mountain[2]/name"));
assertThat(xml, hasXPath("/mountains/mountain[@id='a']/@altname"));
```

**Basic Matching using Namespaces**
```
import org.xmlmatchers.namespace.SimpleNamespaceContext;

...

NamespaceContext usingNamespaces = new SimpleNamespaceContext()
			.withBinding("m", "http://mountains.com");
assertThat(xml, hasXPath("/mountains/m:range", usingNamespaces));
```

**Matching XPath Expression Results - Basic**
```
assertThat(
	xml,
	hasXPath("/mountains/mountain[@id='a']/name",
	    equalToIgnoringCase("EVEREST")));

assertThat(xml, hasXPath("count(/mountains/mountain)", equalTo("2")));
assertThat(
	xml,
	hasXPath("/mountains/m:oceanRidge/@m:depth", usingNamespaces,
		equalTo("-5000")));
assertThat(
	xml,
	hasXPath("/mountains/mountain[@id='a']/name",
		containsString("Everest")));
```

**Matching XPath Expression Results - Type Safe**
```
// Specifying return types
import static org.xmlmatchers.xpath.XpathReturnType.returningABoolean;
import static org.xmlmatchers.xpath.XpathReturnType.returningANumber;
import static org.xmlmatchers.xpath.XpathReturnType.returningAnXmlNode;

// and leveraging the Equivalence matchers
import static org.xmlmatchers.XmlMatchers.equivalentTo;

...

assertThat(
	xml,
	hasXPath("/mountains/mountain[@id='a']/name",
		returningAnXmlNode(),
		equivalentTo("<name>Everest</name>")));

assertThat(
	xml,
	hasXPath("/mountains/mountain[@id='a']/name",
		returningAnXmlNode(),
		equivalentTo("<name><!-- some comment -->Everest</name>")));

assertThat(
       xml,//
	hasXPath("count(/mountains/mountain)", //
		returningANumber(), //
		greaterThanOrEqualTo(2d)));

assertThat(
	xml,
	hasXPath("/mountains/volcanoe/@eruptions", 
               returningANumber(),
	       closeTo(4, 2)));

assertThat(
	xml,
	hasXPath("/mountains/volcanoe/@eruptions", 
                returningANumber(),
		lessThan(5d)));

assertThat(xml,//
	hasXPath("not(/mountains/climber)", //
		returningABoolean(),//
		equalTo(true)));
```

Take a look at [some example tests for additional examples](../xml-matchers/src/test/java/org/xmlmatchers/xpath/HasXPathTest.java).

# Equivalence #

The equivalence matchers look for XML documents that are semantically equivalent regardless of their format.   Basic string comparisons and regular expressions often aren't enough when dealing with XML documents. Whitespace, attribute ordering, comments, and CDATA/text usage are all hard to deal with and usually irrelevant when comparing XML documents.  These matchers make comparing XML documents easier.

The heavy lifting for these matchers is left to XMLUnit as the matchers are a thin facade over the Diff facility.  XMLUnit 1.3 is a required dependency for this functionality.

```
<dependency>
     <groupId>xmlunit</groupId>
     <artifactId>xmlunit</artifactId>
     <version>1.3</version>
</dependency>
```

A few example use cases follow.  They assume a few static imports.  The specific imports may vary by example.

```
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat; // or Hamcrest's version
import static org.xmlmatchers.XmlMatchers.equivalentTo;
import static org.xmlmatchers.XmlMatchers.isEquivalentTo;
import static org.xmlmatchers.transform.XmlConverters.the;
```

**Whitespace**
```
String xml = "<mountains><mountain>K2</mountain></mountains>";
String xmlWithSpaceWrappingText = "<mountains><mountain>\n\tK2\n\t </mountain></mountains>";
assertThat(the(xml), isEquivalentTo(the(xmlWithSpaceWrappingText)));
```

**Comments**
```
String xml = "<mountains><mountain>K2</mountain></mountains>";
String xmlWithComments = "<mountains><!-- 28,251 ft. --><mountain>K2</mountain></mountains>";
assertThat(the(xml), isEquivalentTo(the(xmlWithComments)));
```

# Schema Compliance #

**W3C XML Schema**
```
import static org.xmlmatchers.XmlMatchers.conformsTo;
import static org.xmlmatchers.validation.SchemaFactory.w3cXmlSchemaFromClasspath;


...
Schema schema = w3cXmlSchemaFromClasspath("org/xmlmatchers/validation/example.xsd");
String xml = "<person private=\"true\"><name>Dave</name></person>";
assertThat(the(xml), conformsTo(schema));
```

**RelaxNG**

Support for XML and compact schema formats is very experimental at this point.

# Usage with JUnit #

...
