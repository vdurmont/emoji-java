package com.vdurmont.emoji;

import com.vdurmont.emoji.EmojiParser.AliasCandidate;
import com.vdurmont.emoji.EmojiParser.FitzpatrickAction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class EmojiParserTest {

  final int FIRST_ITEM = 0;
  final int SECOND_ITEM = 1;
  final int THIRD_ITEM = 2;
  final int FOURTH_ITEM = 3;
  final int FIFTH_ITEM = 4;

  final String surfer0 = EmojiManager.getForAlias("surfer").getUnicode();
  final String surferFitzpatrickType3 = EmojiManager.getForAlias("surfer").getUnicode(Fitzpatrick.TYPE_3);
  final String surferFitzpatrickType4 = EmojiManager.getForAlias("surfer").getUnicode(Fitzpatrick.TYPE_4);
  final String surferFitzpatrickType5 = EmojiManager.getForAlias("surfer").getUnicode(Fitzpatrick.TYPE_5);
  final String surferFitzpatrickType6 = EmojiManager.getForAlias("surfer").getUnicode(Fitzpatrick.TYPE_6);
  final int NUM_EXTRACTED_EMOJIS = 5;
  final String strWithThreeHappyFaceEmojis = "An 😀awesome 😃string with a few 😉emojis!";

  @Test
  public void parseToAliases_replaces_the_emojis_by_one_of_their_aliases() {
    // GIVEN
    String str = "An 😀awesome 😃string with a few 😉emojis!";

    // WHEN
    String result = EmojiParser.parseToAliases(str);

    // THEN
    assertEquals("An :grinning:awesome :smiley:string with a few :wink:emojis!", result);
  }

  @Test
  public void replaceAllEmojis_replace_the_emojis_by_string() throws Exception {
    // GIVEN
    String str = "An 😀awesome 😃string with a few 😉emojis!";

    // WHEN
    String result = EmojiParser.replaceAllEmojis(str, ":)");

    // THEN
    assertEquals("An :)awesome :)string with a few :)emojis!", result);
  }

  @Test
  public void parseToAliases_REPLACE_with_a_fitzpatrick_modifier() {
    // GIVEN
    String str = "\uD83D\uDC66\uD83C\uDFFF";

    // WHEN
    String result = EmojiParser.parseToAliases(str);

    // THEN
    assertEquals(":boy|type_6:", result);
  }

  @Test
  public void parseToAliases_REMOVE_with_a_fitzpatrick_modifier() {
    // GIVEN
    String str = "\uD83D\uDC66\uD83C\uDFFF";

    // WHEN
    String result = EmojiParser.parseToAliases(str, FitzpatrickAction.REMOVE);

    // THEN
    assertEquals(":boy:", result);
  }

  @Test
  public void parseToAliases_REMOVE_without_a_fitzpatrick_modifier() {
    // GIVEN
    String str = "\uD83D\uDC66";

    // WHEN
    String result = EmojiParser.parseToAliases(str, FitzpatrickAction.REMOVE);

    // THEN
    assertEquals(":boy:", result);
  }

  @Test
  public void parseToAliases_IGNORE_with_a_fitzpatrick_modifier() {
    // GIVEN
    String str = "\uD83D\uDC66\uD83C\uDFFF";

    // WHEN
    String result = EmojiParser.parseToAliases(str, FitzpatrickAction.IGNORE);

    // THEN
    assertEquals(":boy:\uD83C\uDFFF", result);
  }

  @Test
  public void parseToAliases_IGNORE_without_a_fitzpatrick_modifier() {
    // GIVEN
    String str = "\uD83D\uDC66";

    // WHEN
    String result = EmojiParser.parseToAliases(str, FitzpatrickAction.IGNORE);

    // THEN
    assertEquals(":boy:", result);
  }

  @Test
  public void parseToAliases_with_long_overlapping_emoji() {
    // GIVEN
    String str = "\uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC66";

    // WHEN
    String result = EmojiParser.parseToAliases(str);

    // With greedy parsing, this will give :man::woman::boy:
    // THEN
    assertEquals(":family_man_woman_boy:", result);
  }

  @Test
  public void parseToAliases_continuous_non_overlapping_emojis() {
    // GIVEN
    String str = "\uD83D\uDC69\uD83D\uDC68\uD83D\uDC66";

    // WHEN
    String result = EmojiParser.parseToAliases(str);

    // THEN
    assertEquals(":woman::man::boy:", result);
  }

  @Test
  public void parseToHtmlDecimal_replaces_the_emojis_by_their_html_decimal_representation() {
    // GIVEN
    String str = "An 😀awesome 😃string with a few 😉emojis!";

    // WHEN
    String result = EmojiParser.parseToHtmlDecimal(str);

    // THEN
    assertEquals("An &#128512;awesome &#128515;string with a few &#128521;emojis!", result);
  }

  @Test
  public void parseToHtmlDecimal_PARSE_with_a_fitzpatrick_modifier() {
    // GIVEN
    String str = "\uD83D\uDC66\uD83C\uDFFF";

    // WHEN
    String result = EmojiParser.parseToHtmlDecimal(str, FitzpatrickAction.PARSE);

    // THEN
    assertEquals("&#128102;", result);
  }

  @Test
  public void parseToHtmlDecimal_REMOVE_with_a_fitzpatrick_modifier() {
    // GIVEN
    String str = "\uD83D\uDC66\uD83C\uDFFF";

    // WHEN
    String result = EmojiParser.parseToHtmlDecimal(str, FitzpatrickAction.REMOVE);

    // THEN
    assertEquals("&#128102;", result);
  }

  @Test
  public void parseToHtmlDecimal_IGNORE_with_a_fitzpatrick_modifier() {
    // GIVEN
    String str = "\uD83D\uDC66\uD83C\uDFFF";

    // WHEN
    String result = EmojiParser.parseToHtmlDecimal(str, FitzpatrickAction.IGNORE);

    // THEN
    assertEquals("&#128102;\uD83C\uDFFF", result);
  }

  @Test
  public void parseToHtmlHexadecimal_replaces_the_emojis_by_their_htm_hex_representation() {
    // GIVEN
    String str = "An 😀awesome 😃string with a few 😉emojis!";

    // WHEN
    String result = EmojiParser.parseToHtmlHexadecimal(str);

    // THEN
    assertEquals("An &#x1f600;awesome &#x1f603;string with a few &#x1f609;emojis!", result);
  }

  @Test
  public void parseToHtmlHexadecimal_PARSE_with_a_fitzpatrick_modifier() {
    // GIVEN
    String str = "\uD83D\uDC66\uD83C\uDFFF";

    // WHEN
    String result = EmojiParser.parseToHtmlHexadecimal(str, FitzpatrickAction.PARSE);

    // THEN
    assertEquals("&#x1f466;", result);
  }

  @Test
  public void parseToHtmlHexadecimal_REMOVE_with_a_fitzpatrick_modifier() {
    // GIVEN
    String str = "\uD83D\uDC66\uD83C\uDFFF";

    // WHEN
    String result = EmojiParser.parseToHtmlHexadecimal(str, FitzpatrickAction.REMOVE);

    // THEN
    assertEquals("&#x1f466;", result);
  }

  @Test
  public void parseToHtmlHexadecimal_IGNORE_with_a_fitzpatrick_modifier() {
    // GIVEN
    String str = "\uD83D\uDC66\uD83C\uDFFF";

    // WHEN
    String result = EmojiParser.parseToHtmlHexadecimal(str, FitzpatrickAction.IGNORE);

    // THEN
    assertEquals("&#x1f466;\uD83C\uDFFF", result);
  }

  @Test
  public void parseToUnicode_replaces_the_aliases_and_the_html_by_their_emoji() {
    // GIVEN
    String str = "An :grinning:awesome :smiley:string " + "&#128516;with a few &#x1f609;emojis!";

    // WHEN
    String result = EmojiParser.parseToUnicode(str);

    // THEN
    assertEquals("An 😀awesome 😃string 😄with a few 😉emojis!", result);
  }

  @Test
  public void parseToUnicode_with_the_thumbsup_emoji_replaces_the_alias_by_the_emoji() {
    // GIVEN
    String str = "An :+1:awesome :smiley:string " + "&#128516;with a few :wink:emojis!";

    // WHEN
    String result = EmojiParser.parseToUnicode(str);

    // THEN
    assertEquals("An \uD83D\uDC4Dawesome 😃string 😄with a few 😉emojis!", result);
  }

  @Test
  public void parseToUnicode_with_the_thumbsdown_emoji_replaces_the_alias_by_the_emoji() {
    // GIVEN
    String str = "An :-1:awesome :smiley:string &#128516;" + "with a few :wink:emojis!";

    // WHEN
    String result = EmojiParser.parseToUnicode(str);

    // THEN
    assertEquals("An \uD83D\uDC4Eawesome 😃string 😄with a few 😉emojis!", result);
  }

  @Test
  public void parseToUnicode_with_the_thumbsup_emoji_in_hex_replaces_the_alias_by_the_emoji() {
    // GIVEN
    String str = "An :+1:awesome :smiley:string &#x1f604;" + "with a few :wink:emojis!";

    // WHEN
    String result = EmojiParser.parseToUnicode(str);

    // THEN
    assertEquals("An \uD83D\uDC4Dawesome 😃string 😄with a few 😉emojis!", result);
  }

  @Test
  public void parseToUnicode_with_a_fitzpatrick_modifier() {
    // GIVEN
    String str = ":boy|type_6:";

    // WHEN
    String result = EmojiParser.parseToUnicode(str);

    // THEN
    assertEquals("\uD83D\uDC66\uD83C\uDFFF", result);
  }

  @Test
  public void parseToUnicode_with_an_unsupported_fitzpatrick_modifier_doesnt_replace() {
    // GIVEN
    String str = ":grinning|type_6:";
    // WHEN
    String result = EmojiParser.parseToUnicode(str);

    // THEN
    assertEquals(str, result);
  }

  @Test
  public void givenOneAliasBoy_whenParse_thenConfirmAliasBoy() {
    // GIVEN
    String str = "test :boy: test";
    // WHEN
    AliasCandidate candidate = EmojiParser.getAliasAt(str, 5);
    // THEN
    assertTrue(candidate.emoji.getAliases().contains("boy"));
  }

  @Test
  public void givenOneAliasBoy_whenParse_thenFitzpatrickIsNull() {
    // GIVEN
    String str = "test :boy: test";
    // WHEN
    AliasCandidate candidate = EmojiParser.getAliasAt(str, 5);
    // THEN
    assertNull(candidate.fitzpatrick);
  }

  @Test
  public void givenOneAliasBoyAndAColonAfter_whenParse_thenConfirmAliasBoy() {
    // GIVEN
    String str = "test :boy: test:";
    // WHEN
    AliasCandidate candidate = EmojiParser.getAliasAt(str, 5);
    // THEN
    assertTrue(candidate.emoji.getAliases().contains("boy"));
  }

  @Test
  public void givenOneAliasBoyAndAColonAfter_whenParse_thenFitzpatrickIsNull() {
    // GIVEN
    String str = "test :boy: test:";
    // WHEN
    AliasCandidate candidate = EmojiParser.getAliasAt(str, 5);
    // THEN
    assertNull(candidate.fitzpatrick);
  }

  @Test
  public void givenOneAliasBoyAndAnotherColonRightAfter_whenParse_thenConfirmAliasBoy() {
    // GIVEN
    String str = "test :boy::test";
    // WHEN
    AliasCandidate candidate = EmojiParser.getAliasAt(str, 5);
    // THEN
    assertTrue(candidate.emoji.getAliases().contains("boy"));
  }

  @Test
  public void givenOneAliasBoyAndAnotherColonRightAfter_whenParse_thenFitzpatrickIsNull() {
    // GIVEN
    String str = "test :boy::test";
    // WHEN
    AliasCandidate candidate = EmojiParser.getAliasAt(str, 5);
    // THEN
    assertNull(candidate.fitzpatrick);
  }

  @Test
  public void givenOneAliasBoyWithAColonBeforeAndAnotherAfter_whenParseAtWrongPosition_thenReturnNull() {
    // GIVEN
    String str = "test ::boy: test";
    // WHEN
    AliasCandidate candidate = EmojiParser.getAliasAt(str, 5);
    // THEN
    assertNull(candidate);
  }

  @Test
  public void givenOneAliasBoyWithAColonBeforeAndAnotherAfter_whenParse_thenConfirmAliasBoy() {
    // GIVEN
    String str = "test ::boy: test";
    // WHEN
    AliasCandidate candidate = EmojiParser.getAliasAt(str, 6);
    // THEN
    assertTrue(candidate.emoji.getAliases().contains("boy"));
  }

  @Test
  public void givenOneAliasBoyWithAColonBeforeAndAnotherAfter_whenParse_thenFitzpatrickIsNull() {
    // GIVEN
    String str = "test ::boy: test";
    // WHEN
    AliasCandidate candidate = EmojiParser.getAliasAt(str, 6);
    // THEN
    assertNull(candidate.fitzpatrick);
  }

  @Test
  public void givenOneAliasBoyAndAFitzpatrickModifier_whenParse_thenConfirmAliasBoy() {
    // GIVEN
    String str = "test :boy|type_3: test";
    // WHEN
    AliasCandidate candidate = EmojiParser.getAliasAt(str, 5);
    // THEN
    assertTrue(candidate.emoji.getAliases().contains("boy"));
  }

  @Test
  public void givenOneAliasBoyAndAFitzpatrickModifier_whenParse_thenFitzpatrickCandidateEqualsFitzpatrickType3() {
    // GIVEN
    String str = "test :boy|type_3: test";
    // WHEN
    AliasCandidate candidate = EmojiParser.getAliasAt(str, 5);
    // THEN
    assertEquals(Fitzpatrick.TYPE_3, candidate.fitzpatrick);
  }

  @Test
  public void givenInputWithAlias_whenParseToUnicode_thenExpectedUnicode() {
    String input = "Cuba has a new flag! :cu:";
    String expected = "Cuba has a new flag! \uD83C\uDDE8\uD83C\uDDFA";
    assertEquals(expected, EmojiParser.parseToUnicode(input));
  }

  @Test
  public void givenInputWithUnicode_whenParseToAliases_thenExpectedAlias() {
    String input = "Cuba has a new flag! \uD83C\uDDE8\uD83C\uDDFA";
    String expected = "Cuba has a new flag! :cu:";
    assertEquals(expected, EmojiParser.parseToAliases(input));
  }

  @Test
  public void removeAllEmojis_removes_all_the_emojis_from_the_string() {
    // GIVEN
    String input = "An 😀awesome 😃string 😄with " + "a \uD83D\uDC66\uD83C\uDFFFfew 😉emojis!";
    // WHEN
    String result = EmojiParser.removeAllEmojis(input);
    // THEN
    String expected = "An awesome string with a few emojis!";
    assertEquals(expected, result);
  }

  @Test
  public void removeEmojis_only_removes_the_emojis_in_the_iterable_from_the_string() {
    // GIVEN
    String input = "An\uD83D\uDE03 awesome\uD83D\uDE04 string"
        + "\uD83D\uDC4D\uD83C\uDFFF with\uD83D\uDCAA\uD83C\uDFFD a few emojis!";

    List<Emoji> emojis = new ArrayList<Emoji>();
    emojis.add(EmojiManager.getForAlias("smile"));
    emojis.add(EmojiManager.getForAlias("+1"));

    // WHEN
    String result = EmojiParser.removeEmojis(input, emojis);

    // THEN
    String expected = "An\uD83D\uDE03 awesome string with" + "\uD83D\uDCAA\uD83C\uDFFD a few emojis!";
    assertEquals(expected, result);
  }

  @Test
  public void removeAllEmojisExcept_removes_all_the_emojis_from_the_string_except_those_in_the_iterable() {
    // GIVEN
    String input = "An\uD83D\uDE03 awesome\uD83D\uDE04 string"
        + "\uD83D\uDC4D\uD83C\uDFFF with\uD83D\uDCAA\uD83C\uDFFD a few emojis!";

    List<Emoji> emojis = new ArrayList<Emoji>();
    emojis.add(EmojiManager.getForAlias("smile"));
    emojis.add(EmojiManager.getForAlias("+1"));

    // WHEN
    String result = EmojiParser.removeAllEmojisExcept(input, emojis);

    // THEN
    String expected = "An awesome\uD83D\uDE04 string\uD83D\uDC4D\uD83C\uDFFF " + "with a few emojis!";
    assertEquals(expected, result);
  }

  @Test
  public void parseToUnicode_with_the_keycap_asterisk_emoji_replaces_the_alias_by_the_emoji() {
    // GIVEN
    String str = "Let's test the :keycap_asterisk: emoji and " + "its other alias :star_keycap:";

    // WHEN
    String result = EmojiParser.parseToUnicode(str);

    // THEN
    assertEquals("Let's test the *⃣ emoji and its other alias *⃣", result);
  }

  @Test
  public void parseToAliases_NG_and_nigeria() {
    // GIVEN
    String str = "Nigeria is 🇳🇬, NG is 🆖";

    // WHEN
    String result = EmojiParser.parseToAliases(str);

    // THEN
    assertEquals("Nigeria is :ng:, NG is :squared_ng:", result);
  }

  @Test
  public void parseToAliases_couplekiss_woman_woman() {
    // GIVEN
    String str = "👩‍❤️‍💋‍👩";

    // WHEN
    String result = EmojiParser.parseToAliases(str);

    // THEN
    assertEquals(":couplekiss_woman_woman:", result);
  }


  @Test
  public void givenThreeEmojis_whenParseToExtractEmojis_thenConfirmFirstItem() {    
    // WHEN
    List<String> result = EmojiParser.extractEmojis(strWithThreeHappyFaceEmojis);
    // THEN
    assertEquals("😀", result.get(FIRST_ITEM));
  }

  @Test
  public void givenThreeEmojis_whenParseToExtractEmojis_thenConfirmSecondItem() {    
    // WHEN
    List<String> result = EmojiParser.extractEmojis(strWithThreeHappyFaceEmojis);
    // THEN
    assertEquals("😃", result.get(SECOND_ITEM));
  }

  @Test
  public void givenThreeEmojis_whenParseToExtractEmojis_thenConfirmThirdItem() {    
    // WHEN
    List<String> result = EmojiParser.extractEmojis(strWithThreeHappyFaceEmojis);
    // THEN
    assertEquals("😉", result.get(THIRD_ITEM));
  }

  private String getEmojisWithFitzpatrickModifiers() {
    return surfer0 + " " + surferFitzpatrickType3 + " " + surferFitzpatrickType4 + " " + surferFitzpatrickType5 + " "
        + surferFitzpatrickType6;
  }

  @Test
  public void givenEmojisWithFitzpatrickModifiers_whenExtractEmojis_thenSizeEqualNumExtractedEmojis() {
    // GIVEN
    final String surfers = getEmojisWithFitzpatrickModifiers();
    // WHEN
    List<String> result = EmojiParser.extractEmojis(surfers);
    // THEN
    assertEquals(NUM_EXTRACTED_EMOJIS, result.size());
  }

  @Test
  public void givenEmojisWithFitzpatrickModifiers_whenExtractEmojis_thenFirstItemEqualSurfer0() {
    // GIVEN
    final String surfers = getEmojisWithFitzpatrickModifiers();
    // WHEN
    List<String> result = EmojiParser.extractEmojis(surfers);
    // THEN
    assertEquals(surfer0, result.get(FIRST_ITEM));
  }

  @Test
  public void givenEmojisWithFitzpatrickModifiers_whenExtractEmojis_thenSecondItemEqualSurferFitzpatrickType3() {
    // GIVEN
    final String surfers = getEmojisWithFitzpatrickModifiers();
    // WHEN
    List<String> result = EmojiParser.extractEmojis(surfers);
    // THEN
    assertEquals(surferFitzpatrickType3, result.get(SECOND_ITEM));
  }

  @Test
  public void givenEmojisWithFitzpatrickModifiers_whenExtractEmojis_thenThirdItemEqualSurferFitzpatrickType4() {
    // GIVEN
    final String surfers = getEmojisWithFitzpatrickModifiers();
    // WHEN
    List<String> result = EmojiParser.extractEmojis(surfers);
    // THEN
    assertEquals(surferFitzpatrickType4, result.get(THIRD_ITEM));
  }

  @Test
  public void givenEmojisWithFitzpatrickModifiers_whenExtractEmojis_thenFourthItemEqualSurferFitzpatrickType5() {
    // GIVEN
    final String surfers = getEmojisWithFitzpatrickModifiers();
    // WHEN
    List<String> result = EmojiParser.extractEmojis(surfers);
    // THEN
    assertEquals(surferFitzpatrickType5, result.get(FOURTH_ITEM));
  }

  @Test
  public void givenEmojisWithFitzpatrickModifiers_whenExtractEmojis_thenFifthItemEqualSurferFitzpatrickType6() {
    // GIVEN
    final String surfers = getEmojisWithFitzpatrickModifiers();
    // WHEN
    List<String> result = EmojiParser.extractEmojis(surfers);
    // THEN
    assertEquals(surferFitzpatrickType6, result.get(FIFTH_ITEM));
  }

  @Test
  public void parseToAliases_with_first_medal() {
    // GIVEN
    String str = "🥇";

    // WHEN
    String result = EmojiParser.parseToAliases(str);

    // THEN
    assertEquals(":first_place_medal:", result);
  }
}
