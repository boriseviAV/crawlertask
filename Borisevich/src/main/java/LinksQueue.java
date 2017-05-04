package main.java;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

class LinksQueue {
    private int maxNumOfLinks;

    private int nextLink = 0;
    private Semaphore linksAvailable = new Semaphore(0);
    private final Object lock = new Object();
    private ArrayList<String> linksList = new ArrayList<String>();

    LinksQueue(int maxNumOfLinks) {
        this.maxNumOfLinks = maxNumOfLinks;
    }

    boolean putNextLink(String link) {
        if (link == null || link.equals("")) return false;

        synchronized (lock) {
            if (linksList.size() >= maxNumOfLinks) {
                return false;
            }

            for (String iterator : linksList) {
                if (iterator.equals(link)) {
                    return false;
                }
            }

            // Add link to database
            linksList.add(link);

            // Print as you go
            System.out.println(linksList.size() + ": " + link);
        }

        linksAvailable.release();
        return true;
    }



    String getNextLink() throws InterruptedException {
        linksAvailable.acquire();

        synchronized (lock) {
//            if (linksList.size() >= maxNumOfLinks) {
//                linksAvailable.release();
//                return null;
//            }

            String link = null;
            if (nextLink == linksList.size()) {
                link = null;
                nextLink++;
            }
            else
                if (nextLink < linksList.size())
                    link = linksList.get(nextLink++);

            if (nextLink == linksList.size()) {
                linksAvailable.release();
            }

            return link;
        }
    }

}
