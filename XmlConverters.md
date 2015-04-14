# Converters #

The [XmlConverters](http://code.google.com/p/xml-matchers/source/browse/trunk/xml-matchers/src/main/java/org/xmlmatchers/transform/XmlConverters.java) class has a set of "`the`" methods that will convert XML in various formats into a Source object, the standard format fused by all the matchers. The following formats are supported:
  * org.w3c.dom.Node
  * javax.xml.transform.Source (as a pure "sugar" identity conversion)
  * javax.xml.transform.Result
  * javax.xml.soap.SOAPElement (as a subclass of Node)
  * java.lang.String


For example:

```
import static org.xmlmatchers.transform.XmlConverters.the;

...

String xml = "<person private=\"true\"><name>Dave</name></person>";
assertThat(the(xml), conformsTo(schema));
```