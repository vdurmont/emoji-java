package com.vdurmont.emoji;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Holds the loaded emojis and provides search functions.
 *
 * @author Vincent DURMONT [vdurmont@gmail.com]
 */
public class EmojiManager {
  private static final String DEFAULT_RESOURCE = "/emojis.json";
  private final Map<String, Emoji> EMOJIS_BY_ALIAS;
  private final Map<String, Set<Emoji>> EMOJIS_BY_TAG;
  private final List<Emoji> ALL_EMOJIS;
  private final EmojiTrie EMOJI_TRIE;

  /**
   * Builds an emoji manager
   */
  public EmojiManager() {
    EMOJIS_BY_ALIAS = new HashMap<String, Emoji>();
    EMOJIS_BY_TAG = new HashMap<String, Set<Emoji>>();
    ALL_EMOJIS = new ArrayList<Emoji>();
    EMOJI_TRIE = new EmojiTrie();

    loadEmojisFromResource(DEFAULT_RESOURCE);
  }

  /**
   * Loads emojis from a resource
   *
   * @param resourcePath path to a resource file
   */
  private void loadEmojisFromResource(String resourcePath) {
    ObjectMapper mapper = new ObjectMapper();
    final CollectionType emojiList = mapper.getTypeFactory().constructCollectionType(List.class, Emoji.class);

    try {
      InputStream stream = EmojiManager.class.getResourceAsStream(resourcePath);
      List<Emoji> emojis = mapper.readValue(stream, emojiList);
      addEmojis(emojis);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Adds emojis to the list of known emojis
   *
   * @param emojis emojis
   */
  public void addEmojis(List<Emoji> emojis) {
    for (Emoji emoji : emojis) {
      addEmoji(emoji);
    }
  }

  /**
   * Adds an emoji to the manager
   *
   * @param emoji emoji to add
   */
  public void addEmoji(Emoji emoji) {
    ALL_EMOJIS.add(emoji);

    for (String tag : emoji.getTags()) {
      if (EMOJIS_BY_TAG.get(tag) == null) {
        EMOJIS_BY_TAG.put(tag, new HashSet<Emoji>());
      }
      EMOJIS_BY_TAG.get(tag).add(emoji);
    }
    for (String alias : emoji.getAliases()) {
      EMOJIS_BY_ALIAS.put(alias, emoji);
    }

    EMOJI_TRIE.addEmoji(emoji);
  }

  /**
   * Returns all the {@link com.vdurmont.emoji.Emoji}s for a given tag.
   *
   * @param tag the tag
   *
   * @return the associated {@link com.vdurmont.emoji.Emoji}s, null if the tag
   * is unknown
   */
  public Set<Emoji> getForTag(String tag) {
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
   * @return the associated {@link com.vdurmont.emoji.Emoji}, null if the alias
   * is unknown
   */
  public Emoji getForAlias(String alias) {
    if (alias == null) {
      return null;
    }
    return EMOJIS_BY_ALIAS.get(trimAlias(alias));
  }

  private String trimAlias(String alias) {
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
   * Returns the {@link com.vdurmont.emoji.Emoji} for a given unicode.
   *
   * @param unicode the the unicode
   *
   * @return the associated {@link com.vdurmont.emoji.Emoji}, null if the
   * unicode is unknown
   */
  public Emoji getByUnicode(String unicode) {
    if (unicode == null) {
      return null;
    }
    return EMOJI_TRIE.getEmoji(unicode);
  }

  /**
   * Returns all the {@link com.vdurmont.emoji.Emoji}s
   *
   * @return all the {@link com.vdurmont.emoji.Emoji}s
   */
  public Collection<Emoji> getAll() {
    return ALL_EMOJIS;
  }

  /**
   * Tests if a given String is an emoji.
   *
   * @param string the string to test
   *
   * @return true if the string is an emoji's unicode, false else
   */
  public boolean isEmoji(String string) {
    return string != null &&
      EMOJI_TRIE.isEmoji(string.toCharArray()).exactMatch();
  }

  /**
   * Checks if sequence of chars contain an emoji.
   * @param sequence Sequence of char that may contain emoji in full or
   * partially.
   *
   * @return
   * &lt;li&gt;
   *   Matches.EXACTLY if char sequence in its entirety is an emoji
   * &lt;/li&gt;
   * &lt;li&gt;
   *   Matches.POSSIBLY if char sequence matches prefix of an emoji
   * &lt;/li&gt;
   * &lt;li&gt;
   *   Matches.IMPOSSIBLE if char sequence matches no emoji or prefix of an
   *   emoji
   * &lt;/li&gt;
   */
  public EmojiTrie.Matches isEmoji(char[] sequence) {
    return EMOJI_TRIE.isEmoji(sequence);
  }

  /**
   * Returns all the tags in the database
   *
   * @return the tags
   */
  public Collection<String> getAllTags() {
    return EMOJIS_BY_TAG.keySet();
  }
}
