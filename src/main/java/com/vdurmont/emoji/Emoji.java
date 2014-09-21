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

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * This class represents an emoji.
 *
 * @author Vincent DURMONT <vdurmont@gmail.com>
 */
public class Emoji {
	private final String description;
	private final List<String> aliases;
	private final List<String> tags;
	private final byte[] bytes;

	public Emoji(String description, List<String> aliases, List<String> tags, byte... bytes) {
		this.description = description;
		this.aliases = aliases;
		this.tags = tags;
		this.bytes = bytes;
	}

	public String getDescription() {
		return this.description;
	}

	public List<String> getAliases() {
		return this.aliases;
	}

	public List<String> getTags() {
		return this.tags;
	}

	public String getUnicode() {
		try {
			return new String(this.bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}
