package com.leovalls.demo3.activities;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.leovalls.demo3.R;

public class CameraActivity extends Activity implements OnClickListener{

	private static final int LOAD_IMAGE = 1;
	private static final int CAMERA = 2;

	Button btnFromCamera;
	Button btnFromGallery;
	private String photoPath;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		
		btnFromCamera = (Button)findViewById(R.id.btnFromCamera);
		btnFromCamera.setOnClickListener(this);
		btnFromGallery = (Button)findViewById(R.id.btnFromGallery);
		btnFromGallery.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		int code = 0;
		if (v.getId()  == btnFromGallery.getId()){
			intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			code = LOAD_IMAGE;
		}else if (v.getId()  == btnFromCamera.getId()){
			File photo = setUpFile();
			photoPath = photo.getAbsolutePath();
			
			intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
			code = CAMERA;
		}

		startActivityForResult(intent, code);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case LOAD_IMAGE:
			if (resultCode == RESULT_OK){
				fromGallery(data);
			}
			break;
		case CAMERA:
			if (resultCode == RESULT_OK){
				fromCamera(data);
			}
			break;
		default:
			break;
		}
		
		if (requestCode == LOAD_IMAGE && 
				resultCode == RESULT_OK &&
				data != null){
			
		}
	}
	
	public void fromCamera(Intent data){
		ImageView imageView = (ImageView) findViewById(R.id.img);
		Bitmap bitmap = resizeBitmap(imageView.getWidth(), imageView.getHeight());
		imageView.setImageBitmap(bitmap);
	}
	
	public void fromGallery(Intent data){
		if (data != null){
			Uri selectedImage = data.getData();
			String[] filePathColum = {MediaStore.Images.Media.DATA};
			
			Cursor cursor = getContentResolver().query(selectedImage, filePathColum, null, null, null);
			if (cursor.moveToFirst()){
				int columnIndex = cursor.getColumnIndex(filePathColum[0]);
				String imgPath = cursor.getString(columnIndex);
				cursor.close();
				
				ImageView imageView = (ImageView) findViewById(R.id.img);
				imageView.setImageBitmap(BitmapFactory.decodeFile(imgPath));
			}
		}
	}

	
	private File setUpFile() {
		File albumDir;
		String albumName = "ejemplo";
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO){
			albumDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), albumName);
		} else {
			albumDir = new File(Environment.getExternalStorageDirectory()+"/dcim/" +  albumName);
		}
		albumDir.mkdirs();
		
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
									.format(Calendar.getInstance().getTime());
		
		String imgFileName = "IMG_" + timeStamp + ".jpg";
		File image = new File(albumDir + "/" + imgFileName);
		return image;
	}
	
	 public Bitmap resizeBitmap(int targetW, int targetH) {
		        BitmapFactory.Options options = new BitmapFactory.Options();
		        options.inJustDecodeBounds = true;
		        BitmapFactory.decodeFile(photoPath, options);
		        
				int height = options.outHeight;
				int width = options.outWidth;
				int scaleFactor = 1;
				
				if (targetW > 0 || targetH > 0) {
				    scaleFactor = Math.min(width/targetW, height/targetH);
				}
				options.inJustDecodeBounds = false;
				options.inSampleSize = scaleFactor;
				options.inPurgeable = true;
				
				return BitmapFactory.decodeFile(photoPath, options);
			}        
}
