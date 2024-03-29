package com.vdurmont.emoji.validator;

import com.vdurmont.emoji.EmojiParser;

public class NonNullAndEmptyValidator implements EmojiValidator {
    @Override
    public boolean isValid(String string) {
        return string != null && EmojiParser.removeAllEmojis(string).isEmpty();
    }
}
