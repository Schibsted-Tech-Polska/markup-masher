package no.schibsted.markupmasher.formats

import static no.schibsted.markupmasher.formats.FormatUtils.escapeURL

class HTMLOutput implements OutputFormat {
    String opening(Map markup) {
        switch(markup.type) {
            case 'style:strong': return '<strong>'
            case 'style:em': return '<em>'
            case 'link:external': return "<a href=\"${escapeURL(markup.uri)}\">"
            default:
                return ''
        }
    }

    String closing(Map markup) {
        switch(markup.type) {
            case 'style:strong': return '</strong>'
            case 'style:em': return '</em>'
            case 'link:external': return '</a>'
            default:
                return ''
        }
    }
}
