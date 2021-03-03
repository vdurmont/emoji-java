# emoji-table-genrator

This is just a "quick'n'dirty" project to generate a markdown table with all the emojis.

It is used for the table in the top level README :)


Run with:

```bash
mvn exec:java -Dexec.mainClass="com.vdurmont.emoji.TableGenerator"
```

```bash
mvn exec:java -Dexec.mainClass="com.vdurmont.emoji.JsonGenerator" [-Dexec.args="proxy=1270.0.1 port=1080 \
    path=path\to\already\download\emoji.html save_url=/path/to/emoji.json \
    url=https://unicode.org/emoji/charts/full-emoji-list.html \
    emoji_path=/path/to/already/emoji.json \
    emoji_i18n_path=/path/to/i18n_description/emoji_i18n.json
    "]
```

**note:**
- []: meaning optional
- proxy: proxy server address ,e.g. 127.0.0.1
- port: proxy server port ,e.g. 1080
- path: download https://unicode.org/emoji/charts/full-emoji-list.html and save to local path,  priority is higher than the proxy and port
- save_url: generator emoji json save path, default is `System.getProperty("java.io.tmpdir")`
- url: emoji list url, default is https://unicode.org/emoji/charts/full-emoji-list.html
- emoji_path: emoji json path,e.g. src/main/resources/emojis.json, use emoji_path json overwrite https://unicode.org/emoji/charts/full-emoji-list.html by `emojiChar`,content like https://github.com/vdurmont/emoji-java/blob/master/src/main/resources/emojis.json
- emoji_i18n_path: if you want i18n emoji `description`,content like https://github.com/anjia0532/emoji-java/blob/master/src/main/resources/emojis.i18n.json
