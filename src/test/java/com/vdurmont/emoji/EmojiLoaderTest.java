/*
 * =================
 * EMOJI-JAVA
 * =================
 *
 * The missing emoji library for java.
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

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class EmojiLoaderTest {
	@Test
	public void load_empty_database_returns_empty_list() throws IOException {
		// GIVEN
		InputStream stream = new ByteArrayInputStream(new JSONArray().toString().getBytes("UTF-8"));

		// WHEN
		List<Emoji> emojis = EmojiLoader.loadEmojis(stream);

		// THEN
		assertEquals(0, emojis.size());
	}

	@Test
	public void buildEmojiFromJSON() throws UnsupportedEncodingException {
		// GIVEN
		JSONObject json = new JSONObject("{"
				+ "\"emoji\": \"😄\","
				+ "\"description\": \"smiling face with open mouth and smiling eyes\","
				+ "\"aliases\": [\"smile\"],"
				+ "\"tags\": [\"happy\", \"joy\", \"pleased\"]"
				+ "}");

		// WHEN
		Emoji emoji = EmojiLoader.buildEmojiFromJSON(json);

		// THEN
		assertEquals("😄", emoji.getUnicode());
		assertEquals("smiling face with open mouth and smiling eyes", emoji.getDescription());
		assertEquals(1, emoji.getAliases().size());
		assertEquals("smile", emoji.getAliases().get(0));
		assertEquals(3, emoji.getTags().size());
		assertEquals("happy", emoji.getTags().get(0));
		assertEquals("joy", emoji.getTags().get(1));
		assertEquals("pleased", emoji.getTags().get(2));
	}

	@Test
	public void buildEmojiFromJSON_without_description_sets_a_null_description() throws UnsupportedEncodingException {
		// GIVEN
		JSONObject json = new JSONObject("{"
				+ "\"emoji\": \"😄\","
				+ "\"aliases\": [\"smile\"],"
				+ "\"tags\": [\"happy\", \"joy\", \"pleased\"]"
				+ "}");

		// WHEN
		Emoji emoji = EmojiLoader.buildEmojiFromJSON(json);

		// THEN
		assertNull(emoji.getDescription());
	}

	@Test
	public void buildEmojiFromJSON_without_unicode_returns_null() throws UnsupportedEncodingException {
		// GIVEN
		JSONObject json = new JSONObject("{"
				+ "\"aliases\": [\"smile\"],"
				+ "\"tags\": [\"happy\", \"joy\", \"pleased\"]"
				+ "}");

		// WHEN
		Emoji emoji = EmojiLoader.buildEmojiFromJSON(json);

		// THEN
		assertNull(emoji);
	}
}
