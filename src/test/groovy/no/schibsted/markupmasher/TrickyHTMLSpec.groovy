package no.schibsted.markupmasher

import spock.lang.Specification

class TrickyHTMLSpec extends Specification {
    MarkupMasher mm = new MarkupMasher()

    def "should apply multiple styles in the same place correctly"() {
        given:
        String original = "Hello world."
        List markup = [
            [offset:0, length:5, type: 'link:external', uri: 'http://google.pl'],
            [offset:0, length:5, type: 'style:em'],
            [offset:0, length:5, type: 'style:strong']
        ]
        when:
        String result = mm.toHTML(original, markup)
        then:
        result == '<a href="http://google.pl"><em><strong>Hello</strong></em></a> world.'
    }

    def "should be able to apply identical markup twice"() {
        given:
        String original = "Hello world."
        List markup = [
            [offset:0, length:5, type: 'style:strong'],
            [offset:0, length:5, type: 'style:strong']
        ]
        when:
        String result = mm.toHTML(original, markup)
        then:
        result == '<strong><strong>Hello</strong></strong> world.'
    }


}
