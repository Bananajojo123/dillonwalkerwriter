package com.writer.dillon;

import java.io.File;
import java.io.FilenameFilter;

public class FileFilter {

    public File[] finder(String dirName){
        File dir = new File(dirName);

        return dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename)
            {
                return filename.endsWith(".epub");
            }

        });
    }

}