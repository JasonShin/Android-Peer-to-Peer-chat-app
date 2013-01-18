package com.konichiwalivejason.www;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.konichiwa.settings.BackgroundSetter;
import com.konichiwa.settings.MyProfile;

public class MyProfileActivity extends Activity implements OnItemClickListener, OnClickListener, OnCheckedChangeListener {

	private ArrayList<Item> items;
	private CustomProfileAdapter adapter;
	private MyProfile mp;
	private ListView lv;
	private Dialog dialog;
	private int currentAge;
	//private NumberPicker np;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_profile_layout);
		mp = new MyProfile(this);
		//setUpConfiguration();
		setListItems();

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	/*
	 * private static final String KEY_GENDER = "MY_PREF_GENDER"; private static final String KEY_COUNTRY = "MY_PREF_COUNTRY"; private static final String KEY_INTEREST = "MY_PREF_INTEREST"; private static final String KEY_AGE = "MY_PREF_AGE"; private static final String KEY_ICON = "MY_PREF_ICON"; private static final String KEY_BG = "MY_PREF_BG";
	 */
	public void setListItems() {
		lv = (ListView) findViewById(R.id.lv_my_profile_stuff);
		items = new ArrayList<Item>();
		items.add(new Item(1, "Age", Integer.toString(mp.getAge())));
		items.add(new Item(1, "Gender", mp.getGender()));
		items.add(new Item(1, "Country", mp.getCountry(getApplicationContext())));
		items.add(new Item(1, "Interest", mp.getInterest()));
		adapter = new CustomProfileAdapter(this, 0, items);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
	}
	public void setUpConfiguration() {

		new BackgroundSetter(new MyProfile(this).getBackground(), this, (LinearLayout) findViewById(R.id.ll_my_prof_bg));
	}

	private class Item {
		private int choice;
		private String title;
		private String value;

		public Item(int choice, String title, String value) {
			this.choice = choice;
			this.title = title;
			this.value = value;
		}

		public int getChoice() {
			return choice;
		}

		public String getTitle() {
			return title;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

	}

	static class ListHolder {
		TextView title;
		TextView value;
	}

	public class CustomProfileAdapter extends ArrayAdapter<Item> {
		private static final int REST_TEXTS = 1;

		Context context;
		ArrayList<Item> list;

		public CustomProfileAdapter(Context context, int resource, ArrayList<Item> l) {
			super(context, 0, l);
			this.context = context;
			this.list = l;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = null; //by forcing passed in row to be null, it will refresh all the time
			ListHolder holder = null;
			if (row == null) {

				LayoutInflater inflater = ((Activity) context).getLayoutInflater();
				int text_title = 0;
				int text_value = 0;

				switch (list.get(position).getChoice()) {
					case REST_TEXTS :
						row = inflater.inflate(R.layout.custom_my_prof_plain_row, parent, false);
						text_title = R.id.tv_my_prof_title;
						text_value = R.id.tv_my_prof_value;
						break;
				}
				holder = new ListHolder();

				holder.title = (TextView) row.findViewById(text_title);
				holder.value = (TextView) row.findViewById(text_value);
				holder.title.setText(list.get(position).getTitle());
				holder.value.setText(list.get(position).getValue());

				row.setTag(holder);
			}

			//Chats chat = list.get(position);

			return row;
		}
	}

	public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
		// TODO Auto-generated method stub
		dialog = new Dialog(MyProfileActivity.this);
		switch (position) {

			case 0 :
				dialog.setContentView(R.drawable.custom_my_profile_age);
				dialog.setTitle("Set age");
				dialog.show();

				Button bt_confirm = (Button) dialog.findViewById(R.id.bt_np_age_confirm);
				Button bt_cancel = (Button) dialog.findViewById(R.id.bt_np_age_cancel_backup);
				Button bt_increment = (Button) dialog.findViewById(R.id.but_prof_age_increment);
				Button bt_decrement = (Button) dialog.findViewById(R.id.but_prof_age_decrement);
				EditText et_age_value = (EditText) dialog.findViewById(R.id.et_age_value);
				currentAge = mp.getAge();
				et_age_value.setText(Integer.toString(mp.getAge()));
				bt_confirm.setOnClickListener(this);
				bt_cancel.setOnClickListener(this);
				bt_increment.setOnClickListener(this);
				bt_decrement.setOnClickListener(this);
				break;
			case 1 :
				dialog.setContentView(R.drawable.custom_dialog_my_profile_gender);
				dialog.setTitle("gender");
				RadioGroup genderGroup = (RadioGroup) dialog.findViewById(R.id.rg_gender);
				genderGroup.setOnCheckedChangeListener(this);
				setDefaultCheckedRadioButton(genderGroup);
				dialog.show();
				Button bt_gender_exit = (Button) dialog.findViewById(R.id.but_prof_gender_exit);
				bt_gender_exit.setOnClickListener(this);
				break;
			case 2 :
				//Country is set by default
				break;
			case 3 :
				dialog.setContentView(R.drawable.custom_dialog_my_profile_interest);
				dialog.setTitle("interest");
				dialog.show();
				Button bt_confirm_two = (Button) dialog.findViewById(R.id.but_prof_confirm);
				bt_confirm_two.setOnClickListener(this);
		}

	}
	private void setDefaultCheckedRadioButton(RadioGroup rg) {
		String gender = mp.getGender();
		if (gender.equals("male")) {
			rg.check(R.id.rb_gender_male);
		} else if (gender.equals("female")) {
			rg.check(R.id.rb_gender_female);
		} else {
			rg.check(R.id.rb_gender_hide);
		}
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
			case R.id.but_prof_age_increment :

				currentAge++;
				if (currentAge >= 999) {
					currentAge = 999;
				}

				break;
			case R.id.but_prof_age_decrement :
				currentAge--;
				if (currentAge < 0) {
					currentAge = 0;
				}
				break;
			case R.id.bt_np_age_confirm :
				dialog.dismiss();
				EditText et_age_value = (EditText) dialog.findViewById(R.id.et_age_value);
				currentAge = Integer.parseInt(et_age_value.getText().toString());
				break;
			case R.id.bt_np_age_cancel_backup :
				dialog.dismiss();
				break;
			case R.id.but_prof_gender_exit :
				dialog.dismiss();
				break;
			case R.id.but_prof_confirm :
				dialog.dismiss();
				EditText et_interest = (EditText) dialog.findViewById(R.id.et_my_prof_value);
				String text = et_interest.getText().toString();
				mp.setInterest(text);
				items.get(3).setValue(et_interest.getText().toString());
				break;
		}
		if (v.getId() == R.id.but_prof_age_increment || v.getId() == R.id.but_prof_age_decrement || v.getId() == R.id.bt_np_age_confirm) {
			EditText et_age_value = (EditText) dialog.findViewById(R.id.et_age_value);
			mp.setAge(Integer.parseInt(et_age_value.getText().toString()));
			items.get(0).setValue(Integer.toString(currentAge));
			et_age_value.setText(Integer.toString(currentAge));

		}

		adapter.notifyDataSetChanged();

	}
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
			case R.id.rb_gender_hide :
				mp.setGender("hidden");
				break;
			case R.id.rb_gender_female :
				mp.setGender("female");
				break;
			case R.id.rb_gender_male :
				mp.setGender("male");
				break;
		}
		items.get(1).setValue(mp.getGender());
		adapter.notifyDataSetChanged();
	}

}
