package dev.interfiber.karpet.installer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class DownloadURLS {

    public static HashMap<String, String> downloadUrls = new HashMap<>();
    public String releaseJSON = "https://karpet-services.surge.sh/releases.json";
    public String[] installOptions;
    public String defaultVersion = null;
    public DownloadURLS() throws IOException {
        URL url = new URL(releaseJSON);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setConnectTimeout(1000);
        urlConnection.setReadTimeout(1000);
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

        StringBuilder releaseJSONData = new StringBuilder();

        String line;
        while((line = reader.readLine()) != null) {
            releaseJSONData.append(line);
        }
        JSONObject releaseData = new JSONObject(releaseJSONData.toString());
        JSONArray releases = releaseData.getJSONArray("versions");
        this.installOptions = new String[releases.length()];
        this.defaultVersion = releaseData.getString("latest_version");
        for (int i = 0; i < releases.length(); i++) {
            String version = releases.getJSONObject(i).getString("version");
            String downloadUrl = releases.getJSONObject(i).getString("url");
            downloadUrls.put(version, downloadUrl);
            this.installOptions[i] = version;
        }
    }

    public static String versionToUrl(String version){
        String url = downloadUrls.get(version);
        if (url == null){
            throw new RuntimeException("Failed to find version download URL");
        } else {
            return url;
        }
    }
}
