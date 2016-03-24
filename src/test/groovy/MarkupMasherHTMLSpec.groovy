import no.schibsted.markupmasher.MarkupMasher
import spock.lang.Specification

class MarkupMasherHTMLSpec extends Specification{
    MarkupMasher mm = new MarkupMasher()

    def "should return text unchanged if there is no markup"() {
        given:
            String original = "Hello world."
        when:
            String result = mm.toHTML(original, null)
        then:
            result == original
    }

    def "should apply bold correctly"() {
        given:
        String original = "Hello world."
        List markup = [[offset:6, length:5, type: 'style:strong']]
        when:
        String result = mm.toHTML(original, markup)
        then:
        result == "Hello <strong>world</strong>."
    }

    def "should apply italics correctly"() {
        given:
        String original = "Hello world."
        List markup = [[offset:6, length:5, type: 'style:em']]
        when:
        String result = mm.toHTML(original, markup)
        then:
        result == "Hello <em>world</em>."
    }

    def "should apply links correctly"() {
        given:
        String original = "Hello world."
        List markup = [[offset:6, length:5, type: 'link:external', uri: 'http://google.pl']]
        when:
        String result = mm.toHTML(original, markup)
        then:
        result == 'Hello <a href="http://google.pl">world</a>.'
    }

    def "should apply multiple styles to one string correctly"() {
        given:
        String original = "Hello world."
        List markup = [
            [offset:6, length:5, type: 'link:external', uri: 'http://google.pl'],
            [offset:3, length:3, type: 'style:strong'],
            [offset:0, length:2, type: 'style:em']
        ]
        when:
        String result = mm.toHTML(original, markup)
        then:
        result == '<em>He</em>l<strong>lo </strong><a href="http://google.pl">world</a>.'
    }

    def "should apply nested styles correctly"() {
        given:
        String original = "Hello world."
        List markup = [
            [offset:6, length:5, type: 'link:external', uri: 'http://google.pl'],
            [offset:7, length:1, type: 'style:em'],
            [offset:0, length:12, type: 'style:strong']
        ]
        when:
        String result = mm.toHTML(original, markup)
        then:
        result == '<strong>Hello <a href="http://google.pl">w<em>o</em>rld</a>.</strong>'
    }

    def "should ignore criss-crossing styling"() {
        given:
        String original = "Hello world."
        List markup = [
            [offset:0, length:9, type: 'style:strong'],
            [offset:6, length:6, type: 'style:em']
        ]
        when:
        String result = mm.toHTML(original, markup)
        then:
        result == '<strong>Hello wor</strong>ld.'
    }
}
