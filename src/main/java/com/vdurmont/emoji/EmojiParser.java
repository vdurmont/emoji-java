package com.vdurmont.emoji;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Provides methods to parse strings with emojis.
 *
 * @author Vincent DURMONT [vdurmont@gmail.com]
 */
public class EmojiParser {

  /**
   * See {@link #parseToAliases(String, FitzpatrickAction)} with the action
   * "PARSE"
   *
   * @param input the string to parse
   *
   * @return the string with the emojis replaced by their alias.
   */
  public static String parseToAliases(String input) {
    return parseToAliases(input, FitzpatrickAction.PARSE);
  }

  /**
   * Replaces the emoji's unicode occurrences by one of their alias
   * (between 2 ':').<br>
   * Example: <code>😄</code> will be replaced by <code>:smile:</code><br>
   * <br>
   * When a fitzpatrick modifier is present with a PARSE action, a "|" will be
   * appendend to the alias, with the fitzpatrick type.<br>
   * Example: <code>👦🏿</code> will be replaced by
   * <code>:boy|type_6:</code><br>
   * The fitzpatrick types are: type_1_2, type_3, type_4, type_5, type_6<br>
   * <br>
   * When a fitzpatrick modifier is present with a REMOVE action, the modifier
   * will be deleted.<br>
   * Example: <code>👦🏿</code> will be replaced by <code>:boy:</code><br>
   * <br>
   * When a fitzpatrick modifier is present with a IGNORE action, the modifier
   * will be ignored.<br>
   * Example: <code>👦🏿</code> will be replaced by <code>:boy:🏿</code><br>
   *
   * @param input             the string to parse
   * @param fitzpatrickAction the action to apply for the fitzpatrick modifiers
   *
   * @return the string with the emojis replaced by their alias.
   */
  public static String parseToAliases(
    String input,
    final FitzpatrickAction fitzpatrickAction
  ) {
    EmojiTransformer emojiTransformer = new EmojiTransformer() {
      public String transform(UnicodeCandidate unicodeCandidate) {
        switch (fitzpatrickAction) {
          default:
          case PARSE:
            if (unicodeCandidate.hasFitzpatrick()) {
              return ":" +
                unicodeCandidate.getEmoji().getAliases().get(0) +
                "|" +
                unicodeCandidate.getFitzpatrickType() +
                ":";
            }
          case REMOVE:
            return ":" +
              unicodeCandidate.getEmoji().getAliases().get(0) +
              ":";
          case IGNORE:
            return ":" +
              unicodeCandidate.getEmoji().getAliases().get(0) +
              ":" +
              unicodeCandidate.getFitzpatrickUnicode();
        }
      }
    };

    return parseFromUnicode(input, emojiTransformer);
  }

  /**
   * Replace all emojis with character
   *
   * @param str the string to process
   * @param replacementString replacement the string that will replace all the emojis
   * @return the string with replaced character
   */
  public static String replaceAllEmojis(String str, final String replacementString) {
    EmojiParser.EmojiTransformer emojiTransformer = new EmojiParser.EmojiTransformer() {
      public String transform(EmojiParser.UnicodeCandidate unicodeCandidate) {
        return replacementString;
      }
    };

    return parseFromUnicode(str, emojiTransformer);
  }


  /**
   * Replaces the emoji's aliases (between 2 ':') occurrences and the html
   * representations by their unicode.<br>
   * Examples:<br>
   * <code>:smile:</code> will be replaced by <code>😄</code><br>
   * <code>&amp;#128516;</code> will be replaced by <code>😄</code><br>
   * <code>:boy|type_6:</code> will be replaced by <code>👦🏿</code>
   *
   * @param input the string to parse
   *
   * @return the string with the aliases and html representations replaced by
   * their unicode.
   */
  public static String parseToUnicode(String input) {
    StringBuilder sb = new StringBuilder(input.length());

    for (int last = 0; last < input.length(); last++) {
      AliasCandidate alias = getAliasAt(input, last);
      if (alias == null) {
          alias = getHtmlEncodedEmojiAt(input, last);
      }

      if (alias != null) {
        sb.append(alias.emoji.getUnicode());
        last = alias.endIndex;

        if (alias.fitzpatrick != null) {
          sb.append(alias.fitzpatrick.unicode);
        }
      } else {
        sb.append(input.charAt(last));
      }
    }

    return sb.toString();
  }

