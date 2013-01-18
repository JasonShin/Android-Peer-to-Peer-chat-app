package com.konichiwalivejason.www;

import java.util.ArrayList;
import java.util.Arrays;

import com.konichiwalivejason.www.R;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class LobbyActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lobbylayout);
		this.setTabhost();
		this.setListView();
		this.setContents();
	}

	// -------------------------------------------------------------------------------------

	private void setTabhost() {

		TabHost th = (TabHost) findViewById(R.id.tabhost);
		th.setup();

		// one on one chat
		TabSpec specs = th.newTabSpec("one");
		specs.setIndicator("");
		specs.setContent(R.id.first_content);
		th.addTab(specs);
		RelativeLayout tabLayout1 = (RelativeLayout) th.getTabWidget().getChildAt(0);
		tabLayout1.setBackgroundDrawable(getResources().getDrawable(R.drawable.one_tab_indicator));

		// group chat
		TabSpec specs2 = th.newTabSpec("one");
		specs2.setIndicator("");
		specs2.setContent(R.id.second_content);
		th.addTab(specs2);
		RelativeLayout tabLayout2 = (RelativeLayout) th.getTabWidget().getChildAt(1);
		tabLayout2.setBackgroundDrawable(getResources().getDrawable(R.drawable.group_tab_indicator));

		// settings
		TabSpec specs3 = th.newTabSpec("one");
		specs3.setIndicator("");
		specs3.setContent(R.id.third_content);
		th.addTab(specs3);
		RelativeLayout tabLayout3 = (RelativeLayout) th.getTabWidget().getChildAt(2);
		tabLayout3.setBackgroundDrawable(getResources().getDrawable(R.drawable.setting_tab_indicator));

	}

	// -------------------------------------------------------------------------------------

	private void setListView() {
		Resources aResource = this.getResources();
		String myList[] = aResource.getStringArray(R.array.settings_stuffs);
		ArrayList<String> al = new ArrayList<String>(Arrays.asList(myList));
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al);

		ListView listview = (ListView) findViewById(R.id.lv);
		listview.setAdapter(adapter);
	}

	// -------------------------------------------------------------------------------------

	private void setContents() {
		Button oneChatStart = (Button) findViewById(R.id.but_startonechat);
		oneChatStart.setOnClickListener(this);

	}

	// -------------------------------------------------------------------------------------

	public void onClick(View a) {
		// TODO Auto-generated method stub
		int id = a.getId();
		switch (id) {
			case R.id.but_startonechat :
				Intent onechat = new Intent("android.intent.action.ONEONONE");
				startActivity(onechat);
				break;
		}

	}
	
	// -------------------------------------------------------------------------------------

}
