package com.vdurmont.emoji;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class EmojiManagerTest {
  @Test
  public void getForTag_with_unknown_tag_returns_null() throws IOException {
    // GIVEN

    // WHEN
    Set<Emoji> emojis = new EmojiManager().getForTag("jkahsgdfjksghfjkshf");

    // THEN
    assertNull(emojis);
  }

  @Test
  public void getForTag_returns_the_emojis_for_the_tag() throws IOException {
    // GIVEN

    // WHEN
    Set<Emoji> emojis = new EmojiManager().getForTag("happy");

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
    Emoji emoji = new EmojiManager().getForAlias("jkahsgdfjksghfjkshf");

    // THEN
    assertNull(emoji);
  }

  @Test
  public void getForAlias_returns_the_emoji_for_the_alias() throws IOException {
    // GIVEN

    // WHEN
    Emoji emoji = new EmojiManager().getForAlias("smile");

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
    Emoji emoji = new EmojiManager().getForAlias(":smile:");

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
    boolean isEmoji = new EmojiManager().isEmoji(emoji);

    // THEN
    assertTrue(isEmoji);
  }

  @Test
  public void isEmoji_for_a_non_emoji_returns_false() {
    // GIVEN
    String str = "test";

    // WHEN
    boolean isEmoji = new EmojiManager().isEmoji(str);

    // THEN
    assertFalse(isEmoji);
  }

  @Test
  public void isEmoji_for_an_emoji_and_other_chars_returns_false() {
    // GIVEN
    String str = "😀 test";

    // WHEN
    boolean isEmoji = new EmojiManager().isEmoji(str);

    // THEN
    assertFalse(isEmoji);
  }

  @Test
  public void getAllTags_returns_the_tags() {
    // GIVEN

    // WHEN
    Collection<String> tags = new EmojiManager().getAllTags();

    // THEN
    // We know the number of distinct tags int the...!
    assertEquals(594, tags.size());
  }

  @Test
  public void getAll_doesnt_return_duplicates() {
    // GIVEN

    // WHEN
    Collection<Emoji> emojis = new EmojiManager().getAll();

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
  public void addEmoji_adds_emoji() {
    // GIVEN
    EmojiManager manager = new EmojiManager();
    Emoji emoji = new Emoji(
      "Test emoji",
      false,
      Collections.singletonList("testtesttest"),
      Collections.singletonList("testtesttest"),
      "2");

    // WHEN
    manager.addEmoji(emoji);

    // THEN
    assertEquals(emoji, manager.getForAlias(":testtesttest:"));
    assertEquals(new HashSet<>(Collections.singletonList(emoji)), manager.getForTag("testtesttest"));
  }
}
