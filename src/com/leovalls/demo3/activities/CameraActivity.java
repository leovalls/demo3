package com.leovalls.demo3.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.leovalls.demo3.R;

public class CameraActivity extends Activity implements OnClickListener{

	private static final int LOAD_IMAGE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		
		Button btnFromGallery = (Button)findViewById(R.id.btnFromGallery);
		btnFromGallery.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, LOAD_IMAGE);
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		 
		if (requestCode == LOAD_IMAGE && 
				resultCode == RESULT_OK &&
				data != null){
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

}
