package main.java;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Crawler extends Thread {
    private LinksQueue linksQueue;

    Crawler(LinksQueue linksQueue) {
        this.linksQueue = linksQueue;
    }


    private String downloadPage(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();

        String fileName = doc.title() + ".html";
        String content = doc.html();
        SaveContent.saveToFile(fileName, content);

        return content;
    }


    private String getAbsoluteUrl(URL url, String link) {

        if (link == null || link.equals("")) return null;
        if (link.matches("javascript:.*|mailto:.*")) return null;

        if (link.matches("https?://.*")) return link;

        String root;
        if ('#' == link.charAt(0) || '?' == link.charAt(0)) {
            root = url.toString();
        }
        else if ('/' == link.charAt(0)) {
            root = url.getProtocol() + "://" + url.getHost();
        }
        else {
            root = url.toString().substring(0, url.toString().lastIndexOf("/"));
        }

        return root + link;
    }


    private void parseLinks(URL url, String html) {
        Pattern tagPattern = Pattern.compile("<a\\b[^>]*href=[\"'][^>]*>(.*?)</a>");
        Pattern linkPattern = Pattern.compile("href=[\"'][^>\"']*"); // Does not include last quotation mark

        Matcher tagMatcher = tagPattern.matcher(html);

        while (tagMatcher.find()) {
            String tag = tagMatcher.group();
            Matcher linkMatcher = linkPattern.matcher(tag);

            if (linkMatcher.find()) {
                String link = linkMatcher.group().replaceFirst("href=[\"']", "");

                link = getAbsoluteUrl(url, link);

                linksQueue.putNextLink(link);
            }
        }

    }

    @Override
    public void run() {
        while (true) {
            try {
                String link = linksQueue.getNextLink();
                if (link == null || link.equals(""))
                    break;

                URL url = new URL(link);
                String html = downloadPage(link);

                parseLinks(url, html);

            } catch (IOException e) {
                System.err.println(e.getMessage());
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
    }

}
