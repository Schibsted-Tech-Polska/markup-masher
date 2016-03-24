package no.schibsted.markupmasher.formats

import static no.schibsted.markupmasher.formats.FormatUtils.escapeURL

class MarkdownOutput implements OutputFormat {
    String opening(Map markup) {
        switch(markup.type) {
            case 'style:strong': return '**'
            case 'style:em': return '_'
            case 'link:external': return "["
            default:
                return ''
        }
    }

    String closing(Map markup) {
        switch(markup.type) {
            case 'style:strong': return '**'
            case 'style:em': return '_'
            case 'link:external': return "](${escapeURL(markup.uri)})"
            default:
                return ''
        }
    }
}
