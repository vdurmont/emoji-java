# emoji-table-genrator

This is just a "quick'n'dirty" project to generate a markdown table with all the emojis.

It is used to generate the top file EMOJIS.md :)


Run with:

```
mvn compile exec:java -Dexec.mainClass="com.vdurmont.emoji.TableGenerator" -Dexec.args=<path_to_EMOJIS.md>
```