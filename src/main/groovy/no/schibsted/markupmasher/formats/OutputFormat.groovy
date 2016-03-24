package no.schibsted.markupmasher.formats

/**
 * Implementations of this class let us convert markup to different output formats.
 */
public interface OutputFormat {
    /**
     * Returns the opening "tag" for the given type of markup, or "" if the markup
     * is not understood.
     */
    String opening(Map markup)
    /**
     * Returns the closing "tag" for the given type of markup, or "" if the markup
     * is not understood.
     */
    String closing(Map markup)
}
