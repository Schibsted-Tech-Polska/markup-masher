package no.schibsted.markupmasher

import no.schibsted.markupmasher.formats.HTMLOutput
import no.schibsted.markupmasher.formats.MarkupTrackingString
import no.schibsted.markupmasher.formats.OffsetTrackingString
import no.schibsted.markupmasher.formats.OutputFormat

class MarkupMasher {
    HTMLOutput htmlTemplates = new HTMLOutput()


    /**
     * Given the "markup" as a List of JSON Maps, applies this markup to a string
     * and returns the result as formatted HTML.
     *
     * @param rawText the text to apply markup to
     * @param markup the markup to apply
     * @return an HTML-formatted string with the markup applied
     */
    String toHTML(String rawText, List<Map> markup) {
        formatted(rawText, markup, htmlTemplates)
    }

    private String formatted(String rawText, List<Map> markup, OutputFormat format) {
        new MarkupTrackingString(rawText, markup).toFormattedString(format)
    }
}
