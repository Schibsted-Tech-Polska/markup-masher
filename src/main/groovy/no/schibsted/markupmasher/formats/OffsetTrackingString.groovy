package no.schibsted.markupmasher.formats

import groovy.transform.TupleConstructor

/**
 * Represents a String which can have certain parts replaced, but if you refer
 * to specific offsets, you will always reach the same parts as if you operated
 * on the original string.
 */
class OffsetTrackingString {
    static final ORIGINAL_TEXT = [:]
    final private List<Segment> segments

    OffsetTrackingString(String original) {
        this.segments = [new Segment(original, true)]
    }
    private OffsetTrackingString(List<Segment> segments) {
        this.segments = segments
    }

    String toString() { segments.collect { it.content }.join('') }

    OffsetTrackingString replace(int start, int length, List newContent) {
        // find the right segment and offset within it
        def location = findOffset(start)
        if (!location)
            return this

        Segment segment = location.segment
        int adjustedOffset = location.adjustedOffset
        String content = segment.content

        // check if we didn't cross segments
        if (adjustedOffset + length > segment.content.length()) {
            // ignore this change, it crosses segments
            return this
        }

        // slice the existing segments
        String pre = content.substring(0, adjustedOffset),
            inside = content.substring(adjustedOffset, adjustedOffset + length),
            post = content.substring(adjustedOffset + length)

        // replace the existing segment with a new list
        List replacedSegs = newContent.collect { text ->
            if (text == ORIGINAL_TEXT) {
                return new Segment(inside, true)
            } else {
                return new Segment(text, false)
            }
        }
        List newSegs = [new Segment(pre, true)] + replacedSegs + [new Segment(post, true)]

        // create a new offset string
        int replacementIndex = segments.indexOf(segment)
        return new OffsetTrackingString(
            segments.subList(0, replacementIndex) +
            newSegs +
            segments.subList(replacementIndex+1, segments.size())
        )
    }

    def findOffset(int offset) {
        int totalBefore = 0
        Segment seg = segments.find { seg ->
            if (!seg.original)
                return false

            if (offset - totalBefore < seg.content.length()) {
                return true
            } else {
                totalBefore += seg.content.length()
                return false
            }
        }
        return [segment: seg, adjustedOffset: offset - totalBefore]
    }

    @TupleConstructor
    private static class Segment {
        final String content
        final boolean original
    }
}
