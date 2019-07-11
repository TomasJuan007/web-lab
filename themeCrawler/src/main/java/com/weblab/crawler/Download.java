package com.weblab.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

class Download {

    private boolean checkURL(URL url) {
        String s = url.toString();
        return !s.endsWith(".zip") && !s.endsWith(".gz")
                && !s.endsWith(".rar") && !s.endsWith(".exe")
                && !s.endsWith(".jpg") && !s.endsWith(".png")
                && !s.endsWith(".tar") && !s.endsWith(".chm")
                && !s.endsWith(".iso") && !s.endsWith(".gif")
                && !s.endsWith(".csv") && !s.endsWith(".pdf")
                && !s.endsWith(".doc");
    }

    String downloadHttp(URL url) {
        boolean isOK = checkURL(url);
        if (!isOK) {
            return null;
        }
        StringBuilder content = new StringBuilder();
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpConstants.HTTP_NOTFOUND
                    || responseCode == HttpConstants.HTTP_FORBIDDEN) {
                return null;
            }

            boolean hasCharset = false;
            String value = connection.getHeaderField("Content-Type");
            if (value != null) {
                int index = value.indexOf("charset=");
                if (index >= 0) {
                    value = value.substring(index + 8);
                    hasCharset = true;
                }
            }

            BufferedReader reader;
            if (hasCharset) {
                reader = new BufferedReader(new InputStreamReader(
                        connection.getInputStream(), value));
            } else {
                reader = new BufferedReader(new InputStreamReader(
                        connection.getInputStream(), StandardCharsets.ISO_8859_1));
            }
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            if (!hasCharset) {
                int index = content.indexOf("charset=");
                if (index >= 0) {
                    String charset = content.substring(index + 8, index + 13)
                            .split("\"")[0];
                    byte[] b;
                    b = content.toString().getBytes(StandardCharsets.ISO_8859_1);
                    return new String(b, charset);
                }
            }
            return content.toString();
        }
        catch (IOException e) {
            return null;
        }
    }
}
