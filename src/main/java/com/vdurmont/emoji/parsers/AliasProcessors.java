package com.vdurmont.emoji.parsers;

import com.vdurmont.emoji.Emoji;

/**
 * Contains built-in AliasProcessors.
 */
public class AliasProcessors {
    /**
     * Singleton class.
     */
    private AliasProcessors() { }

    /**
     * Provides the alias processing implementation for {@link com.vdurmont.emoji.EmojiParser#parseToUnicode(String)}
     */
    public static final AliasProcessor TO_UNICODE = new AliasProcessor() {
        @Override
        public String apply(AliasCandidate alias, Emoji emoji) {
            if (emoji != null) {
                boolean aliasHasFitzpatrick = alias.fitzpatrick != null;
                if(!emoji.supportsFitzpatrick() && aliasHasFitzpatrick) {
                    return null;
                } else {
                    return emoji.getUnicode() + (aliasHasFitzpatrick ? alias.fitzpatrick.unicode : "");
                }
            }

            return null;
        }
    };
}
