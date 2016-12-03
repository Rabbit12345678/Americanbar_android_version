package com.spaculus.americanbars;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class PushNotificationDialog extends Activity {
	@SuppressWarnings("unused")
	private String type = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_error);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		Window window = getWindow();
		window.setBackgroundDrawable(new ColorDrawable(
				android.graphics.Color.TRANSPARENT));
		lp.copyFrom(window.getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
		window.setAttributes(lp);
		try {
			type = getIntent().getExtras().getString("type");
		} catch (Exception e) {
			e.printStackTrace();
		}
		setViews();
	}

	private void setViews() {
		try {
			TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
			tvTitle.setText(getString(R.string.app_name) + " - "
					+ getIntent().getExtras().getString("subject"));

			TextView tvMessage = (TextView) findViewById(R.id.tvFullDescription);
			tvMessage.setText(getIntent().getExtras().getString("message"));

			Button btnOk = (Button) findViewById(R.id.btnOk);
			btnOk.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						if (isTaskRoot()) {
							closeActivity();
							startActivity(new Intent(getContext(),
									SplashScreenActivity.class));
						} else {
							closeActivity();
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			Button btnCancel = (Button) findViewById(R.id.btnCancel);
			btnCancel.setVisibility(View.GONE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Context getContext() {
		return PushNotificationDialog.this;
	}

	protected void closeActivity() {
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			closeActivity();
		}
		return super.onKeyDown(keyCode, event);
	}

	/*
	 * public boolean isAppRunning(Context context) {
	 * 
	 * // check with the first task(task in the foreground) // in the returned
	 * list of tasks ActivityManager activityManager = (ActivityManager) context
	 * .getSystemService(Context.ACTIVITY_SERVICE);
	 * 
	 * List<RunningTaskInfo> services = activityManager
	 * .getRunningTasks(Integer.MAX_VALUE);
	 * 
	 * Log.i("Service List Length is:", "" + services.size());
	 * 
	 * for (int i = 0; i < services.size(); i++) { Log.i("i:", "" + i);
	 * Log.i("services.get(i)", services.get(i).toString()); }
	 * 
	 * 
	 * ActivityManager activityManager = (ActivityManager)
	 * getApplicationContext() .getSystemService(Context.ACTIVITY_SERVICE);
	 * List<ActivityManager.RunningTaskInfo> runningTasks = activityManager
	 * .getRunningTasks(1);
	 * 
	 * @SuppressWarnings("unused") ComponentName topActivity =
	 * runningTasks.get(0).topActivity; String topActivityString =
	 * runningTasks.get(0).topActivity .getClassName().substring(
	 * runningTasks.get(0).topActivity.getClassName() .lastIndexOf(".") + 1);
	 * Log.i("topActivityString : ", topActivityString);
	 * 
	 * // For the Drawer Layout if (topActivityString.equals("ActivityMain")) {
	 * 
	 * if (componentInfo.getPackageName().toString()
	 * .equalsIgnoreCase(context.getPackageName().toString())) { return true; }
	 * else { return false; }
	 * 
	 * } }
	 */
}
