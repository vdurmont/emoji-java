package com.vdurmont.emoji;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Holds the loaded emojis and provides search functions.
 *
 * @author Vincent DURMONT [vdurmont@gmail.com]
 */
public class EmojiManager {
    private static final String PATH = "/emojis-2014-12-11.json";
    private static final Map<String, Emoji> EMOJIS_BY_ALIAS = new HashMap<String, Emoji>();
    private static final Map<String, Set<Emoji>> EMOJIS_BY_TAG = new HashMap<String, Set<Emoji>>();

    static {
        try {
            InputStream stream = EmojiLoader.class.getResourceAsStream(PATH);
            List<Emoji> emojis = EmojiLoader.loadEmojis(stream);
            for (Emoji emoji : emojis) {
                for (String tag : emoji.getTags()) {
                    if (EMOJIS_BY_TAG.get(tag) == null) {
                        EMOJIS_BY_TAG.put(tag, new HashSet<Emoji>());
                    }
                    EMOJIS_BY_TAG.get(tag).add(emoji);
                }
                for (String alias : emoji.getAliases()) {
                    EMOJIS_BY_ALIAS.put(alias, emoji);
                }
            }
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns all the {@link com.vdurmont.emoji.Emoji}s for a given tag.
     *
     * @param tag the tag
     *
     * @return the associated {@link com.vdurmont.emoji.Emoji}s, null if the tag is unknown
     */
    public static Set<Emoji> getForTag(String tag) {
        if (tag == null) {
            return null;
        }
        return EMOJIS_BY_TAG.get(tag);
    }

    /**
     * Returns the {@link com.vdurmont.emoji.Emoji} for a given alias.
     *
     * @param alias the alias
     *
     * @return the associated {@link com.vdurmont.emoji.Emoji}, null if the alias is unknown
     */
    public static Emoji getForAlias(String alias) {
        if (alias == null) {
            return null;
        }
        return EMOJIS_BY_ALIAS.get(trimAlias(alias));
    }

    private static String trimAlias(String alias) {
        String result = alias;
        if (result.startsWith(":")) {
            result = result.substring(1, result.length());
        }
        if (result.endsWith(":")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    /**
     * Returns all the {@link com.vdurmont.emoji.Emoji}s
     *
     * @return all the {@link com.vdurmont.emoji.Emoji}s
     */
    public static Collection<Emoji> getAll() {
        return EMOJIS_BY_ALIAS.values();
    }

    /**
     * Tests if a given String is an emoji.
     *
     * @param string the string to test
     *
     * @return true if the string is an emoji's unicode, false else
     */
    public static boolean isEmoji(String string) {
        if (string != null) {
            for (Emoji emoji : getAll()) {
                if (emoji.getUnicode().equals(string)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns all the tags in the database
     *
     * @return the tags
     */
    public static Collection<String> getAllTags() {
        return EMOJIS_BY_TAG.keySet();
    }
}