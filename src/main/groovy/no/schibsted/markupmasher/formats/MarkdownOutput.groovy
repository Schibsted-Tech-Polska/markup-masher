package no.schibsted.markupmasher.formats

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
            case 'link:external': return "](${markup.uri})"
            default:
                return ''
        }
    }
}
