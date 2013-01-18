package com.konichiwalivejason.www;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class OpponentProfileActivity extends Activity{

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oppo_profile_layout);
		
	}
	
	public void setGui(){
		ImageView iv_icon = (ImageView) findViewById(R.id.iv_profile_icon);
		TextView tv_profile = (TextView) findViewById(R.id.tv_profile_txt);
		Bitmap bitmap = (Bitmap) getIntent().getParcelableExtra("bitmapImage");
		iv_icon.setImageBitmap(bitmap);
		
		String profile_data = getIntent().getStringExtra("stuff");
		
		
		/*String gender = getIntent().getStringExtra("gender");
		int age = getIntent().getIntExtra("age", 0);
		String country = getIntent().getStringExtra("country");
		String occupation = getIntent().getStringExtra("occupation");
		String interest = getIntent().getStringExtra("interest");
		*/
		//String concat = "Gender: " + gender + "\nAge: " + age +"\nOccupation: " + occupation + "\nCountry: " + country + "\nInterest: " + interest;
		
		tv_profile.setText(profile_data);
		
	}

	
}
