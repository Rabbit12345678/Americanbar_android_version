package com.spaculus.americanbars;

import java.util.Date;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;

public class MyGcmListenerService extends GcmListenerService {

	@SuppressWarnings("unused")
	private static final String TAG = "MyGcmListenerService";

	@Override
	public void onMessageReceived(String from, Bundle data) {
		String message = "", subject = "", type = "";
		try {
			message = data.getString("message");
			subject = data.getString("subject");
			type = data.getString("type");
		} catch (Exception e) {
			e.printStackTrace();
		}
		sendNotification(message, subject, type);
	}

	private void sendNotification(String message, String subject, String type) {
		try {
			Intent intent = new Intent(MyGcmListenerService.this,
					PushNotificationDialog.class);
			intent.putExtra("subject", subject);
			intent.putExtra("message", message);
			intent.putExtra("type", type);
			//intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			long time = new Date().getTime();
			String tmpStr = String.valueOf(time);
			String last4Str = tmpStr.substring(tmpStr.length() - 5);
			int notificationId = Integer.valueOf(last4Str);
			PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationId,
					intent,PendingIntent.FLAG_ONE_SHOT);
			Uri defaultSoundUri = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(
					this)
					.setSmallIcon(R.drawable.app_icon)
					.setContentTitle(
							this.getString(R.string.app_name) + " - " + message)
					.setContentText(message).setAutoCancel(true)
					.setSound(defaultSoundUri).setContentIntent(pendingIntent);
			
			notificationManager.notify(notificationId,
					notificationBuilder.build());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}