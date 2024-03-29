package com.vdurmont.emoji;

import org.junit.Test;

import static com.vdurmont.emoji.EmojiManager.isEmoji;
import static org.junit.Assert.assertEquals;

public class EmojiTrieTest {
    @Test
    public void testIsEmoji_ValidEmoji() {
        char[] sequence = {'\uD83D', '\uDE0A', 'a', 'b'};
        EmojiTrie.Matches result = isEmoji(sequence);
        assertEquals(EmojiTrie.Matches.IMPOSSIBLE, result);
    }

    @Test
    public void testIsEmoji_NoEmoji() {
        char[] sequence = {'a', 'b', 'c'};
        EmojiTrie.Matches result = isEmoji(sequence);
        assertEquals(EmojiTrie.Matches.IMPOSSIBLE, result);
    }

    @Test
    public void testIsEmoji_EmptySequence() {
        char[] sequence = {};
        EmojiTrie.Matches result = isEmoji(sequence);
        assertEquals(EmojiTrie.Matches.POSSIBLY, result);
    }
}
