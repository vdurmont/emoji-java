package com.vdurmont.emoji;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;


/**
 * This class represents an emoji.<br>
 * <br>
 * This object is immutable so it can be used safely in a multithreaded context.
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

    /**
     * Constructor for the Emoji.
     *
     * @param description         The description of the emoji
     * @param supportsFitzpatrick wether the emoji supports the Fitzpatrick modifiers or not
     * @param aliases             the aliases for this emoji
     * @param tags                the tags associated with this emoji
     * @param bytes               the bytes that represent the emoji
     */
    protected Emoji(String description, boolean supportsFitzpatrick, List<String> aliases, List<String> tags, byte... bytes) {
        this.description = description;
        this.supportsFitzpatrick = supportsFitzpatrick;
        this.aliases = Collections.unmodifiableList(aliases);
        this.tags = Collections.unmodifiableList(tags);
        try {
            this.unicode = new String(bytes, "UTF-8");
            int htmlCode = Character.codePointAt(this.unicode, 0);
            this.htmlDec = "&#" + htmlCode + ";";
            this.htmlHex = "&#x" + Integer.toHexString(htmlCode) + ";";
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the description of the emoji
     *
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns wether the emoji supports the Fitzpatrick modifiers or not
     *
     * @return true if the emoji supports the Fitzpatrick modifiers
     */
    public boolean supportsFitzpatrick() {
        return this.supportsFitzpatrick;
    }

    /**
     * Returns the aliases of the emoji
     *
     * @return the aliases (unmodifiable)
     */
    public List<String> getAliases() {
        return this.aliases;
    }

    /**
     * Returns the tags of the emoji
     *
     * @return the tags (unmodifiable)
     */
    public List<String> getTags() {
        return this.tags;
    }

    /**
     * Returns the unicode representation of the emoji
     *
     * @return the unicode representation
     */
    public String getUnicode() {
        return this.unicode;
    }

    /**
     * Returns the unicode representation of the emoji associated with the provided Fitzpatrick modifier.<br>
     * If the modifier is null, then the result is similar to {@link Emoji#getUnicode()}
     *
     * @param fitzpatrick the fitzpatrick modifier or null
     *
     * @return the unicode representation
     * @throws UnsupportedOperationException if the emoji doesn't support the Fitzpatrick modifiers
     */
    public String getUnicode(Fitzpatrick fitzpatrick) {
        if (!this.supportsFitzpatrick()) {
            throw new UnsupportedOperationException("Cannot get the unicode with a fitzpatrick modifier, the emoji doesn't support fitzpatrick.");
        } else if (fitzpatrick == null) {
            return this.getUnicode();
        }
        return this.getUnicode() + fitzpatrick.unicode;
    }

    /**
     * Returns the HTML decimal representation of the emoji
     *
     * @return the HTML decimal representation
     */
    public String getHtmlDecimal() {
        return this.htmlDec;
    }

    /**
     * Returns the HTML hexadecimal representation of the emoji
     *
     * @return the HTML hexadecimal representation
     */
    public String getHtmlHexidecimal() {
        return this.htmlHex;
    }

    /**
     * Returns the String representation of the Emoji object.<br>
     * <br>
     * Example:<br>
     * <code>Emoji{description='smiling face with open mouth and smiling eyes', supportsFitzpatrick=false, aliases=[smile], tags=[happy, joy, pleased], unicode='😄', htmlDec='&amp;#128516;', htmlHex='&amp;#x1f604;'}</code>
     *
     * @return the string representation
     */
    @Override
    public String toString() {
        return "Emoji{" +
                "description='" + description + '\'' +
                ", supportsFitzpatrick=" + supportsFitzpatrick +
                ", aliases=" + aliases +
                ", tags=" + tags +
                ", unicode='" + unicode + '\'' +
                ", htmlDec='" + htmlDec + '\'' +
                ", htmlHex='" + htmlHex + '\'' +
                '}';
    }
}
