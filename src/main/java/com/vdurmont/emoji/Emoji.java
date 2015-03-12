package com.vdurmont.emoji;

import java.io.UnsupportedEncodingException;
import java.util.List;


/**
 * This class represents an emoji.
 *
 * @author Vincent DURMONT [vdurmont@gmail.com]
 */
public class Emoji {

    private final String description;
    private final List<String> aliases;
    private final List<String> tags;
    private final byte[] bytes;
    private final String htmlDec;
    private final String htmlHex;

    public Emoji(String description, List<String> aliases, List<String> tags, int htmlCode, byte... bytes) {
        this.description = description;
        this.aliases = aliases;
        this.tags = tags;
        this.htmlDec = "&#" + htmlCode + ";";
        this.htmlHex = "&#x" + Integer.toHexString(htmlCode) + ";";
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

    @Deprecated
    public String getHtml() {
        return this.getHtmlDecimal();
    }

    public String getHtmlDecimal() {
        return this.htmlDec;
    }

    public String getHtmlHexidecimal() {
        return this.htmlHex;
    }

    @Override
    public String toString() {
        return "Emoji{" +
                "description='" + description + '\'' +
                ", aliases=" + aliases +
                ", tags=" + tags +
                ", unicode=" + this.getUnicode() +
                ", htmlDec='" + htmlDec + '\'' +
                ", htmlHex='" + htmlHex + '\'' +
                '}';
    }
}
