package com.konichiwa.settings;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.konichiwalivejason.www.R;

public class PanelEventHandler implements OnClickListener {

	private LinearLayout bgPanel;
	private Context context;
	private Dialog dialog;
	
	public PanelEventHandler(Context c, LinearLayout l, Dialog dialog, int position) {
		this.bgPanel = l;
		this.context = c;
		this.dialog = dialog;
		switch (position) {
			case 0 :
				this.dialog.setContentView(R.drawable.custom_dialog_background_layout);
				this.dialog.setTitle("Backgrounds!");
				Log.i("se", "works 2");
				ImageView bg1 = (ImageView) dialog.findViewById(R.id.bg_one);
				ImageView bg2 = (ImageView) dialog.findViewById(R.id.bg_two);
				ImageView bg3 = (ImageView) dialog.findViewById(R.id.bg_three);
				bg1.setOnClickListener(this);
				bg2.setOnClickListener(this);
				bg3.setOnClickListener(this);

				bg1.setTag("a0");
				bg2.setTag("a1");
				bg3.setTag("a2");
				Log.i("se", "works 3");
				this.dialog.show();
				break;
			case 1 :
				Intent profileIntent = new Intent("android.intent.action.MYPROFILE");
				Log.i("prof", "works1");
				profileIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Log.i("prof", "works2");
				context.startActivity(profileIntent);
				break;
			case 2 :
				break;
			case 3 :
				break;
		}
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		char key = v.getTag().toString().charAt(0);
		int index = Integer.parseInt(""+v.getTag().toString().charAt(1));
		switch (key) {
			case 'a' :
				new BackgroundSetter(index, context, this.bgPanel);
				break;
			case 'b' :
				break;
			case 'c' :
				break;
			case 'd' :
				break;
		}
		this.dialog.dismiss();
	}

	
}