  /** Finds the alias in the given string starting at the given point, null otherwise */
  protected static AliasCandidate getAliasAt(String input, int start) {
    if (input.length() < start + 2 || input.charAt(start) != ':') return null; // Aliases start with :
    int aliasEnd = input.indexOf(':', start + 2);  // Alias must be at least 1 char in length
    if (aliasEnd == -1) return null; // No alias end found

    int fitzpatrickStart = input.indexOf('|', start + 2);
    if (fitzpatrickStart != -1 && fitzpatrickStart < aliasEnd) {
      Emoji emoji = EmojiManager.getForAlias(input.substring(start, fitzpatrickStart));
      if (emoji == null) return null; // Not a valid alias
      if (!emoji.supportsFitzpatrick()) return null; // Fitzpatrick was specified, but the emoji does not support it
      Fitzpatrick fitzpatrick = Fitzpatrick.fitzpatrickFromType(input.substring(fitzpatrickStart + 1, aliasEnd));
      return new AliasCandidate(emoji, fitzpatrick, start, aliasEnd);
    }

    Emoji emoji = EmojiManager.getForAlias(input.substring(start, aliasEnd));
    if (emoji == null) return null; // Not a valid alias
    return new AliasCandidate(emoji, null, start, aliasEnd);
  }

  /** Finds the HTML encoded emoji in the given string starting at the given point, null otherwise */
  protected static AliasCandidate getHtmlEncodedEmojiAt(String input, int start) {
    if (input.length() < start + 4 || input.charAt(start) != '&' || input.charAt(start + 1) != '#') return null;

    Emoji longestEmoji = null;
    int longestCodePointEnd = -1;
    char[] chars = new char[EmojiManager.EMOJI_TRIE.maxDepth];
    int charsIndex = 0;
    int codePointStart = start;
    do {
      int codePointEnd = input.indexOf(';', codePointStart + 3);  // Code point must be at least 1 char in length
      if (codePointEnd == -1) break;

      try {
        int radix = input.charAt(codePointStart + 2) == 'x' ? 16 : 10;
        int codePoint = Integer.parseInt(input.substring(codePointStart + 2 + radix / 16, codePointEnd), radix);
        charsIndex += Character.toChars(codePoint, chars, charsIndex);
      } catch (NumberFormatException e) {
        break;
      } catch (IllegalArgumentException e) {
        break;
      }
      Emoji foundEmoji = EmojiManager.EMOJI_TRIE.getEmoji(chars, 0, charsIndex);
      if (foundEmoji != null) {
        longestEmoji = foundEmoji;
        longestCodePointEnd = codePointEnd;
      }
      codePointStart = codePointEnd + 1;
    } while (input.length() > codePointStart + 4 &&
            input.charAt(codePointStart) == '&' &&
            input.charAt(codePointStart + 1) == '#' &&
            charsIndex < chars.length &&
            !EmojiManager.EMOJI_TRIE.isEmoji(chars, 0, charsIndex).impossibleMatch());

    if (longestEmoji == null) return null;
    return new AliasCandidate(longestEmoji, null, start, longestCodePointEnd);
  }

  /**
   * See {@link #parseToHtmlDecimal(String, FitzpatrickAction)} with the action
   * "PARSE"
   *
   * @param input the string to parse
   *
   * @return the string with the emojis replaced by their html decimal
   * representation.
   */
  public static String parseToHtmlDecimal(String input) {
    return parseToHtmlDecimal(input, FitzpatrickAction.PARSE);
  }

  /**
   * Replaces the emoji's unicode occurrences by their html representation.<br>
   * Example: <code>😄</code> will be replaced by <code>&amp;#128516;</code><br>
   * <br>
   * When a fitzpatrick modifier is present with a PARSE or REMOVE action, the
   * modifier will be deleted from the string.<br>
   * Example: <code>👦🏿</code> will be replaced by
   * <code>&amp;#128102;</code><br>
   * <br>
   * When a fitzpatrick modifier is present with a IGNORE action, the modifier
   * will be ignored and will remain in the string.<br>
   * Example: <code>👦🏿</code> will be replaced by
   * <code>&amp;#128102;🏿</code>
   *
   * @param input             the string to parse
   * @param fitzpatrickAction the action to apply for the fitzpatrick modifiers
   *
   * @return the string with the emojis replaced by their html decimal
   * representation.
   */
  public static String parseToHtmlDecimal(
    String input,
    final FitzpatrickAction fitzpatrickAction
  ) {
    EmojiTransformer emojiTransformer = new EmojiTransformer() {
      public String transform(UnicodeCandidate unicodeCandidate) {
        switch (fitzpatrickAction) {
          default:
          case PARSE:
          case REMOVE:
            return unicodeCandidate.getEmoji().getHtmlDecimal();
          case IGNORE:
            return unicodeCandidate.getEmoji().getHtmlDecimal() +
              unicodeCandidate.getFitzpatrickUnicode();
        }
      }
    };

    return parseFromUnicode(input, emojiTransformer);
  }

