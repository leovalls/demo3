package com.leovalls.demo3.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.leovalls.demo3.R;
import com.leovalls.demo3.data.Helper;
import com.leovalls.demo3.data.Image;
import com.leovalls.demo3.data.ImageAdapter;
import com.leovalls.demo3.fragments.FotoDialogFragment;
import com.leovalls.demo3.fragments.FotoDialogFragment.NoticeDialogListener;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class MainActivity extends FragmentActivity 
							implements OnClickListener,
										NoticeDialogListener{
	
	Button btnFoto;
	Button btnUpdate;
	Button btnParse;
	GridView gridView;
	ProgressBar progressBar;
	ImageAdapter adapter;
	ArrayList<Image> imagesArray;
	
	public static RequestQueue resquestQueue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		resquestQueue = Volley.newRequestQueue(this);
		Parse.initialize(this, "hVz4jBrzACkIQ82abr8A5Z9R60GNoZZNRjeA4ip4", "z2c2KhzxRaMkf8LJLEbIld4uKqRwDOKJm17MMHhg");
		
		imagesArray = new ArrayList<Image>();
		adapter = new ImageAdapter(this,imagesArray);
		
		gridView = (GridView)findViewById(R.id.grid);
		gridView.setAdapter(adapter);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		
		btnFoto = (Button) findViewById(R.id.btnFoto);
		btnFoto.setOnClickListener(this);
		btnUpdate = (Button) findViewById(R.id.btnUpdate);
		btnUpdate.setOnClickListener(this);
		btnParse = (Button) findViewById(R.id.btnParse);
		btnParse.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		if (viewId == btnFoto.getId()){
			new FotoDialogFragment().show(getSupportFragmentManager(),"");
		}else if (viewId == btnUpdate.getId()){
			APICall();
		}else if (viewId == btnParse.getId()){
			parse();
		}
	}

	private void parse() {
		ParseObject test = new ParseObject("Prueba");
		test.put("nombre", "Er Leo");
		test.saveInBackground();
		Log.e("TAG", "Guardando...");
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Prueba");
		query.getInBackground("RGPX7bTmsQ", new GetCallback<ParseObject>() {
			
			@Override
			public void done(ParseObject obj, ParseException arg1) {
				if (obj != null){
					Toast.makeText(getApplicationContext(), obj.getString("nombre"), Toast.LENGTH_SHORT).show();
				}
			}
		});
//		
	}

	private void APICall() {
		String url = Helper.getRecentMediaUrl("venezuela");

		progressBar.setVisibility(View.VISIBLE);
		btnUpdate.setEnabled(false);
		
		Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>(){

			@Override
			public void onResponse(JSONObject resonse) {
				progressBar.setVisibility(View.GONE);
				btnUpdate.setEnabled(true);
				gridView.setVisibility(View.VISIBLE);	

				JSONArray data ;
				
				try {
					data = resonse.getJSONArray("data");
					for(int i = 0; i < data.length(); i++){
						JSONObject element = data.getJSONObject(i);
						String type = element.getString("type");
						if (type.equals("image")){
							JSONObject user = element.getJSONObject("user");
							JSONObject images = element.getJSONObject("images");
							JSONObject standarRes = images.getJSONObject("standard_resolution");
							
							String userName = user.getString("username");
							String imgUrl = standarRes.getString("url");
							
							Image imagen = new Image();
							imagen.setImageUrl(imgUrl);
							imagen.setUserName(userName);
							imagesArray.add(imagen);
						
						}
					}
					
					adapter.notifyDataSetChanged();
					showNotification();
					
				} catch (Exception e) {
					Log.e("ERROR", Log.getStackTraceString(e));
				}
				
			}
			
		} ;
		
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, listener, null);
		resquestQueue.add(request);
	}

	public void showNotification(){
		Intent result = new Intent(this, CameraActivity.class);
		
		TaskStackBuilder tsb = TaskStackBuilder.create(this);
		tsb.addParentStack(CameraActivity.class);
		tsb.addNextIntent(result);
		
		PendingIntent pendingIntent = tsb.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentTitle(getString(R.string.txt_notif_title))
			.setContentText(getString(R.string.txt_notif_subtitle));
		
		builder.setContentIntent(pendingIntent);
		
		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(1,builder.build());
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
