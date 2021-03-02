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

            emoji = new JSONObject();
            emoji.put("emojiChar", tdTags.get(2).text());
            emoji.put("emoji", convertEmoji2Unicode(tdTags.get(1)));
            emoji.put("description", desc);
            emoji.put("aliases", desc.replace(" ", "_"));
            emoji.put("tags", Arrays.asList(aliasBigHead, aliasMediumHead));
            emojis.put(emoji);

        }
        String emojiJson = emojis.toString(4).replaceAll("/", "\\\\");
        File emojiFile = new File(ARGS_MAP.getOrDefault(ARGS_NAME_SAVE_PATH, System.getProperty("java.io.tmpdir")
                + File.separator + "emoji.json"));
        System.out.println("save to: "+emojiFile.getAbsolutePath());
        Files.write(emojiFile.toPath(), Collections.singleton(emojiJson), StandardCharsets.UTF_8);
    }

    /**
     * convert emoji to unicode
     *
     * @param td code td tag
     * @return emoji's unicode
     */
    private static String convertEmoji2Unicode(Element td) {
        Element aTag = td.select("a").first();
        if (Objects.isNull(aTag) || isBlank(aTag.attr("name"))) {
            return "";
        }
        return "/u" + aTag.attr("name").replaceAll("_", "/u");
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
}