  /**
   * See {@link #parseToHtmlHexadecimal(String, FitzpatrickAction)} with the
   * action "PARSE"
   *
   * @param input the string to parse
   *
   * @return the string with the emojis replaced by their html hex
   * representation.
   */
  public static String parseToHtmlHexadecimal(String input) {
    return parseToHtmlHexadecimal(input, FitzpatrickAction.PARSE);
  }

  /**
   * Replaces the emoji's unicode occurrences by their html hex
   * representation.<br>
   * Example: <code>👦</code> will be replaced by <code>&amp;#x1f466;</code><br>
   * <br>
   * When a fitzpatrick modifier is present with a PARSE or REMOVE action, the
   * modifier will be deleted.<br>
   * Example: <code>👦🏿</code> will be replaced by
   * <code>&amp;#x1f466;</code><br>
   * <br>
   * When a fitzpatrick modifier is present with a IGNORE action, the modifier
   * will be ignored and will remain in the string.<br>
   * Example: <code>👦🏿</code> will be replaced by
   * <code>&amp;#x1f466;🏿</code>
   *
   * @param input             the string to parse
   * @param fitzpatrickAction the action to apply for the fitzpatrick modifiers
   *
   * @return the string with the emojis replaced by their html hex
   * representation.
   */
  public static String parseToHtmlHexadecimal(
    String input,
    final FitzpatrickAction fitzpatrickAction
  ) {
    EmojiTransformer emojiTransformer = new EmojiTransformer() {
      public String transform(UnicodeCandidate unicodeCandidate) {
        switch (fitzpatrickAction) {
          default:
          case PARSE:
          case REMOVE:
            return unicodeCandidate.getEmoji().getHtmlHexadecimal();
          case IGNORE:
            return unicodeCandidate.getEmoji().getHtmlHexadecimal() +
              unicodeCandidate.getFitzpatrickUnicode();
        }
      }
    };

    return parseFromUnicode(input, emojiTransformer);
  }

  /**
   * Removes all emojis from a String
   *
   * @param str the string to process
   *
   * @return the string without any emoji
   */
  public static String removeAllEmojis(String str) {
    EmojiTransformer emojiTransformer = new EmojiTransformer() {
      public String transform(UnicodeCandidate unicodeCandidate) {
        return "";
      }
    };

    return parseFromUnicode(str, emojiTransformer);
  }


  /**
   * Removes a set of emojis from a String
   *
   * @param str            the string to process
   * @param emojisToRemove the emojis to remove from this string
   *
   * @return the string without the emojis that were removed
   */
  public static String removeEmojis(
    String str,
    final Collection<Emoji> emojisToRemove
  ) {
    EmojiTransformer emojiTransformer = new EmojiTransformer() {
      public String transform(UnicodeCandidate unicodeCandidate) {
        if (!emojisToRemove.contains(unicodeCandidate.getEmoji())) {
          return unicodeCandidate.getEmoji().getUnicode() +
            unicodeCandidate.getFitzpatrickUnicode();
        }
        return "";
      }
    };

    return parseFromUnicode(str, emojiTransformer);
  }

  /**
   * Removes all the emojis in a String except a provided set
   *
   * @param str          the string to process
   * @param emojisToKeep the emojis to keep in this string
   *
   * @return the string without the emojis that were removed
   */
  public static String removeAllEmojisExcept(
    String str,
    final Collection<Emoji> emojisToKeep
  ) {
    EmojiTransformer emojiTransformer = new EmojiTransformer() {
      public String transform(UnicodeCandidate unicodeCandidate) {
        if (emojisToKeep.contains(unicodeCandidate.getEmoji())) {
          return unicodeCandidate.getEmoji().getUnicode() +
            unicodeCandidate.getFitzpatrickUnicode();
        }
        return "";
      }
    };

    return parseFromUnicode(str, emojiTransformer);
  }


  /**
   * Detects all unicode emojis in input string and replaces them with the
   * return value of transformer.transform()
   *
   * @param input the string to process
   * @param transformer emoji transformer to apply to each emoji
   *
   * @return input string with all emojis transformed
   */
  public static String parseFromUnicode(
    String input,
    EmojiTransformer transformer
  ) {
    int prev = 0;
    StringBuilder sb = new StringBuilder(input.length());
    List<UnicodeCandidate> replacements = getUnicodeCandidates(input);
    for (UnicodeCandidate candidate : replacements) {
      sb.append(input, prev, candidate.getEmojiStartIndex());

      sb.append(transformer.transform(candidate));
      prev = candidate.getFitzpatrickEndIndex();
    }

    return sb.append(input.substring(prev)).toString();
  }

  public static List<String> extractEmojis(String input) {
    List<UnicodeCandidate> emojis = getUnicodeCandidates(input);
    List<String> result = new ArrayList<String>();
    for (UnicodeCandidate emoji : emojis) {
      if (emoji.getEmoji().supportsFitzpatrick() && emoji.hasFitzpatrick()) {
        result.add(emoji.getEmoji().getUnicode(emoji.getFitzpatrick()));
      } else {
        result.add(emoji.getEmoji().getUnicode());
      }
    }
    return result;
  }


