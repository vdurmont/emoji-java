package com.vdurmont.emoji.parsers;

import com.vdurmont.emoji.EmojiParser.FitzpatrickAction;

/**
 * Defines a type which can perform string substitutions on detected unicode emoji.
 */
public interface UnicodeProcessor {
    /**
     * A function which takes an emoji identified in the corpus and replaces it with whatever.
     * @param input the identified emoji
     * @param fitzpatrickAction the {@link FitzpatrickAction} that the caller requested.
     * @return a String with which to replace the identified emoji with.
     */
    String apply(UnicodeCandidate input, FitzpatrickAction fitzpatrickAction);

    /**
     * Whether or not to remove all Fitzpatrick modifiers from the corpus before
     * processing with this UnicodeProcessor
     * @param fitzpatrickAction the {@link FitzpatrickAction} that the caller requested.
     * @return whether or not to strip all Fitzpatrick modifiers before processing the corpus.
     */
    boolean shouldRemoveFitzpatrick(FitzpatrickAction fitzpatrickAction);
}
