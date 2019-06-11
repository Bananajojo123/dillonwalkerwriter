package com.writer.dillon;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import java.io.InputStream;
import java.io.FileOutputStream;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Metadata;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.TOCReference;

import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.epub.EpubWriter;
import java.io.File;
import java.io.FilenameFilter;


/**
 * A simple {@link Fragment} subclass.
 */
public class LibraryFragment  extends Fragment  {
    private String TAG = this.getClass().getSimpleName();
    private String root = Environment.getExternalStorageDirectory().toString();

    public LibraryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_library, container, false);

        FileFilter fileFilter = new FileFilter();
        File[] files = fileFilter.finder(root);

        for(File f : files){
            Log.i(TAG, getDetails(f.getAbsolutePath()));
        }



        return view;
    }

    private String getDetails(String dir){
        EpubReader epubReader = new EpubReader();
        try {
            Book book = epubReader.readEpub(new FileInputStream(dir));
            return(book.getTitle());
        }
        catch (FileNotFoundException ex){
            return ex.getMessage();

        }
        catch (IOException dfg){
            return dfg.getMessage();
        }
    }

}

