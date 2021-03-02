package com.vdurmont.emoji;

import org.json.JSONObject;

import java.util.List;
import java.util.Objects;

/**
 * TODO
 *
 * @author AnJia
 * @since 2020-12-14 19:37
 */
public class EmojiDto {

    private String description;
    private String emojiChar;
    private String emoji;
    private List<String> aliases;
    private List<String> tags;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmojiChar() {
        return emojiChar;
    }

    public void setEmojiChar(String emojiChar) {
        this.emojiChar = emojiChar;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override public String toString() {
        return new JSONObject(this).toString();
    }
}
