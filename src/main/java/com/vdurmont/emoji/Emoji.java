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
	private final String html;

	public Emoji(String description, List<String> aliases, List<String> tags, String html, byte... bytes) {
		this.description = description;
		this.aliases = aliases;
		this.tags = tags;
		this.html = html;
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

	public String getHtml() {
		return html;
	}
}
