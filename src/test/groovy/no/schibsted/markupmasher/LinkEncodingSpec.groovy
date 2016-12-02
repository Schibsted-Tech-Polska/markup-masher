package no.schibsted.markupmasher

import spock.lang.Specification

class LinkEncodingSpec extends Specification {
    def "links should get properly escaped"() {
        given:
        def rawText = "Hello."
        def markup = [
            [offset: 0, length: 6, type: 'link:external', uri: 'http://example.org/"Hi (there)"']
        ]
        when:
        def html = new MarkupMasher().toHTML(rawText, markup)
        def md = new MarkupMasher().toMarkdown(rawText, markup)
        then:
        html == '<a href="http://example.org/&quot;Hi &#40;there&#41;&quot;">Hello.</a>'
        md == "[Hello.](http://example.org/&quot;Hi &#40;there&#41;&quot;)"
    }
}
