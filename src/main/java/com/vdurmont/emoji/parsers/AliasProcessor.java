package com.vdurmont.emoji.parsers;

import com.vdurmont.emoji.Emoji;

/**
 * Defines a type which can perform string substitutions on detected emoji aliases.
 */
public interface AliasProcessor {
    /**
     * A function that takes an {@link AliasCandidate} as extracted from <code>:<b>an_alias</b><i>|optional_fitzpatrick</i>:</code>
     * and returns what to replace it with. The colons get removed as well.
     * @param alias The string which was identified as potentially being an emoji alias.
     * @param emoji The {@link Emoji} that <code>alias</code> is associated with, or <code>null</code> if it's not associated
     * @return the string you want to replace <code>:alias:</code> with, or null to leave it unchanged
     */
    String apply(AliasCandidate alias, Emoji emoji);
}
