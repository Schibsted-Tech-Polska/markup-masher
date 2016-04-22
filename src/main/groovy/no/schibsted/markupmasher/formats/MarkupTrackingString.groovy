package no.schibsted.markupmasher.formats

import groovy.transform.EqualsAndHashCode
import groovy.transform.TupleConstructor

class MarkupTrackingString {
    SortedSet<Marker> markers = new TreeSet<>()
    String baseString
    int markerNo = 0

    MarkupTrackingString(String baseString, List<Map> markups) {
        this.baseString = baseString
        markups.each { m -> addMarkup(m) }
    }

    private void addMarkup(Map markup) {
        String type = markup.type as String
        Integer length = markup.length as Integer
        Integer offset = markup.offset as Integer

        // rejected malformed markup
        if (type == null || length == null || offset == null) {
            return
        }

        int start = clamp(offset, 0, baseString.length())
        int end = clamp(offset + length, 0, baseString.length())
        if (start == end) return

        def startMarker = new Marker(MarkerType.OPENING, start, markup, start, markerNo++)
        def endMarker = new Marker(MarkerType.CLOSING, end, markup, start, markerNo++)

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
        int startingIndex
        int ordinalNumber

        @Override
        int compareTo(Marker o) {
            if (index != o.index) {
                // by index ascending, if indices are different
                return index - o.index
            } else if (o.type != type) {
                // CLOSING markers before OPENING markers at the same index
                return o.type.ordinal() - type.ordinal()
            } else if (o.startingIndex != startingIndex) {
                // tags opened later have to be closed earlier
                return o.startingIndex - startingIndex;
            } else {
                // any sort of arbitrary ordering will be OK by this point, as long as its consistent
                // we use the order in which the markup was added
                // the order for closing tags has to be the reverse of the opening order
                if (type == MarkerType.OPENING) {
                    return ordinalNumber - o.ordinalNumber
                } else {
                    return o.ordinalNumber - ordinalNumber
                }
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
