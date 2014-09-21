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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides methods to parse strings with emojis.
 *
 * @author Vincent DURMONT <vdurmont@gmail.com>
 */
public class EmojiParser {
	/**
	 * Replaces the emoji's unicode occurences by one of their alias (between 2 ':').
	 * Example: "😄" => ":smile:"
	 *
	 * @param input the string to parse
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
	 * Replaces the emoji's aliases (between 2 ':') occurences by their unicode.
	 * Example: ":smile:" => "😄"
	 *
	 * @param input the string to parse
	 * @return the string with the aliases replaced by their unicode.
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
		return result;
	}

	protected static List<String> getAliasesCandidates(String input) {
		List<String> candidates = new ArrayList<String>();
		String regex = "(?<=:)\\w+(?=:)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(input);
		matcher = matcher.useTransparentBounds(true);
		while (matcher.find()) {
			candidates.add(matcher.group());
		}
		return candidates;
	}
}
