package com.leovalls.demo3.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.leovalls.demo3.R;
import com.leovalls.demo3.data.ImageAdapter;
import com.leovalls.demo3.fragments.FotoDialogFragment;
import com.leovalls.demo3.fragments.FotoDialogFragment.NoticeDialogListener;

public class MainActivity extends FragmentActivity implements OnClickListener,
																NoticeDialogListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		GridView gridView = (GridView)findViewById(R.id.grid);
		gridView.setAdapter(new ImageAdapter(this));
		
		Button btnFoto = (Button) findViewById(R.id.btnFoto);
		btnFoto.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		new FotoDialogFragment().show(getSupportFragmentManager(),"");
	}

	@Override
	public void onDialogPositiveClick() {
		Intent intent = new Intent(this, CameraActivity.class);
		startActivity(intent);
	}

	@Override
	public void onDialogNegativeeClick() {
		Toast.makeText(this, "hizo click en no", Toast.LENGTH_SHORT).show();
	}

}
