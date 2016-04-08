package no.schibsted.markupmasher.formats

import groovy.transform.EqualsAndHashCode
import groovy.transform.TupleConstructor

class MarkupTrackingString {
    SortedSet<Marker> markers = new TreeSet<>()
    String baseString

    MarkupTrackingString(String baseString, List<Map> markups) {
        this.baseString = baseString
        markups.each { m -> addMarkup(m) }
    }

    private void addMarkup(Map markup) {
        int start = clamp(markup.offset, 0, baseString.length())
        int end = clamp(markup.offset + markup.length, 0, baseString.length())
        if (start == end) return

        def startMarker = new Marker(MarkerType.OPENING, start, markup)
        def endMarker = new Marker(MarkerType.CLOSING, end, markup)

        // detect and remove styling that would cause mismatched open/close tags
        def rangeBetween = markers.subSet(startMarker, endMarker)
        def openersInRange = rangeBetween.count { it.type == MarkerType.OPENING }
        def closersInRange = rangeBetween.count { it.type == MarkerType.CLOSING }
        if (openersInRange != closersInRange) {
            // found mismatched/criss-crossing styling, ignore this entry
            return
        }

        markers += [startMarker, endMarker]
    }

    String toFormattedString(OutputFormat format) {
        StringBuilder sb = new StringBuilder()
        int lastIndex = 0

        // nothing to do here.
        if(!shouldFormat()) return baseString ?: ''

        // add text interspersed with the needed markers
        markers.each { m ->
            sb.append(baseString.substring(lastIndex, m.index))
            switch(m.type) {
                case MarkerType.OPENING: sb.append(format.opening(m.markup)); break
                case MarkerType.CLOSING: sb.append(format.closing(m.markup)); break
            }
            lastIndex = m.index
        }

        // fix up - the last substring might have no marker after it
        sb.append(baseString.substring(lastIndex, baseString.length()))

        // done!
        return sb.toString()
    }

    private Boolean shouldFormat() {
        baseString != null && baseString.size() > 0 &&
        markers.size() > 0
    }

    @TupleConstructor
    @EqualsAndHashCode
    private static class Marker implements Comparable<Marker> {
        MarkerType type
        int index
        Map markup

        @Override
        int compareTo(Marker o) {
            if (index != o.index) {
                // by index ascending
                return index - o.index
            } else {
                // CLOSING markers before OPENING markers at the same index
                return o.type.ordinal() - type.ordinal()
            }
        }
    }
    private static enum MarkerType { OPENING, CLOSING }

    private static int clamp(int value, int low, int high) {
        if (value > high) return high
        if (value < low) return low
        return value
    }
}
