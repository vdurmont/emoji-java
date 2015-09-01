package com.vdurmont.emoji;

import com.vdurmont.emoji.EmojiParser.AliasCandidate;
import com.vdurmont.emoji.EmojiParser.FitzpatrickAction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class EmojiParserTest {
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
        String str = "An :grinning:awesome :smiley:string &#128516;with a few &#x1f609;emojis!";

        // WHEN
        String result = EmojiParser.parseToUnicode(str);

        // THEN
        assertEquals("An 😀awesome 😃string 😄with a few 😉emojis!", result);
    }

    @Test
    public void parseToUnicode_with_the_thumbsup_emoji_replaces_the_alias_by_the_emoji() {
        // GIVEN
        String str = "An :+1:awesome :smiley:string &#128516;with a few :wink:emojis!";

        // WHEN
        String result = EmojiParser.parseToUnicode(str);

        // THEN
        assertEquals("An \uD83D\uDC4Dawesome 😃string 😄with a few 😉emojis!", result);
    }

    @Test
    public void parseToUnicode_with_the_thumbsdown_emoji_replaces_the_alias_by_the_emoji() {
        // GIVEN
        String str = "An :-1:awesome :smiley:string &#128516;with a few :wink:emojis!";

        // WHEN
        String result = EmojiParser.parseToUnicode(str);

        // THEN
        assertEquals("An \uD83D\uDC4Eawesome 😃string 😄with a few 😉emojis!", result);
    }

    @Test
    public void parseToUnicode_with_the_thumbsup_emoji_in_hex_replaces_the_alias_by_the_emoji() {
        // GIVEN
        String str = "An :+1:awesome :smiley:string &#x1f604;with a few :wink:emojis!";

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
    public void getAliasCanditates_with_one_alias() {
        // GIVEN
        String str = "test :candidate: test";

        // WHEN
        List<AliasCandidate> candidates = EmojiParser.getAliasCandidates(str);

        // THEN
        assertEquals(1, candidates.size());
        assertEquals("candidate", candidates.get(0).alias);
        assertNull(candidates.get(0).fitzpatrick);
    }

    @Test
    public void getAliasCanditates_with_one_alias_an_another_colon_after() {
        // GIVEN
        String str = "test :candidate: test:";

        // WHEN
        List<AliasCandidate> candidates = EmojiParser.getAliasCandidates(str);

        // THEN
        assertEquals(1, candidates.size());
        assertEquals("candidate", candidates.get(0).alias);
        assertNull(candidates.get(0).fitzpatrick);
    }

    @Test
    public void getAliasCanditates_with_one_alias_an_another_colon_right_after() {
        // GIVEN
        String str = "test :candidate::test";

        // WHEN
        List<AliasCandidate> candidates = EmojiParser.getAliasCandidates(str);

        // THEN
        assertEquals(1, candidates.size());
        assertEquals("candidate", candidates.get(0).alias);
        assertNull(candidates.get(0).fitzpatrick);
    }

    @Test
    public void getAliasCanditates_with_one_alias_an_another_colon_before_after() {
        // GIVEN
        String str = "test ::candidate: test";

        // WHEN
        List<AliasCandidate> candidates = EmojiParser.getAliasCandidates(str);

        // THEN
        assertEquals(1, candidates.size());
        assertEquals("candidate", candidates.get(0).alias);
        assertNull(candidates.get(0).fitzpatrick);
    }

    @Test
    public void getAliasCanditates_with_two_aliases() {
        // GIVEN
        String str = "test :candi: :candidate: test";

        // WHEN
        List<AliasCandidate> candidates = EmojiParser.getAliasCandidates(str);

        // THEN
        assertEquals(2, candidates.size());
        assertEquals("candi", candidates.get(0).alias);
        assertNull(candidates.get(0).fitzpatrick);
        assertEquals("candidate", candidates.get(1).alias);
        assertNull(candidates.get(1).fitzpatrick);
    }

    @Test
    public void getAliasCanditates_with_two_aliases_sharing_a_colon() {
        // GIVEN
        String str = "test :candi:candidate: test";

        // WHEN
        List<AliasCandidate> candidates = EmojiParser.getAliasCandidates(str);

        // THEN
        assertEquals(2, candidates.size());
        assertEquals("candi", candidates.get(0).alias);
        assertNull(candidates.get(0).fitzpatrick);
        assertEquals("candidate", candidates.get(1).alias);
        assertNull(candidates.get(1).fitzpatrick);
    }

    @Test
    public void getAliasCanditates_with_a_fitzpatrick_modifier() {
        // GIVEN
        String str = "test :candidate|type_3: test";

        // WHEN
        List<AliasCandidate> candidates = EmojiParser.getAliasCandidates(str);

        // THEN
        assertEquals(1, candidates.size());
        assertEquals("candidate", candidates.get(0).alias);
        assertEquals(Fitzpatrick.TYPE_3, candidates.get(0).fitzpatrick);
    }

    @Test
    public void test_with_a_new_flag() {
        String input = "Cuba has a new flag! :cu:";
        String expected = "Cuba has a new flag! \uD83C\uDDE8\uD83C\uDDFA";

        assertEquals(expected, EmojiParser.parseToUnicode(input));
        assertEquals(input, EmojiParser.parseToAliases(expected));
    }
}
