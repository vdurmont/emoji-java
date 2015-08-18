package com.vdurmont.emoji;

public enum Fitzpatrick {
    TYPE_1_2("\uD83C\uDFFB"),
    TYPE_3("\uD83C\uDFFC"),
    TYPE_4("\uD83C\uDFFD"),
    TYPE_5("\uD83C\uDFFE"),
    TYPE_6("\uD83C\uDFFF");

    public final String unicode;

    Fitzpatrick(String unicode) {
        this.unicode = unicode;
    }
}
