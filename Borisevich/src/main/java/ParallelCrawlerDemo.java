package main.java;


public class ParallelCrawlerDemo {

    private static final int NUM_OF_CRAWLERS = 5;
    private static final int NUM_OF_LINKS = 10;
    private static final String START_PAGE = "https://en.wikipedia.org/wiki/Interstellar_(film)";

    public static void main(String[] args) {
        SaveContent.resetArchive();

        long startTime = System.currentTimeMillis();

        LinksQueue linksQueue = new LinksQueue(NUM_OF_LINKS);
        Crawler[] crawlerList = new Crawler[NUM_OF_CRAWLERS];
        linksQueue.putNextLink(START_PAGE);

        for (int i = 0; i < crawlerList.length; i++) {
            crawlerList[i] = new Crawler(linksQueue);
            crawlerList[i].start();
        }

        for (Crawler aCrawlerList : crawlerList) {
            try {
                aCrawlerList.join();
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("\nRunning time: " + (endTime - startTime) + " ms");
    }
}
