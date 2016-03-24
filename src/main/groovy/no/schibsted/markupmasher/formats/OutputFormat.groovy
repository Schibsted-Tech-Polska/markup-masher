package no.schibsted.markupmasher.formats


public interface OutputFormat {
    String opening(Map markup)
    String closing(Map markup)
}