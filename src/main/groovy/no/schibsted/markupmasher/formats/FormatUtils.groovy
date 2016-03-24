package no.schibsted.markupmasher.formats

class FormatUtils {
    static String escapeURL(String url) {
        url
            .replace('&', '&amp;')
            .replace('"', '&quot;') // for HTML
            .replace('(', '&#40;')
            .replace(')', '&#41;')  // for Markdown
    }
}
