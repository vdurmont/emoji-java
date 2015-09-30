package com.vdurmont.emoji;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides methods to parse strings with emojis.
 *
 * @author Vincent DURMONT [vdurmont@gmail.com]
 */
public class EmojiParser {
    /**
     * No need for a constructor, all the methods are static.
     */
    private EmojiParser() {}

    /**
     * See {@link #parseToAliases(String, FitzpatrickAction)} with the action "PARSE"
     *
     * @param input the string to parse
     *
     * @return the string with the emojis replaced by their alias.
     */
    public static String parseToAliases(String input) {
        return parseToAliases(input, FitzpatrickAction.PARSE);
    }

    /**
     * Replaces the emoji's unicode occurrences by one of their alias (between 2 ':').<br>
     * Example: <code>😄</code> will be replaced by <code>:smile:</code><br>
     * <br>
     * When a fitzpatrick modifier is present with a PARSE action, a "|" will be appendend to the alias, with the fitzpatrick type.<br>
     * Example: <code>👦🏿</code> will be replaced by <code>:boy|type_6:</code><br>
     * The fitzpatrick types are: type_1_2, type_3, type_4, type_5, type_6<br>
     * <br>
     * When a fitzpatrick modifier is present with a REMOVE action, the modifier will be deleted.<br>
     * Example: <code>👦🏿</code> will be replaced by <code>:boy:</code><br>
     * <br>
     * When a fitzpatrick modifier is present with a IGNORE action, the modifier will be ignored.<br>
     * Example: <code>👦🏿</code> will be replaced by <code>:boy:🏿</code><br>
     *
     * @param input             the string to parse
     * @param fitzpatrickAction the action to apply for the fitzpatrick modifiers
     *
     * @return the string with the emojis replaced by their alias.
     */
    public static String parseToAliases(String input, FitzpatrickAction fitzpatrickAction) {
        String result = prepareParsing(input, fitzpatrickAction);

        for (Emoji emoji : EmojiManager.getAll()) {
            if (fitzpatrickAction != FitzpatrickAction.REMOVE) {
                if (emoji.supportsFitzpatrick()) {
                    if (fitzpatrickAction == FitzpatrickAction.PARSE) {
                        for (Fitzpatrick fitzpatrick : Fitzpatrick.values()) {
                            String replacement = ":" + emoji.getAliases().get(0) + "|" + fitzpatrick.toString().toLowerCase() + ":";
                            result = result.replace(emoji.getUnicode(fitzpatrick), replacement);
                        }
                    }
                }
            }
            result = result.replace(emoji.getUnicode(), ":" + emoji.getAliases().get(0) + ":");
        }
        return result;
    }

    /**
     * Replaces the emoji's aliases (between 2 ':') occurrences and the html representations by their unicode.<br>
     * Examples:<br>
     * <code>:smile:</code> will be replaced by <code>😄</code><br>
     * <code>&amp;#128516;</code> will be replaced by <code>😄</code><br>
     * <code>:boy|type_6:</code> will be replaced by <code>👦🏿</code>
     *
     * @param input the string to parse
     *
     * @return the string with the aliases and html representations replaced by their unicode.
     */
    public static String parseToUnicode(String input) {
        // Get all the potential aliases
        List<AliasCandidate> candidates = getAliasCandidates(input);

        // Replace the aliases by their unicode
        String result = input;
        for (AliasCandidate candidate : candidates) {
            Emoji emoji = EmojiManager.getForAlias(candidate.alias);
            if (emoji != null) {
                if (emoji.supportsFitzpatrick() || (!emoji.supportsFitzpatrick() && candidate.fitzpatrick == null)) {
                    String replacement = emoji.getUnicode();
                    if (candidate.fitzpatrick != null) {
                        replacement += candidate.fitzpatrick.unicode;
                    }
                    result = result.replace(":" + candidate.fullString + ":", replacement);
                }
            }
        }

        // Replace the html
        for (Emoji emoji : EmojiManager.getAll()) {
            result = result.replace(emoji.getHtmlHexidecimal(), emoji.getUnicode());
            result = result.replace(emoji.getHtmlDecimal(), emoji.getUnicode());
        }

        return result;
    }

