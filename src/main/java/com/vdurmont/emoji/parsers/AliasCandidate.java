package com.vdurmont.emoji.parsers;

import com.vdurmont.emoji.Fitzpatrick;

/**
 * A POJO which represents a string of text identified as potentially an ASCII alias for an emoji.
 */
public class AliasCandidate {
    public final String fullString;
    public final String alias;
    public final Fitzpatrick fitzpatrick;

    public AliasCandidate(String fullString, String alias, String fitzpatrickString) {
        this.fullString = fullString;
        this.alias = alias;
        if (fitzpatrickString == null) {
            this.fitzpatrick = null;
        } else {
            this.fitzpatrick = Fitzpatrick.fitzpatrickFromType(fitzpatrickString);
        }
    }
}
