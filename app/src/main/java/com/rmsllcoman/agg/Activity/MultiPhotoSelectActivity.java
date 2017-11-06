package com.rmsllcoman.agg.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import com.rmsllcoman.agg.R;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class MultiPhotoSelectActivity extends AppCompatActivity {

	private ImageAdapter imageAdapter;
	private static final int REQUEST_FOR_STORAGE_PERMISSION = 123;
    private ArrayList<String> imagesListShow = new ArrayList<>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_multi_photo_select);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        if (b != null) {
            imagesListShow = b.getStringArrayList("imageListShow");
        }

		populateImagesFromGallery();
	}

	public void btnChoosePhotosClick(View v){
		
		ArrayList<String> selectedItems = new ArrayList<>();

        if (imagesListShow != null && imagesListShow.size() > 0) {
            selectedItems.addAll(imagesListShow);
            selectedItems.addAll(imageAdapter.getCheckedItems());
        } else {
            selectedItems = imageAdapter.getCheckedItems();
        }

        Intent apply = new Intent(MultiPhotoSelectActivity.this, ImageAttachmentActivity.class);
        apply.putExtra("MULTIPLE", true);
        apply.putExtra("selectedItems", selectedItems);
        apply.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(apply);

		if (selectedItems!= null && selectedItems.size() > 0) {
			Toast.makeText(MultiPhotoSelectActivity.this, "Total photos selected: " + selectedItems.size(), Toast.LENGTH_SHORT).show();
			Log.d(MultiPhotoSelectActivity.class.getSimpleName(), "Selected Items: " + selectedItems.toString());
		}
	}

	private void populateImagesFromGallery() {
		if (!mayRequestGalleryImages()) {
			return;
		}
		ArrayList<String> imageUrls = loadPhotosFromNativeGallery();
		initializeRecyclerView(imageUrls);
	}

	private boolean mayRequestGalleryImages() {

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			return true;
		}

		if (checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
			return true;
		}

		if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
			//promptStoragePermission();
			showPermissionRationaleSnackBar();
		} else {
			requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, REQUEST_FOR_STORAGE_PERMISSION);
		}

		return false;
	}

	/**
	 * Callback received when a permissions request has been completed.
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
										   @NonNull int[] grantResults) {

		switch (requestCode) {

			case REQUEST_FOR_STORAGE_PERMISSION: {

				if (grantResults.length > 0) {
					if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
						populateImagesFromGallery();
					} else {
						if (ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)) {
							showPermissionRationaleSnackBar();
						} else {
							Toast.makeText(this, "Go to settings and enable permission", Toast.LENGTH_LONG).show();
						}
					}
				}

				break;
			}
		}
	}

	private ArrayList<String> loadPhotosFromNativeGallery() {
		final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
		final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
		Cursor imagecursor = managedQuery(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
				null, orderBy + " DESC");

		ArrayList<String> imageUrls = new ArrayList<String>();

		for (int i = 0; i < imagecursor.getCount(); i++) {
			imagecursor.moveToPosition(i);
			int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
			imageUrls.add(imagecursor.getString(dataColumnIndex));

			System.out.println("=====> Array path => "+imageUrls.get(i));
		}

		return imageUrls;
	}

	private void initializeRecyclerView(ArrayList<String> imageUrls) {
		imageAdapter = new ImageAdapter(this, imageUrls);

		RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),4);
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView.addItemDecoration(new ItemOffsetDecoration(this, R.dimen.item_offset));
		recyclerView.setAdapter(imageAdapter);
	}

	private void showPermissionRationaleSnackBar() {
		Snackbar.make(findViewById(R.id.button1), getString(R.string.permission_rationale),
				Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Request the permission
				ActivityCompat.requestPermissions(MultiPhotoSelectActivity.this,
						new String[]{READ_EXTERNAL_STORAGE},
						REQUEST_FOR_STORAGE_PERMISSION);
			}
		}).show();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == android.R.id.home) {
			// finish the activity
			onBackPressed();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}