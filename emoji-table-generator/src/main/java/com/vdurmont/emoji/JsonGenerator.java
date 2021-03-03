package com.vdurmont.emoji;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

/**
 * This app generate the emoji json from https://unicode.org/emoji/charts/full-emoji-list.html ;)
 * <p/>
 * Run with:
 * mvn exec:java -Dexec.mainClass="com.vdurmont.emoji.JsonGenerator"
 */
public class JsonGenerator {
    private static final String ARGS_NAME_PROXY_HOST = "proxy";
    private static final String ARGS_NAME_PROXY_PORT = "port";
    private static final String ARGS_NAME_OFFLINE_PATH = "path";
    private static final String ARGS_NAME_ONLINE_URL = "url";
    private static final String ARGS_NAME_SAVE_PATH = "save_url";
    private static final String ARGS_NAME_EMOJI_JSON_PATH = "emoji_path";
    private static final String ARGS_NAME_EMOJI_I18N_JSON_PATH = "emoji_i18n_path";
    private static final String STRING_SYMBOL_EQUAL = "=";
    private static final String EMOJI_REMOTE_ONLINE_URL = "https://unicode.org/emoji/charts/full-emoji-list.html";
    private static Map<String, String> ARGS_MAP;

    public static void main(String[] args) throws IOException {
        ARGS_MAP = argsParser(args);
        Document root = getDocument();

        Elements tdTags, trTags = root.getElementsByTag("tr");
        String aliasBigHead = null, aliasMediumHead = null;
        Element bighead, mediumhead;
        JSONArray emojis = new JSONArray();
        String desc;
        JSONObject emoji;
        Map<String, JSONObject> emojiMap = getJsonMapFromEmojiJson(ARGS_MAP.get(ARGS_NAME_EMOJI_JSON_PATH));
        Map<String, String> emojiI18nMap = getI18nMapFromEmojiI18nJson(ARGS_MAP.get(ARGS_NAME_EMOJI_I18N_JSON_PATH));
        for (Element trTag : trTags) {
            bighead = trTag.select("th.bighead>a").first();
            if (!Objects.isNull(bighead)) {
                aliasBigHead = bighead.attr("name");
                continue;
            }
            mediumhead = trTag.select("th.mediumhead>a").first();
            if (!Objects.isNull(mediumhead)) {
                aliasMediumHead = mediumhead.attr("name");
                continue;
            }
            tdTags = trTag.children();
            if (!tdTags.get(1).hasClass("code")) {
                continue;
            }
            desc = tdTags.last().text().replaceAll("[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\p{Cs}\\p{Sc}\\s]", "");

            String emojiChar = tdTags.get(2).text();
            if (tdTags.get(1).text().endsWith("U+FE0F U+20E3")) {
                emojiChar = new StringBuilder(emojiChar).deleteCharAt(1).toString();
            }
            if (!emojiMap.containsKey(emojiChar)) {
                emoji = new JSONObject();
                emoji.put("emojiChar", emojiChar);
                emoji.put("emoji", convertEmoji2Unicode(emojiChar));
                emoji.put("description", emojiI18nMap.getOrDefault(emojiChar, desc));
                emoji.put("aliases", desc.replace(" ", "_"));
                emoji.put("tags", Arrays.asList(aliasBigHead, aliasMediumHead));
            } else {
                emoji = emojiMap.get(emojiChar);
                emoji.put("description", emojiI18nMap.getOrDefault(emoji.getString("emojiChar"),
                        emoji.getString("description")));
                emoji.put("emoji", convertEmoji2Unicode(emoji.getString("emojiChar")));
            }
            emojis.put(emoji);
        }

        String emojiJson = emojis.toString(4).replaceAll("/", "\\\\")
                .replaceAll("\\^\\^u", "\\\\u");

        File emojiFile = new File(ARGS_MAP.getOrDefault(ARGS_NAME_SAVE_PATH, System.getProperty("java.io.tmpdir")
                + File.separator + "emoji.json"));
        System.out.println("save to: " + emojiFile.getAbsolutePath());
        Files.write(emojiFile.toPath(), Collections.singleton(emojiJson), StandardCharsets.UTF_8);
    }

