package com.vdurmont.emoji;

import java.util.ArrayList;
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
     * Replaces the emoji's unicode occurrences by one of their alias (between 2 ':').
     * Example: "😄" gives ":smile:"
     *
     * @param input the string to parse
     *
     * @return the string with the emojis replaced by their alias.
     */
    public static String parseToAliases(String input) {
        String result = input;
        for (Emoji emoji : EmojiManager.getAll()) {
            result = result.replace(emoji.getUnicode(), ":" + emoji.getAliases().get(0) + ":");
        }
        return result;
    }

    /**
     * Replaces the emoji's aliases (between 2 ':') occurrences and the html representations by their unicode.
     * Example: ":smile:" gives "😄"
     * "&amp;#128516;" gives "😄"
     *
     * @param input the string to parse
     *
     * @return the string with the aliases and html representations replaced by their unicode.
     */
    public static String parseToUnicode(String input) {
        // Get all the potential aliases
        List<String> aliases = getAliasesCandidates(input);

        // Replace the aliases by their unicode
        String result = input;
        for (String alias : aliases) {
            Emoji emoji = EmojiManager.getForAlias(alias);
            if (emoji != null) {
                result = result.replace(":" + alias + ":", emoji.getUnicode());
            }
        }

        // Replace the html
        for (Emoji emoji : EmojiManager.getAll()) {
            result = result.replace(emoji.getHtmlHexidecimal(), emoji.getUnicode());
            result = result.replace(emoji.getHtml(), emoji.getUnicode());
        }

        return result;
    }

    protected static List<String> getAliasesCandidates(String input) {
        List<String> candidates = new ArrayList<String>();
        String regex = "(?<=:)\\+?\\w+(?=:)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        matcher = matcher.useTransparentBounds(true);
        while (matcher.find()) {
            candidates.add(matcher.group());
        }
        return candidates;
    }

    /**
     * See {@link com.vdurmont.emoji.EmojiParser#parseToHtmlDecimal(String)}
     */
    @Deprecated
    public static String parseToHtml(String input) {
        return parseToHtmlDecimal(input);
    }

    /**
     * Replaces the emoji's unicode occurrences by their html representation.
     * Example: "😄" gives "&amp;#128516;"
     *
     * @param input the string to parse
     *
     * @return the string with the emojis replaced by their html decimal representation.
     */
    public static String parseToHtmlDecimal(String input) {
        String result = input;
        for (Emoji emoji : EmojiManager.getAll()) {
            result = result.replace(emoji.getUnicode(), emoji.getHtmlDecimal());
        }
        return result;
    }

    /**
     * Replaces the emoji's unicode occurrences by their html hex representation.
     * Example: "?" gives "&amp;#x1f064;"
     *
     * @param input the string to parse
     *
     * @return the string with the emojis replaced by their html hex representation.
     */
    public static String parseToHtmlHexadecimal(String input) {
        String result = input;
        for (Emoji emoji : EmojiManager.getAll()) {
            result = result.replace(emoji.getUnicode(), emoji.getHtmlHexidecimal());
        }
        return result;
    }
}