    protected static List<AliasCandidate> getAliasCandidates(String input) {
        List<AliasCandidate> candidates = new ArrayList<AliasCandidate>();
        String regex = "(?<=:)\\+?(\\w|\\||\\-)+(?=:)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        matcher = matcher.useTransparentBounds(true);
        while (matcher.find()) {
            String match = matcher.group();
            if (!match.contains("|")) {
                candidates.add(new AliasCandidate(match, match, null));
            } else {
                String[] splitted = match.split("\\|");
                if (splitted.length == 2 || splitted.length > 2) {
                    candidates.add(new AliasCandidate(match, splitted[0], splitted[1]));
                } else {
                    candidates.add(new AliasCandidate(match, match, null));
                }
            }
        }
        return candidates;
    }

    /**
     * See {@link #parseToHtmlDecimal(String, FitzpatrickAction)} with the action "PARSE"
     *
     * @param input the string to parse
     *
     * @return the string with the emojis replaced by their html decimal representation.
     */
    public static String parseToHtmlDecimal(String input) {
        return parseToHtmlDecimal(input, FitzpatrickAction.PARSE);
    }

    /**
     * Replaces the emoji's unicode occurrences by their html representation.<br>
     * Example: <code>😄</code> will be replaced by <code>&amp;#128516;</code><br>
     * <br>
     * When a fitzpatrick modifier is present with a PARSE or REMOVE action, the modifier will be deleted from the string.<br>
     * Example: <code>👦🏿</code> will be replaced by <code>&amp;#128102;</code><br>
     * <br>
     * When a fitzpatrick modifier is present with a IGNORE action, the modifier will be ignored and will remain in the string.<br>
     * Example: <code>👦🏿</code> will be replaced by <code>&amp;#128102;🏿</code>
     *
     * @param input             the string to parse
     * @param fitzpatrickAction the action to apply for the fitzpatrick modifiers
     *
     * @return the string with the emojis replaced by their html decimal representation.
     */
    public static String parseToHtmlDecimal(String input, FitzpatrickAction fitzpatrickAction) {
        String result = prepareParsing(input, fitzpatrickAction);

        for (Emoji emoji : EmojiManager.getAll()) {
            if (fitzpatrickAction != FitzpatrickAction.REMOVE) {
                if (emoji.supportsFitzpatrick()) {
                    for (Fitzpatrick fitzpatrick : Fitzpatrick.values()) {
                        String search = fitzpatrickAction == FitzpatrickAction.PARSE ? emoji.getUnicode(fitzpatrick) : emoji.getUnicode();
                        result = result.replace(search, emoji.getHtmlDecimal());
                    }
                }
            }
            result = result.replace(emoji.getUnicode(), emoji.getHtmlDecimal());
        }
        return result;
    }

    /**
     * See {@link #parseToHtmlHexadecimal(String, FitzpatrickAction)} with the action "PARSE"
     *
     * @param input the string to parse
     *
     * @return the string with the emojis replaced by their html hex representation.
     */
    public static String parseToHtmlHexadecimal(String input) {
        return parseToHtmlHexadecimal(input, FitzpatrickAction.PARSE);
    }

    /**
     * Replaces the emoji's unicode occurrences by their html hex representation.<br>
     * Example: <code>👦</code> will be replaced by <code>&amp;#x1f466;</code><br>
     * <br>
     * When a fitzpatrick modifier is present with a PARSE or REMOVE action, the modifier will be deleted.<br>
     * Example: <code>👦🏿</code> will be replaced by <code>&amp;#x1f466;</code><br>
     * <br>
     * When a fitzpatrick modifier is present with a IGNORE action, the modifier will be ignored and will remain in the string.<br>
     * Example: <code>👦🏿</code> will be replaced by <code>&amp;#x1f466;🏿</code>
     *
     * @param input             the string to parse
     * @param fitzpatrickAction the action to apply for the fitzpatrick modifiers
     *
     * @return the string with the emojis replaced by their html hex representation.
     */
    public static String parseToHtmlHexadecimal(String input, FitzpatrickAction fitzpatrickAction) {
        String result = prepareParsing(input, fitzpatrickAction);

        for (Emoji emoji : EmojiManager.getAll()) {
            if (fitzpatrickAction != FitzpatrickAction.REMOVE) {
                if (emoji.supportsFitzpatrick()) {
                    for (Fitzpatrick fitzpatrick : Fitzpatrick.values()) {
                        String search = fitzpatrickAction == FitzpatrickAction.PARSE ? emoji.getUnicode(fitzpatrick) : emoji.getUnicode();
                        result = result.replace(search, emoji.getHtmlHexidecimal());
                    }
                }
            }
            result = result.replace(emoji.getUnicode(), emoji.getHtmlHexidecimal());
        }
        return result;
    }

