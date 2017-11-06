package com.rmsllcoman.agg.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import com.rmsllcoman.agg.R;

public class ImageAttachmentActivity extends AppCompatActivity implements Imageutils.ImageAttachmentListener, View.OnClickListener {

    LinearLayout back;
    LinearLayout delete;
    LinearLayout thumbnail;
    LinearLayout upload;
    Imageutils imageutils;
    ImageView iv_attachment;
    String currentImageShown;
    Bitmap add;
    Bitmap add1;

    int height;
    int width;
    int height1;
    int width1;
    boolean fromCamera = false;
    boolean fromImage = false;
    boolean fromMulti = false;
    private ArrayList<String> imagesList = new ArrayList<>();
    private ArrayList<String> imagesListShow = new ArrayList<>();
    private ArrayList<String> imagesListUpload = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_attachment);

        imageutils =new Imageutils(this);
        thumbnail = (LinearLayout) findViewById(R.id.linear);
        back = (LinearLayout) findViewById(R.id.back);
        delete = (LinearLayout) findViewById(R.id.delete);
        upload = (LinearLayout) findViewById(R.id.upload);
        iv_attachment=(ImageView)findViewById(R.id.imageView);

        iv_attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        add = BitmapFactory.decodeResource(
                getResources(), R.drawable.button_add);
        height = add.getHeight();
        width = add.getWidth();

        add1 = BitmapFactory.decodeResource(
                getResources(), R.drawable.ph_add_image);
        height1 = add1.getHeight();
        width1 = add1.getWidth();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagesListUpload.isEmpty()) {
                    imagesListUpload = OwnerDetailsActivity.imagesList;
                }
                Intent apply = new Intent(ImageAttachmentActivity.this, OwnerDetailsActivity.class);
                apply.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                apply.putStringArrayListExtra("selectedItems", imagesListUpload);
                startActivity(apply);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < imagesListShow.size(); i++) {
                    if (imagesListShow.get(i).equalsIgnoreCase(currentImageShown)) {
                        imagesListShow.remove(i);
                        imagesList.remove(i);
                    }
                }
                delete_image_attachment(imagesList);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagesListUpload = imagesListShow;
                Intent apply = new Intent(ImageAttachmentActivity.this, OwnerDetailsActivity.class);
                apply.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                apply.putStringArrayListExtra("selectedItems", imagesListShow);
                if (fromCamera)
                    apply.putExtra("isCamera", true);
                else if (fromMulti)
                    apply.putExtra("isCamera", false);
                startActivity(apply);
            }
        });

        Intent i = getIntent();
        Bundle b = i.getExtras();
        if (b != null) {
            fromCamera = b.getBoolean("FROMCAMERA");
            fromImage = b.getBoolean("FROMIMAGE");
            fromMulti = b.getBoolean("MULTIPLE");
            if (fromCamera) {
                if (imageutils.isDeviceSupportCamera()){
                    imageutils.launchCamera(1);
                } else {
                    Toast.makeText(this, "Camera Feature is not Available", Toast.LENGTH_LONG).show();
                }
            } else if (fromImage){
                boolean isCamera = b.getBoolean("isCamera");
                if (isCamera) {
                    fromCamera = true;
                } else {
                    fromCamera = false;
                }
                ArrayList<String> selectedItems = b.getStringArrayList("selectedItems");
                gallery_image_attachment(selectedItems);
            } else if (fromMulti) {
                ArrayList<String> selectedItems = b.getStringArrayList("selectedItems");
                gallery_image_attachment(selectedItems);

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            imageutils.onActivityResult(requestCode, resultCode, data);
        } else {
            finish();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        imageutils.request_permission_result(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == imagesList.size()-1) {
            if(fromCamera) {
                imageutils.launchCamera(1);
            } else {
                Intent apply = new Intent(ImageAttachmentActivity.this, MultiPhotoSelectActivity.class);
                apply.putExtra("FROMCAMERA", false);
                apply.putStringArrayListExtra("imageListShow", imagesListShow);
                apply.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(apply);
            }
        } else {
            currentImageShown = imagesListShow.get(v.getId());
            Glide.with(ImageAttachmentActivity.this)
                    .load("file://"+imagesListShow.get(v.getId()))
                    .crossFade()
                    .override(width1,height1)
                    .placeholder(R.drawable.ph_add_image)
                    .into(iv_attachment);
        }
    }

    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
        thumbnail.removeAllViews();
        iv_attachment.setImageBitmap(file);
        if (imagesList.size() != 0) {
            imagesList.remove(imagesList.size()-1);
        }

        File file1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), filename);
        imagesList.add(String.valueOf(file1));
        imagesListShow.add(String.valueOf(file1));
        currentImageShown = String.valueOf(file1);

        if (OwnerDetailsActivity.imagesList.size() > 0) {
            boolean added = false;
            for (int i = 0; i < imagesListShow.size(); i++) {
                if (imagesListShow.get(i).equalsIgnoreCase(OwnerDetailsActivity.imagesList.get(0))) {
                    added = true;
                }
            }
            if (!added){
                imagesList.addAll(OwnerDetailsActivity.imagesList);
                imagesListShow.addAll(OwnerDetailsActivity.imagesList);
            }
        }

        int resourceId = R.drawable.button_add;
        imagesList.add(String.valueOf(resourceId));

        for (int i = 0; i < imagesList.size(); i++) {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(2, 2, 2, 2);
            imageView.setOnClickListener(this);
            if (i == imagesList.size()-1) {
                Glide.with(ImageAttachmentActivity.this)
                        .load(resourceId)
                        .crossFade()
                        .override(width,height)
                        .placeholder(R.drawable.preview)
                        .into(imageView);
            } else {
                Glide.with(ImageAttachmentActivity.this)
                        .load("file://"+imagesList.get(i))
                        .crossFade()
                        .override(width,height)
                        .placeholder(R.drawable.preview)
                        .into(imageView);
            }
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            thumbnail.addView(imageView);

            imageView.requestLayout();
            imageView.getLayoutParams().height = height;
            imageView.getLayoutParams().width = width;
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }

    public void gallery_image_attachment(ArrayList<String> filename) {
        thumbnail.removeAllViews();
        if (imagesList.size() != 0) {
            imagesList.remove(imagesList.size()-1);
        }

        for (int i = 0; i < filename.size(); i++) {
            imagesList.add(filename.get(i));
            imagesListShow.add(filename.get(i));
            if (i == filename.size()-1) {
                currentImageShown = filename.get(i);
                Glide.with(ImageAttachmentActivity.this)
                        .load("file://"+filename.get(i))
                        .crossFade()
                        .override(width1,height1)
                        .placeholder(R.drawable.ph_add_image)
                        .into(iv_attachment);
            }
        }

        if (fromMulti && OwnerDetailsActivity.imagesList.size() > 0) {
            boolean added = false;
            for (int i = 0; i < imagesListShow.size(); i++) {
                if (imagesListShow.get(i).equalsIgnoreCase(OwnerDetailsActivity.imagesList.get(0))) {
                    added = true;
                }
            }
            if (!added){
                imagesList.addAll(OwnerDetailsActivity.imagesList);
                imagesListShow.addAll(OwnerDetailsActivity.imagesList);
            }
        }

        int resourceId = R.drawable.button_add;
        imagesList.add(String.valueOf(resourceId));

        for (int i = 0; i < imagesList.size(); i++) {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(2, 2, 2, 2);
            imageView.setOnClickListener(this);
            if (i == imagesList.size()-1) {
                Glide.with(ImageAttachmentActivity.this)
                        .load(resourceId)
                        .crossFade()
                        .override(width,height)
                        .placeholder(R.drawable.preview)
                        .into(imageView);
            } else {
                Glide.with(ImageAttachmentActivity.this)
                        .load("file://"+imagesList.get(i))
                        .crossFade()
                        .override(width,height)
                        .placeholder(R.drawable.preview)
                        .into(imageView);
            }
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            thumbnail.addView(imageView);

            imageView.requestLayout();
            imageView.getLayoutParams().height = height;
            imageView.getLayoutParams().width = width;
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }

    public void delete_image_attachment(ArrayList<String> filename) {
        thumbnail.removeAllViews();

        for (int i = 0; i < filename.size(); i++) {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(2, 2, 2, 2);
            imageView.setOnClickListener(this);
            if (i == filename.size()-2) {
                currentImageShown = filename.get(i);
                Glide.with(ImageAttachmentActivity.this)
                        .load("file://"+filename.get(i))
                        .crossFade()
                        .override(width1,height1)
                        .placeholder(R.drawable.ph_add_image)
                        .into(iv_attachment);
                Glide.with(ImageAttachmentActivity.this)
                        .load("file://"+filename.get(i))
                        .crossFade()
                        .override(width,height)
                        .placeholder(R.drawable.preview)
                        .into(imageView);
            } else if (i == filename.size()-1){
                Glide.with(ImageAttachmentActivity.this)
                        .load(Integer.parseInt(filename.get(i)))
                        .crossFade()
                        .override(width,height)
                        .placeholder(R.drawable.preview)
                        .into(imageView);
            } else {
                Glide.with(ImageAttachmentActivity.this)
                        .load("file://"+filename.get(i))
                        .crossFade()
                        .override(width,height)
                        .placeholder(R.drawable.preview)
                        .into(imageView);
            }

            if (filename.size() == 1) {
                currentImageShown = filename.get(i);
                Glide.with(ImageAttachmentActivity.this)
                        .load(R.drawable.ph_add_image)
                        .crossFade()
                        .override(width1,height1)
                        .placeholder(R.drawable.ph_add_image)
                        .into(iv_attachment);
            }

            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            thumbnail.addView(imageView);

            imageView.requestLayout();
            imageView.getLayoutParams().height = height;
            imageView.getLayoutParams().width = width;
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }
}
