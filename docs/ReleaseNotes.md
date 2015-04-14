# 1.0 #

_**In Progress**_

Changes since the 0.10 version:

  * XPath 2.0 support
  * DOMSource returned by methods in XmlConverter is decorated with a user friendly toString()
  * XPathReturnType.evaluationMode is now protected and allows for additional return types
  * XpathReturnType.returningAnXmlNode() has a more usable return type
  * Added an isSimilarTo() matcher to supplement the existing isEquivalentTo() matcher to address ordering issues around equivalence.  See [issue 7](http://code.google.com/p/xml-matchers/issues/detail?id=7).
  * Upgraded Hamcrest dependency to 1.3

# 0.10 #

First fully featured, "production-ready" release.
