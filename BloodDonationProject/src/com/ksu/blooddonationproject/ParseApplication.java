package com.ksu.blooddonationproject;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.PushService;

public class ParseApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		/*
		 * Add Parse initialization code here
		 */

		// TODO: Add your own application ID and client key!
		Parse.initialize(this, "4KowakwOZcKjIlpgNZtj0SQzXUuVLlcfQgiiy6Db", "G9x1aqXuTVL0lMRiHIPEM2yRUSDG96Z3UKKlzmBo");

		//PushService.setDefaultPushCallback(this, MainActivity.class);
	    PushService.setDefaultPushCallback(this, MessagesListView.class, R.drawable.logo);
	    ParseInstallation.getCurrentInstallation().saveInBackground();
	}
	
	public static void updateParseInstallation(ParseUser user) {
		ParseInstallation installation = ParseInstallation.getCurrentInstallation();
		installation.put("userId", user.getObjectId());
		installation.saveInBackground();
	}
}
