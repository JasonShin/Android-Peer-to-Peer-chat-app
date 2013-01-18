package com.konichiwa.settings;

import com.konichiwalivejason.www.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.LinearLayout;

public class BackgroundSetter {


	public BackgroundSetter(int index, Context context, LinearLayout bgPanel){
		
		Log.i("cb", "changing bg: " + index);
		Drawable d = null;
		switch(index){
			case 0:
				d = context.getResources().getDrawable(R.drawable.chat_back_repeat_one);
				break;
			case 1:
				d = context.getResources().getDrawable(R.drawable.chat_back_repeat_two);
				break;
			case 2:
				d = context.getResources().getDrawable(R.drawable.chat_back_repeat_three);
				break;
		}
		new MyProfile(context).setBackground(index);
		bgPanel.setBackgroundDrawable(d);
	}
}
