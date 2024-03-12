package com.app.printscreen;

import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManagerRecyclerView;
    private RecyclerViewCustomAdapter recyclerViewCustomAdapter;
    private File filePDFOutput;
    private ScrollView scrollable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,
                new String[]{READ_MEDIA_IMAGES, WRITE_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);

        recyclerView = findViewById(R.id.recyclerView);
        scrollable = findViewById(R.id.scrollable);

        // Liner Layout
//        layoutManagerRecyclerView = new LinearLayoutManager(MainActivity.this);
        // GRID Layout
        layoutManagerRecyclerView = new GridLayoutManager(MainActivity.this, 3);

        recyclerView.setLayoutManager(layoutManagerRecyclerView);

        StorageManager storageManager = (StorageManager) getSystemService(STORAGE_SERVICE);
        StorageVolume storageVolume = storageManager.getStorageVolumes().get(0); // internal memory/ storage

        File fileImage = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            fileImage = new File(storageVolume.getDirectory().getPath() + "/Download/images.jpeg");
        }
        File fileImage1 = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            fileImage1 = new File(storageVolume.getDirectory().getPath() + "/Download/images1.jpeg");
        }

        Bitmap bitmap = BitmapFactory.decodeFile(fileImage.getPath());
        Bitmap bitmap1 = BitmapFactory.decodeFile(fileImage1.getPath());

        Bitmap[] bitmaps = {bitmap, bitmap1 ,bitmap, bitmap, bitmap,bitmap, bitmap, bitmap,bitmap, bitmap, bitmap,bitmap, bitmap, bitmap,bitmap, bitmap, bitmap,bitmap, bitmap, bitmap,bitmap, bitmap, bitmap,bitmap, bitmap, bitmap};
        recyclerViewCustomAdapter = new RecyclerViewCustomAdapter(bitmaps);
        recyclerView.setAdapter(recyclerViewCustomAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            filePDFOutput = new File(storageVolume.getDirectory().getPath() + "/Download/doc_print.pdf");
        }
    }

    public void buttonPrint(View view) throws IOException {
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(recyclerView.getWidth(),
                recyclerView.getHeight(),
                1).create();

        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        recyclerView.draw(page.getCanvas());
        pdfDocument.finishPage(page);
        pdfDocument.writeTo(new FileOutputStream(filePDFOutput));
        pdfDocument.close();
    }

}