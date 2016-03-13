package com.vdurmont.emoji;

import java.util.ArrayList;
import java.util.Arrays;
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
    private static final Pattern ALIAS_CANDIDATE_PATTERN = Pattern.compile("(?<=:)\\+?(\\w|\\||\\-)+(?=:)");
    private static final Pattern ALL_FITZPATRICK_CODES;

    static {
        String fitzpatrickRegexString = "";
        for(Fitzpatrick fitzpatrick : Fitzpatrick.values()) {
            fitzpatrickRegexString += "|" + fitzpatrick.unicode;
        }
        ALL_FITZPATRICK_CODES = Pattern.compile("(?:" + fitzpatrickRegexString.substring(1) + ")");
    }

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
        if(fitzpatrickAction == FitzpatrickAction.REMOVE) {
            input = removeFitzpatrick(input);
        }

        int prev = 0;
        StringBuilder sb = new StringBuilder();
        List<UnicodeCandidate> replacements = getUnicodeCandidates(input);
        for(UnicodeCandidate candidate : replacements) {
            sb.append(input.substring(prev, candidate.startIndex));

            if(fitzpatrickAction == FitzpatrickAction.PARSE && candidate.emoji.supportsFitzpatrick()) {
                if (candidate.fitzpatrick != null) {
                    sb.append(":").append(candidate.emoji.getAliases().get(0)).append("|");
                    sb.append(candidate.fitzpatrick.name().toLowerCase()).append(":");
                    prev = candidate.getEndIndexWithFitzpatrick();
                    continue;
                }
            }

            sb.append(":").append(candidate.emoji.getAliases().get(0)).append(":");
            prev = candidate.endIndex;
        }

        return sb.append(input.substring(prev)).toString();
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

        Matcher matcher = ALIAS_CANDIDATE_PATTERN.matcher(input);
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
        if(fitzpatrickAction != FitzpatrickAction.IGNORE) {
            input = removeFitzpatrick(input);
        }

        int prev = 0;
        StringBuilder sb = new StringBuilder();
        List<UnicodeCandidate> replacements = getUnicodeCandidates(input);
        for(UnicodeCandidate candidate : replacements) {
            sb.append(input.substring(prev, candidate.startIndex)).append(candidate.emoji.getHtmlDecimal());
            prev = candidate.endIndex;
        }

        return sb.append(input.substring(prev)).toString();
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
        if(fitzpatrickAction != FitzpatrickAction.IGNORE) {
            input = removeFitzpatrick(input);
        }

        int prev = 0;
        StringBuilder sb = new StringBuilder();
        List<UnicodeCandidate> replacements = getUnicodeCandidates(input);
        for(UnicodeCandidate candidate : replacements) {
            sb.append(input.substring(prev, candidate.startIndex)).append(candidate.emoji.getHtmlHexidecimal());
            prev = candidate.endIndex;
        }

        return sb.append(input.substring(prev)).toString();
    }

    private static String removeFitzpatrick(String input) {
        return ALL_FITZPATRICK_CODES.matcher(input).replaceAll("");
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
        str = removeFitzpatrick(str);

        // Remove all emojis
        int prev = 0;
        StringBuilder sb = new StringBuilder();
        List<UnicodeCandidate> replacements = getUnicodeCandidates(str);
        for(UnicodeCandidate candidate : replacements) {
            sb.append(str.substring(prev, candidate.startIndex));
            prev = candidate.endIndex;
        }

        return sb.append(str.substring(prev)).toString();
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
        int prev = 0;
        StringBuilder sb = new StringBuilder();
        List<UnicodeCandidate> replacements = getUnicodeCandidates(str);
        for(UnicodeCandidate candidate : replacements) {
            sb.append(str.substring(prev, candidate.startIndex));
            prev = candidate.getEndIndexWithFitzpatrick();

            if (!emojisToRemove.contains(candidate.emoji)) {
                sb.append(candidate.getEmojiUnicodeWithFitzpatrick());
            }
        }

        return sb.append(str.substring(prev)).toString();
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
        int prev = 0;
        StringBuilder sb = new StringBuilder();
        List<UnicodeCandidate> replacements = getUnicodeCandidates(str);
        for(UnicodeCandidate candidate : replacements) {
            sb.append(str.substring(prev, candidate.startIndex));
            prev = candidate.getEndIndexWithFitzpatrick();

            if (emojisToKeep.contains(candidate.emoji)) {
                sb.append(candidate.getEmojiUnicodeWithFitzpatrick());
            }
        }

        return sb.append(str.substring(prev)).toString();
    }


    /**
     * Generates a list UnicodeCandidates found in input string. A UnicodeCandidate is created for every
     * unicode emoticon found in input string, additionally if Fitzpatrick modifier follows the emoji, it
     * is included in UnicodeCandidate. Finally, it contains start and end index of unicode emoji itself
     * (WITHOUT Fitzpatrick modifier whether it is there or not!).
     * @param input String to find all unicode emojis in
     * @return List of UnicodeCandidates for each unicode emote in text
     */
    private static List<UnicodeCandidate> getUnicodeCandidates(String input) {
        char[] inputCharArray = input.toCharArray();
        List<UnicodeCandidate> candidates = new ArrayList<UnicodeCandidate>();
        for(int i=0; i<input.length(); i++) {
            int emojiEnd = getEmojiEndPos(inputCharArray, i);

            if (emojiEnd != -1) {
                String emojiString = input.substring(i, emojiEnd);
                String fitzpatrickString = (emojiEnd+2 <= input.length()) ? input.substring(emojiEnd, emojiEnd+2) : null;
                candidates.add(new UnicodeCandidate(emojiString, fitzpatrickString, i, emojiEnd));
                i = emojiEnd;
            }
        }
        return candidates;
    }


    /**
     * Returns end index of a unicode emoji if it is found in text starting at index startPos, -1 if not found.
     * This returns the longest matching emoji, for example, in "\uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC66"
     * it will find alias:family_man_woman_boy, NOT alias:man
     */
    private static int getEmojiEndPos(char[] text, int startPos) {
        int best = -1;
        for(int j=startPos+1; j<=text.length; j++) {
            EmojiTrie.Matches status = EmojiManager.isEmoji(Arrays.copyOfRange(text, startPos, j));

            if (status.exactMatch()) {
                best = j;
            } else if (status.impossibleMatch()) {
                return best;
            }
        }

        return best;
    }



    protected static class UnicodeCandidate {
        public final Emoji emoji;
        public final Fitzpatrick fitzpatrick;
        public final int startIndex, endIndex;

        private UnicodeCandidate(String emoji, String fitzpatrick, int startIndex, int endIndex) {
            this.emoji = EmojiManager.getByUnicode(emoji);
            this.fitzpatrick = Fitzpatrick.fitzpatrickFromUnicode(fitzpatrick);
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        public String getEmojiUnicodeWithFitzpatrick() {
            if(emoji.supportsFitzpatrick()) return emoji.getUnicode(fitzpatrick);
            return emoji.getUnicode();
        }

        public int getEndIndexWithFitzpatrick() {
            if(fitzpatrick != null) return endIndex+2;
            return endIndex;
        }
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
                this.fitzpatrick = Fitzpatrick.fitzpatrickFromType(fitzpatrickString);
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