  /**
   * Generates a list UnicodeCandidates found in input string. A
   * UnicodeCandidate is created for every unicode emoticon found in input
   * string, additionally if Fitzpatrick modifier follows the emoji, it is
   * included in UnicodeCandidate. Finally, it contains start and end index of
   * unicode emoji itself (WITHOUT Fitzpatrick modifier whether it is there or
   * not!).
   *
   * @param input String to find all unicode emojis in
   * @return List of UnicodeCandidates for each unicode emote in text
   */
  protected static List<UnicodeCandidate> getUnicodeCandidates(String input) {
    char[] inputCharArray = input.toCharArray();
    List<UnicodeCandidate> candidates = new ArrayList<UnicodeCandidate>();
    UnicodeCandidate next;
    for (int i = 0; (next = getNextUnicodeCandidate(inputCharArray, i)) != null; i = next.getFitzpatrickEndIndex()) {
      candidates.add(next);
    }

    return candidates;
  }

  /**
   * Finds the next UnicodeCandidate after a given starting index
   *
   * @param chars char array to find UnicodeCandidate in
   * @param start starting index for search
   * @return the next UnicodeCandidate or null if no UnicodeCandidate is found after start index
   */
  protected static UnicodeCandidate getNextUnicodeCandidate(char[] chars, int start) {
    for (int i = start; i < chars.length; i++) {
      int emojiEnd = getEmojiEndPos(chars, i);

      if (emojiEnd != -1) {
        Emoji emoji = EmojiManager.getByUnicode(new String(chars, i, emojiEnd - i));
        String fitzpatrickString = (emojiEnd + 2 <= chars.length) ?
                new String(chars, emojiEnd, 2) :
                null;
        return new UnicodeCandidate(
                emoji,
                fitzpatrickString,
                i
        );
      }
    }

    return null;
  }


  /**
   * Returns end index of a unicode emoji if it is found in text starting at
   * index startPos, -1 if not found.
   * This returns the longest matching emoji, for example, in
   * "\uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC66"
   * it will find alias:family_man_woman_boy, NOT alias:man
   *
   * @param text the current text where we are looking for an emoji
   * @param startPos the position in the text where we should start looking for
   * an emoji end
   *
   * @return the end index of the unicode emoji starting at startPos. -1 if not
   * found
   */
  protected static int getEmojiEndPos(char[] text, int startPos) {
    int best = -1;
    for (int j = startPos + 1; j <= text.length; j++) {
      EmojiTrie.Matches status = EmojiManager.EMOJI_TRIE.isEmoji(text, startPos, j);

      if (status.exactMatch()) {
        best = j;
      } else if (status.impossibleMatch()) {
        return best;
      }
    }

    return best;
  }


  public static class UnicodeCandidate {
    private final Emoji emoji;
    private final Fitzpatrick fitzpatrick;
    private final int startIndex;

    private UnicodeCandidate(Emoji emoji, String fitzpatrick, int startIndex) {
      this.emoji = emoji;
      this.fitzpatrick = Fitzpatrick.fitzpatrickFromUnicode(fitzpatrick);
      this.startIndex = startIndex;
    }

    public Emoji getEmoji() {
      return emoji;
    }

    public boolean hasFitzpatrick() {
      return getFitzpatrick() != null;
    }

    public Fitzpatrick getFitzpatrick() {
      return fitzpatrick;
    }

    public String getFitzpatrickType() {
      return hasFitzpatrick() ? fitzpatrick.name().toLowerCase() : "";
    }

    public String getFitzpatrickUnicode() {
      return hasFitzpatrick() ? fitzpatrick.unicode : "";
    }

    public int getEmojiStartIndex() {
      return startIndex;
    }

    public int getEmojiEndIndex() {
      return startIndex + emoji.getUnicode().length();
    }

    public int getFitzpatrickEndIndex() {
      return getEmojiEndIndex() + (fitzpatrick != null ? 2 : 0);
    }
  }


  protected static class AliasCandidate {
    public final Emoji emoji;
    public final Fitzpatrick fitzpatrick;
    public final int startIndex;
    public final int endIndex;

    private AliasCandidate(Emoji emoji, Fitzpatrick fitzpatrick, int startIndex, int endIndex) {
      this.emoji = emoji;
      this.fitzpatrick = fitzpatrick;
      this.startIndex = startIndex;
      this.endIndex = endIndex;
    }
  }

  /**
   * Enum used to indicate what should be done when a Fitzpatrick modifier is
   * found.
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

  public interface EmojiTransformer {
    String transform(UnicodeCandidate unicodeCandidate);
  }
}
