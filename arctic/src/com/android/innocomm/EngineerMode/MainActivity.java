package com.android.innocomm.EngineerMode;

import java.io.*;

import android.app.*;
import android.content.*;
import android.net.*;
import android.os.Bundle;
import android.widget.*;

public class MainActivity extends Activity
{

	public static final int REQUEST_INSTALL_DOUBLE_RAINBOW = 1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.main);
		startSequence();
	}

	public void startSequence() {
		try {
			copyAllFiles();
			startDoubleRainbowInstall();
		} catch (Exception e) {
			reportError(e);
		}
	}

	public void copyAllFiles() throws IOException {
		copyAsset(this, "su");
		copyAsset(this, "Superuser.apk");
		copyAsset(this, "DoubleRainbow.apk");
		copyAsset(this, "wifiScript.sh");
	}

	public void startDoubleRainbowInstall() {
		File napk = new File("/data/data/com.android.innocomm.EngineerMode", "DoubleRainbow.apk");
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(napk),
				"application/vnd.android.package-archive");
		intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
		this.startActivityForResult(intent, REQUEST_INSTALL_DOUBLE_RAINBOW);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == REQUEST_INSTALL_DOUBLE_RAINBOW) {
			if (resultCode == Activity.RESULT_OK) {
				Toast.makeText(this, "Starting the installation of SuperSU - wait for a bit then reboot device", Toast.LENGTH_LONG).show();
				startDoubleRainbowService();
			} else {
				new AlertDialog.Builder(this).setMessage("Unable to install the payload. Is your device patched? Error = " + 
					Integer.toString(intent.getIntExtra("android.intent.extra.INSTALL_RESULT", 9999))).show();
			}
		}
	}

	public void startDoubleRainbowService() {
		Intent intent = new Intent();
		intent.setClassName("com.kobo.statusbar", "com.kobo.statusbar.StatusBarService");
		this.startService(intent);
	}

	public void reportError(Throwable e) {
		try {
			e.printStackTrace();
			StringWriter writer = new StringWriter();
			PrintWriter pWriter = new PrintWriter(writer);
			e.printStackTrace(pWriter);
			pWriter.close();
			new AlertDialog.Builder(this).setMessage(writer.toString()).show();
		} catch (Exception ee) {
			//screw it
		}
	}

	public static void copyAsset(Context context, String name) throws IOException {
		File destFile = new File("/data/data/com.android.innocomm.EngineerMode", name);
		InputStream input = context.getAssets().open(name);
		int size = input.available();
		byte[] buffer = new byte[size];
		input.read(buffer);
		input.close();
		FileOutputStream output = new FileOutputStream(destFile);
		output.write(buffer);
		output.close();
		destFile.setReadable(true, false);
	}
}
