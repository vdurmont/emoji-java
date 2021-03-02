# emoji-table-genrator

This is just a "quick'n'dirty" project to generate a markdown table with all the emojis.

It is used for the table in the top level README :)


Run with:

```
mvn exec:java -Dexec.mainClass="com.vdurmont.emoji.TableGenerator"
or 
mvn exec:java -Dexec.mainClass="com.vdurmont.emoji.JsonGenerator" [-Dexec.args="proxy=1270.0.1 port=1080 path=path\to\already\download\emoji.html"]

```
**note:**
- []: meaning optional
- proxy: proxy server address ,e.g. 127.0.0.1
- port: proxy server port ,e.g. 1080
- path: download https://unicode.org/emoji/charts/full-emoji-list.html and save to local path,  priority is higher than the proxy and port