    private static String prepareParsing(String input, FitzpatrickAction fitzpatrickAction) {
        if (fitzpatrickAction == FitzpatrickAction.REMOVE) {
            for (Fitzpatrick fitzpatrick : Fitzpatrick.values()) {
                input = input.replace(fitzpatrick.unicode, "");
            }
        }
        return input;
    }

    /**
     * Removes all emojis from a String
     *
     * @param str the string to process
     *
     * @return the string without any emoji
     */
    public static String removeAllEmojis(String str) {
        // Remove all fitzpatrick modifiers
        for (Fitzpatrick fitzpatrick : Fitzpatrick.values()) {
            str = str.replaceAll(fitzpatrick.unicode, "");
        }
        // Remove all emojis
        for (Emoji emoji : EmojiManager.getAll()) {
            str = str.replaceAll(emoji.getUnicode(), "");
        }

        return str;
    }

    /**
     * Removes a set of emojis from a String
     *
     * @param str            the string to process
     * @param emojisToRemove the emojis to remove from this string
     *
     * @return the string without the emojis that were removed
     */
    public static String removeEmojis(String str, Collection<Emoji> emojisToRemove) {
        for (Emoji emoji : emojisToRemove) {
            if (emoji.supportsFitzpatrick()) {
                for (Fitzpatrick fitzpatrick : Fitzpatrick.values()) {
                    str = str.replaceAll(emoji.getUnicode(fitzpatrick), "");
                }
            }
            str = str.replaceAll(emoji.getUnicode(), "");
        }

        return str;
    }

    /**
     * Removes all the emojis in a String except a provided set
     *
     * @param str          the string to process
     * @param emojisToKeep the emojis to keep in this string
     *
     * @return the string without the emojis that were removed
     */
    public static String removeAllEmojisExcept(String str, Collection<Emoji> emojisToKeep) {
        for (Emoji emoji : EmojiManager.getAll()) {
            if (!emojisToKeep.contains(emoji)) {
                if (emoji.supportsFitzpatrick()) {
                    for (Fitzpatrick fitzpatrick : Fitzpatrick.values()) {
                        str = str.replaceAll(emoji.getUnicode(fitzpatrick), "");
                    }
                }
                str = str.replaceAll(emoji.getUnicode(), "");
            }
        }

        return str;
    }

    protected static class AliasCandidate {
        public final String fullString;
        public final String alias;
        public final Fitzpatrick fitzpatrick;

        private AliasCandidate(String fullString, String alias, String fitzpatrickString) {
            this.fullString = fullString;
            this.alias = alias;
            if (fitzpatrickString == null) {
                this.fitzpatrick = null;
            } else {
                this.fitzpatrick = resolveFitzpatrick(fitzpatrickString);
            }
        }

        private static Fitzpatrick resolveFitzpatrick(String fitzpatrickString) {
            try {
                return Fitzpatrick.valueOf(fitzpatrickString.toUpperCase());
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }

    /**
     * Enum used to indicate what should be done when a Fitzpatrick modifier is found.
     */
    public enum FitzpatrickAction {
        /**
         * Tries to match the Fitzpatrick modifier with the previous emoji
         */
        PARSE,

        /**
         * Removes the Fitzpatrick modifier from the string
         */
        REMOVE,

        /**
         * Ignores the Fitzpatrick modifier (it will stay in the string)
         */
        IGNORE
    }
}
