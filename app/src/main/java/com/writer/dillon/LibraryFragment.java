package com.writer.dillon;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.folioreader.FolioReader;

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
    private RecyclerView recyclerView;

    Context context;
    public LibraryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        final ArrayList <Item> itemList = new ArrayList<Item>();


        final LibraryAdapter itemArrayAdapter = new LibraryAdapter(R.layout.library_layout, itemList);
        recyclerView = view.findViewById(R.id.recyclerViewLibrary);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        context = getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewLibrary);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemArrayAdapter);



        FileFilter fileFilter = new FileFilter();
        File[] files = fileFilter.finder(root);

        for(File f : files){
            itemList.add(new Item(f.getName(), f.getAbsolutePath()));
        }



        ItemClickSupport.addTo(recyclerView)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        FolioReader folioReader = FolioReader.get();
                        folioReader.openBook(itemList.get(position).getPath());
                    }
                });

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

