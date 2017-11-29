package com.vdurmont.emoji;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.xeustechnologies.jcl.JarClassLoader;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class EmojiManagerTest {
  @Test
  public void getForTag_with_unknown_tag_returns_null() throws IOException {
    // GIVEN

    // WHEN
    Set<Emoji> emojis = EmojiManager.getForTag("jkahsgdfjksghfjkshf");

    // THEN
    assertNull(emojis);
  }

  @Test
  public void getForTag_returns_the_emojis_for_the_tag() throws IOException {
    // GIVEN

    // WHEN
    Set<Emoji> emojis = EmojiManager.getForTag("happy");

    // THEN
    assertEquals(4, emojis.size());
    assertTrue(TestTools.containsEmojis(
      emojis,
      "smile",
      "smiley",
      "grinning",
      "satisfied"
    ));
  }

  @Test
  public void getForAlias_with_unknown_alias_returns_null() throws IOException {
    // GIVEN

    // WHEN
    Emoji emoji = EmojiManager.getForAlias("jkahsgdfjksghfjkshf");

    // THEN
    assertNull(emoji);
  }

  @Test
  public void getForAlias_returns_the_emoji_for_the_alias() throws IOException {
    // GIVEN

    // WHEN
    Emoji emoji = EmojiManager.getForAlias("smile");

    // THEN
    assertEquals(
      "smiling face with open mouth and smiling eyes",
      emoji.getDescription()
    );
  }

  @Test
  public void getForAlias_with_colons_returns_the_emoji_for_the_alias()
    throws IOException {
    // GIVEN

    // WHEN
    Emoji emoji = EmojiManager.getForAlias(":smile:");

    // THEN
    assertEquals(
      "smiling face with open mouth and smiling eyes",
      emoji.getDescription()
    );
  }

  @Test
  public void isEmoji_for_an_emoji_returns_true() {
    // GIVEN
    String emoji = "😀";

    // WHEN
    boolean isEmoji = EmojiManager.isEmoji(emoji);

    // THEN
    assertTrue(isEmoji);
  }

  @Test
  public void isEmoji_with_fitzpatric_modifier_returns_true() {
    // GIVEN
    String emoji = "\uD83E\uDD30\uD83C\uDFFB";

    // WHEN
    boolean isEmoji = EmojiManager.isEmoji(emoji);

    // THEN
    assertTrue(isEmoji);
  }

  @Test
  public void isEmoji_for_a_non_emoji_returns_false() {
    // GIVEN
    String str = "test";

    // WHEN
    boolean isEmoji = EmojiManager.isEmoji(str);

    // THEN
    assertFalse(isEmoji);
  }

  @Test
  public void isEmoji_for_an_emoji_and_other_chars_returns_false() {
    // GIVEN
    String str = "😀 test";

    // WHEN
    boolean isEmoji = EmojiManager.isEmoji(str);

    // THEN
    assertFalse(isEmoji);
  }

  @Test
  public void isOnlyEmojis_for_an_emoji_returns_true() {
    // GIVEN
    String str = "😀";

    // WHEN
    boolean isEmoji = EmojiManager.isOnlyEmojis(str);

    // THEN
    assertTrue(isEmoji);
  }

  @Test
  public void isOnlyEmojis_for_emojis_returns_true() {
    // GIVEN
    String str = "😀😀😀";

    // WHEN
    boolean isEmoji = EmojiManager.isOnlyEmojis(str);

    // THEN
    assertTrue(isEmoji);
  }

  @Test
  public void isOnlyEmojis_for_random_string_returns_false() {
    // GIVEN
    String str = "😀a";

    // WHEN
    boolean isEmoji = EmojiManager.isOnlyEmojis(str);

    // THEN
    assertFalse(isEmoji);
  }

  @Test
  public void getAllTags_returns_the_tags() {
    // GIVEN

    // WHEN
    Collection<String> tags = EmojiManager.getAllTags();

    // THEN
    // We know the number of distinct tags int the...!
    assertEquals(594, tags.size());
  }

  @Test
  public void getAll_doesnt_return_duplicates() {
    // GIVEN

    // WHEN
    Collection<Emoji> emojis = EmojiManager.getAll();

    // THEN
    Set<String> unicodes = new HashSet<String>();
    for (Emoji emoji : emojis) {
      assertFalse(
        "Duplicate: " + emoji.getDescription(),
        unicodes.contains(emoji.getUnicode())
      );
      unicodes.add(emoji.getUnicode());
    }
    assertEquals(unicodes.size(), emojis.size());
  }

  @Test
  public void no_duplicate_alias() {
    // GIVEN

    // WHEN
    Collection<Emoji> emojis = EmojiManager.getAll();

    // THEN
    Set<String> aliases = new HashSet<String>();
    Set<String> duplicates = new HashSet<String>();
    for (Emoji emoji : emojis) {
      for (String alias : emoji.getAliases()) {
        if (aliases.contains(alias)) {
          duplicates.add(alias);
        }
        aliases.add(alias);
      }
    }
    assertEquals("Duplicates: " + duplicates, duplicates.size(), 0);
  }

  @Test
  public void overriden_path() throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
    EmojiManager.getForAlias("smile");
    final String property = "com.vdurmont.emoji.EmojiManager.PATH";
    System.setProperty(property, "/emojis-test-override.json");

    JarClassLoader classLoader = new JarClassLoader();
    classLoader.add(EmojiManager.class.getProtectionDomain().getCodeSource().getLocation());
    Class clazz = classLoader.loadClass(EmojiManager.class.getName());

    Method m_getForAlias = clazz.getMethod("getForAlias", String.class);
    Object emojiOk = m_getForAlias.invoke(null, "smile");
    Object emojiNull = m_getForAlias.invoke(null, "hug");

    Method m_getAliases = emojiOk.getClass().getMethod("getAliases");
    assertEquals("smile", ((List<String>)m_getAliases.invoke(emojiOk)).get(0));
    assertNull(emojiNull);

    System.clearProperty(property);
  }
}
