/*
 * =================
 * EMOJI-JAVA
 * =================
 *
 * The missing emoji library for Java.
 *
 * See http://github.com/vdurmont/emoji-java
 *
 * =================
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Vincent DURMONT
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.vdurmont.emoji;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.junit.Assert.assertEquals;

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
	public void parseToUnicode_replaces_the_aliases_by_their_emoji() {
		// GIVEN
		String str = "An :grinning:awesome :smiley:string with a few :wink:emojis!";

		// WHEN
		String result = EmojiParser.parseToUnicode(str);

		// THEN
		assertEquals("An 😀awesome 😃string with a few 😉emojis!", result);
	}

	@Test
	public void getAliasesCanditates_with_one_alias() {
		// GIVEN
		String str = "test :candidate: test";

		// WHEN
		List<String> candidates = EmojiParser.getAliasesCandidates(str);

		// THEN
		assertEquals(1, candidates.size());
		assertEquals("candidate", candidates.get(0));
	}

	@Test
	public void getAliasesCanditates_with_one_alias_an_another_colon_after() {
		// GIVEN
		String str = "test :candidate: test:";

		// WHEN
		List<String> candidates = EmojiParser.getAliasesCandidates(str);

		// THEN
		assertEquals(1, candidates.size());
		assertEquals("candidate", candidates.get(0));
	}

	@Test
	public void getAliasesCanditates_with_one_alias_an_another_colon_right_after() {
		// GIVEN
		String str = "test :candidate::test";

		// WHEN
		List<String> candidates = EmojiParser.getAliasesCandidates(str);

		// THEN
		assertEquals(1, candidates.size());
		assertEquals("candidate", candidates.get(0));
	}

	@Test
	public void getAliasesCanditates_with_one_alias_an_another_colon_before_after() {
		// GIVEN
		String str = "test ::candidate: test";

		// WHEN
		List<String> candidates = EmojiParser.getAliasesCandidates(str);

		// THEN
		assertEquals(1, candidates.size());
		assertEquals("candidate", candidates.get(0));
	}

	@Test
	public void getAliasesCanditates_with_two_aliases() {
		// GIVEN
		String str = "test :candi: :candidate: test";

		// WHEN
		List<String> candidates = EmojiParser.getAliasesCandidates(str);

		// THEN
		assertEquals(2, candidates.size());
		assertEquals("candi", candidates.get(0));
		assertEquals("candidate", candidates.get(1));
	}

	@Test
	public void getAliasesCanditates_with_two_aliases_sharing_a_colon() {
		// GIVEN
		String str = "test :candi:candidate: test";

		// WHEN
		List<String> candidates = EmojiParser.getAliasesCandidates(str);

		// THEN
		assertEquals(2, candidates.size());
		assertEquals("candi", candidates.get(0));
		assertEquals("candidate", candidates.get(1));
	}
}
