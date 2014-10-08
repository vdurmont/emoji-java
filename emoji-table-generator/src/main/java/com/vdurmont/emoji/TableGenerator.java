package com.vdurmont.emoji;

import java.io.FileWriter;
import java.io.IOException;

/**
 * This app generate the emoji table in the README ;)
 * <p/>
 * Run with:
 * mvn exec:java -Dexec.mainClass="com.vdurmont.emoji.TableGenerator"
 */
public class TableGenerator {
    public static void main(String[] args) throws IOException {
        StringBuilder sb = new StringBuilder();

        // Table header
        sb.append("| Emoji | Aliases | Emoji | Aliases |\n");
        sb.append("| :---: | ------- | :---: | ------- |\n");

        // Emojis!
        int i = 0;
        for (Emoji emoji : EmojiManager.getAll()) {
            String aliases = getAliases(emoji);

            if (i % 2 == 0) {
                sb.append("| ")
                        .append(emoji.getUnicode())
                        .append(" | ")
                        .append(aliases)
                        .append(" |");
            } else {
                sb.append(" ")
                        .append(emoji.getUnicode())
                        .append(" | ")
                        .append(aliases)
                        .append(" |\n");
            }

            i++;
        }

        // Output!
        if (args.length > 0) {
            String path = args[0];
            FileWriter writer = new FileWriter(path);
            writer.write(sb.toString());
            System.out.println("Written on " + path);
        } else {
            System.out.println(sb.toString());
        }
    }

    private static String getAliases(Emoji emoji) {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (String alias : emoji.getAliases()) {
            if (first) {
                first = false;
            } else {
                result.append(", ");
            }
            result.append(alias);
        }

        return result.toString();
    }
}
