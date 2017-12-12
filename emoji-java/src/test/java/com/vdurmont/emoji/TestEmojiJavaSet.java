package com.vdurmont.emoji;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestEmojiJavaSet {

    @Test
    public void ensure_unicodey_set_is_not_loaded() {
        // GIVEN
        String str = "The emojis :hugging_face: and :thinking_face: only exists in unicodey's set!";

        // WHEN
        String result = EmojiParser.parseToUnicode(str);

        // THEN
        assertEquals(
            "The emojis :hugging_face: and :thinking_face: only exists in unicodey's set!",
            result
        );
    }

    @Test
    public void ensure_emoji_java_set_is_loaded() {
        // GIVEN
        String str = "The emojis :hug: and :think: only exists in emoji-java set!";

        // WHEN
        String result = EmojiParser.parseToUnicode(str);

        // THEN
        assertEquals(
            "The emojis \uD83E\uDD17 and \uD83E\uDD14 only exists in emoji-java set!",
            result
        );
    }

}
