# markup-masher

[![Release](https://jitpack.io/v/Schibsted-Tech-Polska/markup-masher.svg)](https://jitpack.io/#Schibsted-Tech-Polska/markup-masher)

A small library for wrangling the SMP Bench "markup" styling format.

# Usage

Use [jitpack.io](https://jitpack.io/docs/) to get the dependency in, and then (from Groovy):

```groovy
import no.schibsted.markupmasher.MarkupMasher;

MarkupMasher mm = new MarkupMasher()
def markup = [
  [offset: 0, length: 3, type: 'style:strong'],
  [offset: 4, length: 3, type: 'style:em']
]

mm.toHTML("Hello, world.", markup);
mm.toMarkdown("Hello, world.", markup);
```

You can use it from Java too - the only hick-up is that it'll be trickier to obtain the `markup` list of maps.
The format we accept is what Groovy's JSONSlurper produces - a simple untyped List of Maps, with each map being
a markup definition.

