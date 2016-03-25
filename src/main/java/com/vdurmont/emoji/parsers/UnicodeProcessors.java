package com.vdurmont.emoji.parsers;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiParser.FitzpatrickAction;

import java.util.Collection;

/**
 * Contains built-in UnicodeProcessors.
 */
public class UnicodeProcessors {
    /**
     * Singleton class
     */
    private UnicodeProcessors() { }

    /**
     * Provides the implementation for {@link com.vdurmont.emoji.EmojiParser#removeAllEmojis(String)}
     */
    public static final UnicodeProcessor REMOVE_ALL_EMOJI = new UnicodeProcessor() {
        @Override
        public String apply(UnicodeCandidate input, FitzpatrickAction fitzpatrickAction) {
            return "";
        }

        @Override
        public boolean shouldRemoveFitzpatrick(FitzpatrickAction fitzpatrickAction) {
            return true;
        }
    };

    /**
     * Generates an implementation for {@link com.vdurmont.emoji.EmojiParser#removeAllEmojisExcept(String, Collection)}
     * @param emojisToKeep which emoji to NOT filter out
     * @return a UnicodeProcessor that removes all emoji except those specified in <code>emojisToKeep</code>
     */
    public static UnicodeProcessor removeAllExcept(final Collection<Emoji> emojisToKeep) {
        return new UnicodeProcessor() {
            @Override
            public String apply(UnicodeCandidate input, FitzpatrickAction fitzpatrickAction) {
                return emojisToKeep.contains(input.emoji) ? input.getEmojiUnicodeWithFitzpatrick() : "";
            }

            @Override
            public boolean shouldRemoveFitzpatrick(FitzpatrickAction fitzpatrickAction) {
                return false;
            }
        };
    }

    /**
     * Generates an implementation for {@link com.vdurmont.emoji.EmojiParser#removeEmojis(String, Collection)}
     * @param emojisToRemove which emoji to filter out
     * @return a UnicodeProcessor that removes all emoji except those specified in <code>emojisToRemove</code>
     */
    public static UnicodeProcessor removeOnly(final Collection<Emoji> emojisToRemove) {
        return new UnicodeProcessor() {
            @Override
            public String apply(UnicodeCandidate input, FitzpatrickAction fitzpatrickAction) {
                return emojisToRemove.contains(input.emoji) ? "" : input.getEmojiUnicodeWithFitzpatrick();
            }

            @Override
            public boolean shouldRemoveFitzpatrick(FitzpatrickAction fitzpatrickAction) {
                return false;
            }
        };
    }

    /**
     * Provides the implementations of {@link com.vdurmont.emoji.EmojiParser#parseToAliases(String)} and
     * {@link com.vdurmont.emoji.EmojiParser#parseToAliases(String, FitzpatrickAction)}
     */
    public static final UnicodeProcessor TO_ALIAS = new UnicodeProcessor() {
        @Override
        public String apply(UnicodeCandidate input, FitzpatrickAction fitzpatrickAction) {
            String inputFitzpatrick = input.hasFitzpatrick() ? input.fitzpatrick.unicode : "";
            String alias            = input.emoji.getAliases().get(0);

            if(fitzpatrickAction == FitzpatrickAction.PARSE && input.emoji.supportsFitzpatrick()) {
                if (input.hasFitzpatrick()) {
                    return String.format(
                            ":%s|%s:",
                            alias,
                            input.fitzpatrick.name().toLowerCase());
                }
            }

            if(fitzpatrickAction == FitzpatrickAction.IGNORE) {
                return String.format(":%s:%s", alias, inputFitzpatrick);
            } else {
                return ":" + alias + ":";
            }
        }

        @Override
        public boolean shouldRemoveFitzpatrick(FitzpatrickAction fitzpatrickAction) {
            return fitzpatrickAction == FitzpatrickAction.REMOVE;
        }
    };

    /**
     * Provides the implementations of {@link com.vdurmont.emoji.EmojiParser#parseToHtmlHexadecimal(String)} and
     * {@link com.vdurmont.emoji.EmojiParser#parseToHtmlHexadecimal(String, FitzpatrickAction)}
     */
    public static final UnicodeProcessor TO_HTML_HEX = new UnicodeProcessor() {
        @Override
        public String apply(UnicodeCandidate input, FitzpatrickAction fitzpatrickAction) {
            String hex = input.emoji.getHtmlHexadecimal();
            if(fitzpatrickAction == FitzpatrickAction.IGNORE && input.fitzpatrick != null) {
                return hex + input.fitzpatrick.unicode;
            }
            return hex;
        }

        @Override
        public boolean shouldRemoveFitzpatrick(FitzpatrickAction fitzpatrickAction) {
            return fitzpatrickAction != FitzpatrickAction.IGNORE;
        }
    };

    /**
     * Provides the implementations of {@link com.vdurmont.emoji.EmojiParser#parseToHtmlDecimal(String)} and
     * {@link com.vdurmont.emoji.EmojiParser#parseToHtmlDecimal(String, FitzpatrickAction)}
     */
    public static final UnicodeProcessor TO_HTML_DEC = new UnicodeProcessor() {
        @Override
        public String apply(UnicodeCandidate input, FitzpatrickAction fitzpatrickAction) {
            String dec = input.emoji.getHtmlDecimal();
            if(fitzpatrickAction == FitzpatrickAction.IGNORE && input.fitzpatrick != null) {
                return dec + input.fitzpatrick.unicode;
            }
            return dec;
        }

        @Override
        public boolean shouldRemoveFitzpatrick(FitzpatrickAction fitzpatrickAction) {
            return fitzpatrickAction != FitzpatrickAction.IGNORE;
        }
    };
}
