package no.schibsted.markupmasher

import groovy.json.JsonSlurper
import spock.lang.Specification

/**
 * Spec based on some real corner cases encountered in production.
 */
class CornerCasesSpec extends Specification {
    def "corner case 1"() {
        given:
            MarkupMasher mm = new MarkupMasher()
            def data = new JsonSlurper().parse(this.class.getResourceAsStream("/test-cases/link-parsed-badly.json"))
        when:
            def result = mm.toHTML(data.text, data.markup)
        then:
            result == "<strong>Hard kamp om ny vei: <a href=\"http://www.osloby.no/politikk/Oslo-byradet-gar-inn-for-farre-bilfelt-pa-ny-E18-enn-det-dagens-planer-legger-opp-til-8342527.html?spid_rel=2\">Oslo-byrådet går inn for færre bilfelt enn det dagens planer legger opp til</a></strong>"

    }
}
