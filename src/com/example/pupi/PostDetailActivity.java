package com.example.pupi;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PostDetailActivity extends Activity {
	
	Intent intent;
	TextView poster;
	private ProgressDialog mDilg;
	private MediaPlayer success_sound;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_post_detail);
		poster = (TextView)findViewById(R.id.txt_post_detail_name);
		TextView title = (TextView)findViewById(R.id.txt_listItem_post_title);
		TextView helper = (TextView)findViewById(R.id.txt_post_detail_hel2);
		TextView reward = (TextView)findViewById(R.id.txt_post_detail_reward2);
		TextView loc = (TextView)findViewById(R.id.txt_post_detail_location2);
		TextView des = (TextView)findViewById(R.id.txt_post_detail_des);
		
		intent = getIntent();
		poster.setText(intent.getStringExtra("POSTER"));
		title.setText(intent.getStringExtra("TITLE"));
		helper.setText(intent.getStringExtra("HELPER"));
		reward.setText(intent.getStringExtra("REWARD"));
		loc.setText(intent.getStringExtra("LOCATION"));
		des.setText(intent.getStringExtra("CONTENT"));
		Button help = (Button)findViewById(R.id.btn_go_to_help);
		
		
		ImageView dialog_profile_img = (ImageView)findViewById(R.id.img_post_detail_picture);
		String poster_name = intent.getStringExtra("POSTER");
		
		if(poster_name.equalsIgnoreCase("zheng")){
			dialog_profile_img.setImageResource(R.drawable.zheng);
		}else if(poster_name.equalsIgnoreCase("sun")){
			dialog_profile_img.setImageResource(R.drawable.sun);
		}else if(poster_name.equalsIgnoreCase("mao")){
			dialog_profile_img.setImageResource(R.drawable.mao);
		}else if(poster_name.equalsIgnoreCase("king")){
			dialog_profile_img.setImageResource(R.drawable.king);
		}else{
			dialog_profile_img.setImageResource(R.drawable.guest);
		}
		
		 success_sound = MediaPlayer.create(this, R.raw.success_sound);
	}
	
	public void help(View view){
		if(MainActivity.userId.equals(poster.getText().toString())){
			Toast.makeText(getApplicationContext(), "This is your own post!", Toast.LENGTH_SHORT).show();
			return;
		}
		mDilg= ProgressDialog.show(this,"","Processing...",false,true);
		new AsyncHelpAgent().execute(this);
	}
	
	private class AsyncHelpAgent extends AsyncTask{

		@Override
		protected Object doInBackground(Object... params) {
			String post_id = String.valueOf(intent.getStringExtra("POST_ID"));
			String resultString = null;
			List<NameValuePair> nameValPair = new ArrayList<NameValuePair>();
			nameValPair.add(new BasicNameValuePair("post_id", post_id));
			nameValPair.add(new BasicNameValuePair("helper",MainActivity.userId));
			resultString = PHPLoader.getStringFromPhp(PHPLoader.HELP_PHP,nameValPair);
			return resultString;
		}

		@Override
		protected void onPostExecute(Object result) {
		//	dilg_progress.dismiss();
			mDilg.dismiss();
			if(((String)result).contains("success")){
				Toast.makeText(getApplicationContext(), "Success! Thank you!", Toast.LENGTH_SHORT).show();
				success_sound.start();
				finish();
			}
			if(((String)result).contains("fail")){
				Toast.makeText(getApplicationContext(), "Someone is helping!", Toast.LENGTH_SHORT).show();
				finish();
			}
		}
	}

}
