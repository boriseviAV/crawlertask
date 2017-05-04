package main.java;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

class SaveContent {

    private static final String ARCHIVE_NAME = "pages";
    private static File file = new File(ARCHIVE_NAME + "/");

    static void resetArchive() {
        try {
            FileUtils.deleteDirectory(file);
            file.mkdirs();
        } catch (IOException e) {
            System.err.println("Archive removing failed!");
        }
    }

    static synchronized void saveToFile(String fileName, String html) {
        PrintWriter out = null;
        try {
            out = new PrintWriter(ARCHIVE_NAME + "/" + fileName);
            out.println(html);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
        finally {
            if (out != null)
                out.close();
        }
    }

}
