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
    private final boolean supportsFitzpatrick;
    private final List<String> aliases;
    private final List<String> tags;
    private final String unicode;
    private final String htmlDec;
    private final String htmlHex;

    public Emoji(String description, boolean supportsFitzpatrick, List<String> aliases, List<String> tags, int htmlCode, byte... bytes) {
        this.description = description;
        this.supportsFitzpatrick = supportsFitzpatrick;
        this.aliases = aliases;
        this.tags = tags;
        this.htmlDec = "&#" + htmlCode + ";";
        this.htmlHex = "&#x" + Integer.toHexString(htmlCode) + ";";
        try {
            this.unicode = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getDescription() {
        return this.description;
    }

    public boolean supportsFitzpatrick() {
        return this.supportsFitzpatrick;
    }

    public List<String> getAliases() {
        return this.aliases;
    }

    public List<String> getTags() {
        return this.tags;
    }

    public String getUnicode() {
        return this.unicode;
    }

    public String getUnicode(Fitzpatrick fitzpatrick) {
        if (!this.supportsFitzpatrick()) {
            throw new UnsupportedOperationException("Cannot get the unicode with a fitzpatrick modifier, the emoji doesn't support fitzpatrick.");
        } else if (fitzpatrick == null) {
            return this.getUnicode();
        }
        return this.getUnicode() + fitzpatrick.unicode;
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
                ", supportsFitzpatrick=" + supportsFitzpatrick +
                ", aliases=" + aliases +
                ", tags=" + tags +
                ", unicode=" + this.getUnicode() +
                ", htmlDec='" + htmlDec + '\'' +
                ", htmlHex='" + htmlHex + '\'' +
                '}';
    }
}
