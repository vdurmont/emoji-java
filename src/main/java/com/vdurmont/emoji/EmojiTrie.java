package com.vdurmont.emoji;

import java.util.*;

public class EmojiTrie {
    private Node root = new Node();

    public EmojiTrie(Collection<Emoji> emojis) {
        for(Emoji emoji: emojis) {
            Node tree = root;
            for (char c: emoji.getUnicode().toCharArray()) {
                if (!tree.hasChild(c)) {
                    tree.addChild(c);
                }
                tree = tree.getChild(c);
            }
            tree.setEmoji(emoji);
        }
    }


    /**
     * Checks if sequence of chars contain an emoji.
     * @param emojiSequence Sequence of char that may contain emoji in full or partially.
     * @return Returns
     * Matches.EXACTLY if char sequence in its entirety is an emoji
     * Matches.POSSIBLY if char sequence matches prefix of an emoji
     * Matches.IMPOSSIBLE if char sequence matches no emoji or prefix of an emoji
     */
    public Matches isEmoji(char[] emojiSequence) {
        Node tree = root;
        for(char c: emojiSequence) {
            if(! tree.hasChild(c)) {
                return Matches.IMPOSSIBLE;
            }
            tree = tree.getChild(c);
        }

        return tree.isEndOfEmoji() ? Matches.EXACTLY : Matches.POSSIBLY;
    }


    /**
     * Finds Emoji instance from emoji unicode
     * @param unicode unicode of emoji to get
     * @return Emoji instance if unicode matches and emoji, null otherwise.
     */
    public Emoji getEmoji(String unicode) {
        Node tree = root;
        for(char c: unicode.toCharArray()) {
            if(! tree.hasChild(c)) {
                return null;
            }
            tree = tree.getChild(c);
        }
        return tree.getEmoji();
    }

    enum Matches {
        EXACTLY, POSSIBLY, IMPOSSIBLE;

        public boolean exactMatch() {
            return this == EXACTLY;
        }

        public boolean impossibleMatch() {
            return this == IMPOSSIBLE;
        }
    }

    private class Node {
        private Map<Character, Node> children = new HashMap<Character, Node>();
        private Emoji emoji;

        private void setEmoji(Emoji emoji) {
            this.emoji = emoji;
        }

        private Emoji getEmoji() {
            return emoji;
        }

        private boolean hasChild(char child) {
            return children.containsKey(child);
        }

        private void addChild(char child) {
            children.put(child, new Node());
        }

        private Node getChild(char child) {
            return children.get(child);
        }

        private boolean isEndOfEmoji() {
            return emoji != null;
        }
    }
}
