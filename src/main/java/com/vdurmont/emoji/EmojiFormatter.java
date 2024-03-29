package com.vdurmont.emoji;

public class EmojiFormatter {
    public static String formatToString(Emoji emoji) {
        return "Emoji{" +
                "description='" + emoji.getDescription() + '\'' +
                ", supportsFitzpatrick=" + emoji.supportsFitzpatrick() +
                ", aliases=" + emoji.getAliases() +
                ", tags=" + emoji.getTags() +
                ", unicode='" + emoji.getUnicode() + '\'' +
                ", htmlDec='" + emoji.getHtmlDecimal() + '\'' +
                ", htmlHex='" + emoji.getHtmlHexadecimal() + '\'' +
                '}';

    }
}
