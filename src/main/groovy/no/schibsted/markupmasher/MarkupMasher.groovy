package no.schibsted.markupmasher

import no.schibsted.markupmasher.formats.HTMLOutput
import no.schibsted.markupmasher.formats.MarkdownOutput
import no.schibsted.markupmasher.formats.MarkupTrackingString
import no.schibsted.markupmasher.formats.OutputFormat

/**
 * MarkupMasher lets you transform raw text + markup into HTML, Markdown, or any other
 * tag-based format that you have an OutputFormat implementation for.
 */
class MarkupMasher {
    /** Output format for HTML. **/
    static final HTMLOutput HTML = new HTMLOutput()
    /** Output format for Markdown. **/
    static final MarkdownOutput MARKDOWN = new MarkdownOutput()

    /**
     * Convenience method for formatting to HTML.
     * @see #formatted(java.lang.String, java.util.List, no.schibsted.markupmasher.formats.OutputFormat)
     */
    String toHTML(String rawText, List<Map> markup) {
        formatted(rawText, markup, HTML)
    }

    /**
     * Convenience method for formatting to Markdown.
     * @see #formatted(java.lang.String, java.util.List, no.schibsted.markupmasher.formats.OutputFormat)
     */
    String toMarkdown(String rawText, List<Map> markup) {
        formatted(rawText, markup, MARKDOWN)
    }

    /**
     * Given the "markup" as a List of JSON Maps, applies this markup to a string
     * and returns the formatted result. Use the constants in this class to choose
     * a format, or define your own.
     *
     * @param rawText the text to apply markup to
     * @param markup the markup to apply
     * @param format an OutputFormat object describing the desired output format
     * @return a formatted string with the markup applied
     */
    String formatted(String rawText, List<Map> markup, OutputFormat format) {
        new MarkupTrackingString(rawText, markup).toFormattedString(format)
    }
}
