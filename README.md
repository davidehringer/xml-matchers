# xml-matchers
A collection of Hamcrest matchers for XML documents. The matchers include XPath 1.0 and 2.0, schema validation using XML Schema or RelaxNG(experimental), and XML document equivalence. Check out the [Tutorial](docs/Tutorial.md) page for a quick overview.

XML Matchers allow you to do things like the following.
```java
Source xml = ...
assertThat(xml, hasXPath("/mountains/mountain"));

Node xml = ...
assertThat(
       the(xml),
        hasXPath("count(/mountains/mountain)", 
                returningANumber(), 
                greaterThanOrEqualTo(2d)));


String xml = "<mountains><mountain>K2</mountain></mountains>";
String xmlWithSpaceWrappingText = "<mountains><mountain>\n\tK2\n\t </mountain></mountains>";
assertThat(the(xml), isEquivalentTo(the(xmlWithSpaceWrappingText)));


Schema schema = w3cXmlSchemaFromClasspath("org/xmlmatchers/validation/example.xsd");
String xml = "<person private=\"true\"><name>Dave</name></person>";
assertThat(the(xml), conformsTo(schema));
```
## Documentation
* [Tutorial](docs/Tutorial.md)
* [XML Converters](XmlConverters.md)
* [Release Notes](ReleaseNotes.md)
* [Distributions](Distributions.md)
* [Design Considerations](DesignConsiderations.md)