    /**
     * convert emoji to unicode
     *
     * @param emoji emoji char
     * @return emoji's unicode
     */
    private static String convertEmoji2Unicode(String emoji) {
        char[] chars = emoji.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (char c : chars) {
            builder.append("^^u");
            builder.append(Integer.toHexString(0x10000 | c).substring(1).toUpperCase());
        }
        return builder.toString();
    }

    /**
     * jsoup document builder
     *
     * @return access url and get body when args without `path` arg else read local file by `path`
     * @throws IOException io exception
     */
    private static Document getDocument() throws IOException {
        if (isBlank(ARGS_MAP.get(ARGS_NAME_OFFLINE_PATH))) {
            return getConnection().get();
        } else {
            return Jsoup.parse(new File(ARGS_MAP.get(ARGS_NAME_OFFLINE_PATH)), "utf-8", "https://unicode.org/");
        }
    }

    /**
     * builder jsoup connection
     *
     * @return jsoup connection
     */
    private static Connection getConnection() {
        Connection connect = Jsoup.connect(ARGS_MAP.getOrDefault(ARGS_NAME_ONLINE_URL, EMOJI_REMOTE_ONLINE_URL))
                .maxBodySize(Integer.MAX_VALUE);
        if (!isBlank(ARGS_MAP.get(ARGS_NAME_PROXY_HOST)) && !isBlank(ARGS_MAP.get(ARGS_NAME_PROXY_PORT))) {
            connect.proxy(ARGS_MAP.get(ARGS_NAME_PROXY_HOST), Integer.parseInt(ARGS_MAP.get(ARGS_NAME_PROXY_PORT)));
        }
        return connect;
    }

    /**
     * like apache commons lang 3 StringUtils.isBlank
     *
     * @param str check string
     * @return blank is true,else false
     */
    private static boolean isBlank(String str) {
        return Objects.isNull(str) || "".equals(str.trim());
    }

    /**
     * args parser
     *
     * @param args command args
     * @return args Map
     */
    private static Map<String, String> argsParser(String[] args) {
        if (Objects.isNull(args) || args.length == 0) {
            return Collections.emptyMap();
        }
        ARGS_MAP = new HashMap<>(args.length);
        int index;
        for (String arg : args) {
            index = arg.indexOf(STRING_SYMBOL_EQUAL);
            if (index <= 0) {
                continue;
            }
            ARGS_MAP.put(arg.substring(0, index), arg.substring(index + 1));
        }
        return ARGS_MAP;
    }

    private static Map<String, JSONObject> getJsonMapFromEmojiJson(String emojiPath) {
        if (Objects.isNull(emojiPath) || emojiPath.length() == 0) {
            return Collections.emptyMap();
        }
        Map<String, JSONObject> emojiMap;
        try {
            JSONArray emojiArray = new JSONArray(String.join("", Files.readAllLines(new File(emojiPath).toPath())));
            emojiMap = new HashMap<>(emojiArray.length());
            for (Object json : emojiArray) {
                JSONObject emoji = (JSONObject) json;
                emojiMap.put(emoji.getString("emojiChar"), emoji);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            emojiMap = Collections.emptyMap();
        }
        return emojiMap;
    }

    private static Map<String, String> getI18nMapFromEmojiI18nJson(String emojiI18nPath) {
        if (Objects.isNull(emojiI18nPath) || emojiI18nPath.length() == 0) {
            return Collections.emptyMap();
        }
        Map<String, String> emojiMap;
        try {
            JSONArray emojiArray = new JSONArray(String.join("", Files.readAllLines(new File(emojiI18nPath).toPath())));
            emojiMap = new HashMap<>(emojiArray.length());
            for (Object json : emojiArray) {
                JSONObject emoji = (JSONObject) json;
                emojiMap.put(emoji.getString("emojiChar"), emoji.getString("description"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            emojiMap = Collections.emptyMap();
        }
        return emojiMap;
    }
}
