package org.rhw.bmr.project.service.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.rhw.bmr.project.service.UrlTitleService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@Service
public class UrlTitleServiceImpl implements UrlTitleService {
    @Override
    @SneakyThrows
    public String getTitleByUrl(String url) {

        URL targetUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) targetUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            Document document = Jsoup.connect(url).get();
            return document.title();
        }

        return "Error while fetching title.";
    }
}
