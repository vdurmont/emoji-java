package com.vdurmont.emoji.parsers;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;
import com.vdurmont.emoji.Fitzpatrick;

/**
 * A detected emoji with associated metadata.
 */
public class UnicodeCandidate {
    public final Emoji emoji;
    public final Fitzpatrick fitzpatrick;
    public final int startIndex, endIndex;

    public UnicodeCandidate(String emoji, String fitzpatrick, int startIndex, int endIndex) {
        this.emoji = EmojiManager.getByUnicode(emoji);
        this.fitzpatrick = Fitzpatrick.fitzpatrickFromUnicode(fitzpatrick);
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    public String getEmojiUnicodeWithFitzpatrick() {
        if(emoji.supportsFitzpatrick()) return emoji.getUnicode(fitzpatrick);
        return emoji.getUnicode();
    }

    public int getEndIndexWithFitzpatrick() {
        if(hasFitzpatrick()) return endIndex+2;
        return endIndex;
    }

    public boolean hasFitzpatrick() {
        return fitzpatrick != null;
    }
}
