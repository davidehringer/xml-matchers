# Guidelines #

  * Multiple, often used XML formats in Java should be supported (e.g. Source, Result, Node, SOAPElement, String, etc.
  * The API should support the formats as natively as possible. In other words, the developer shouldn't have to spend 5 lines of code before using a matcher to convert their object into something the API accepts.
  * Supplementary objects such as Schemas should have a somewhat fluent interface for creating them.  In same manner as XML, the developer shouldn't have to write their own methods to load Schemas from files, the classpath, URLs, etc.

# Implementation (as of 0.8) Drawbacks #

  1. The current implementation requires that each XML type has its own sublcass for each type of matcher.  This leads to a large API and it isn't as easily extensible as would be desired.
  1. Following from 1, a centralized XmlMatchers convenience "matcher library" class is difficult to implement using the language we would like. This is because the "sugar" methods all have the same method signature after type erasure.
> > For example:
```
    @Factory
    public static Matcher<String> hasXPath(String xPath) {
         return new StringHasXPath(xPath);
    }
    @Factory
    public static Matcher<Source> hasXPath(String xPath) {
         return new SourceHasXPath(xPath);
    }
```

  1. Type conversion is repeated across the various types of matchers

# Alternative Implementation Ideas #

Alternatively, the core API could only support a single format, for example Source.  There could be a set of converters that have simple sugar methods that fit in to the typical use case in a fluent manner. Possible method names include `the` or `a`.

```
public abstract class Converter<T>{

    Source convert(T t);
}
public abstract class Converters{

    public static Source the(Source source){
         return source;
    }
    
    public static Source the(Node node){
         return ...
    }
}
```

Client usage:

```
Node xml = ...
assertThat(the(xml), hasXPath("/someElement"));
```