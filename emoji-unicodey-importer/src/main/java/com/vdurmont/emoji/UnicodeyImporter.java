package com.vdurmont.emoji;

import com.google.gson.stream.JsonWriter;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * This app imports the emoji list from https://unicodey.com/emoji-data/emoji_pretty.json
 * <p/>
 * Run with:
 * mvn exec:java -Dexec.mainClass="com.vdurmont.emoji.UnicodeyImporter"
 */
public class UnicodeyImporter {

  private static final String JSON_URL = "https://unicodey.com/emoji-data/emoji_pretty.json";

  
  private static final void initSSL() throws KeyManagementException, NoSuchAlgorithmException {
    SSLContext sc = SSLContext.getInstance("SSL");
    TrustManager[] trustAllCerts = new TrustManager[] {
        new X509TrustManager() {
          public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
          }
          public void checkClientTrusted(
              java.security.cert.X509Certificate[] certs, String authType) {
          }
          public void checkServerTrusted(
              java.security.cert.X509Certificate[] certs, String authType) {
          }
        }
    };
    sc.init(null, trustAllCerts, new java.security.SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

  }

  private static String readStream(InputStream stream) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    int c;
    while((c=stream.read() ) > -1) {
      byteArrayOutputStream.write(c);
    }
    return new String(byteArrayOutputStream.toByteArray(), "UTF-8");
  }

  private static String getEmojiFromUnified(String unified) throws UnsupportedEncodingException {
    String[] splitted = unified.split("-");
    StringBuffer result = new StringBuffer();
    for(int i=0;i<splitted.length;i++) {
      char[] characters = Character.toChars(Integer.parseInt(splitted[i], 16));
      result.append(characters);
    }
    return result.toString();
  }

  private static String getEncodedFromUnified(String unified) throws UnsupportedEncodingException {
    String[] splitted = unified.split("-");
    StringBuffer result = new StringBuffer();
    for(int i=0;i<splitted.length;i++) {
      char[] characters = Character.toChars(Integer.parseInt(splitted[i], 16));
      for(char c:characters){
        result.append(StringEscapeUtils.escapeJava(Character.toString(c)));
      }
    }
    return result.toString();
  }

  public static void main(String[] args) throws Exception {
    initSSL();
    
    String jsonString = readStream(new URL(JSON_URL).openConnection().getInputStream());
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    jsonWriter.setIndent("  ");
    
    JSONArray emojis = new JSONArray(jsonString);

    jsonWriter.beginArray();
    for(int i=0; i<emojis.length(); i++) {
      JSONObject emoji = emojis.getJSONObject(i);
      
      jsonWriter.beginObject();
      String emojiChar = getEmojiFromUnified(emoji.getString("unified"));
      String escapedString = getEncodedFromUnified(emoji.getString("unified"));
      jsonWriter.name("emojiChar").value(emojiChar);
      jsonWriter.name("emoji").value(escapedString);
      jsonWriter.name("description").value("");

      jsonWriter.name("aliases").beginArray();
      for(int j=0; j<emoji.getJSONArray("short_names").length(); j++) {
        jsonWriter.value(emoji.getJSONArray("short_names").getString(j));
      }
      jsonWriter.endArray();

      jsonWriter.name("tags").beginArray().endArray();

      jsonWriter.endObject();
    }
    jsonWriter.endArray();

    jsonWriter.close();

    System.out.println(stringWriter.toString().replace("\\\\", "\\"));
  }

}
