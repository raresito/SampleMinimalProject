package com.example.cristianbaita.sampleminimalproject.utils;

import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Point;
import android.location.Criteria;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.UUID;

public class SystemUtils {
	private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
	private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    /**
     * Checks if notifs are blocked by user in the app settings. It's not a very reliable and recommended solution, but it is the only one available
     * If android verstion is less than 19 it's not working and I'm returning true by default
     *
     * @param context
     * @return
     */
	public static boolean isNotificationEnabled(Context context) {
        if (android.os.Build.VERSION.SDK_INT < 19){
            return true;
        }
		AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
		ApplicationInfo appInfo = context.getApplicationInfo();
		String pkg = context.getApplicationContext().getPackageName();
		int uid = appInfo.uid;
		Class appOpsClass = null; /* Context.APP_OPS_MANAGER */
		try {
			appOpsClass = Class.forName(AppOpsManager.class.getName());
			Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);
			Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
			int value = (int)opPostNotificationValue.get(Integer.class);
			return ((int)checkOpNoThrowMethod.invoke(mAppOps,value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return false;
	}


	/**
	 * Checks if is debuggable.
	 *
	 * @param context
	 *            the context
	 * @return true, if is debuggable
	 */
	public static boolean isDebuggable(Context context)
	{
		return (0 != (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));//
	}

    /**
     * Gets manifest permission status
     *
     * @param context
     * @param permission
     * @return
     */
	public static boolean hasPermission(Context context, String permission)
	{
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
			if (info.requestedPermissions != null) {
				for (String p : info.requestedPermissions) {
					if (p.equals(permission)) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Gets the unique id.
	 *
	 * @param context
	 *            the context
	 * @return the unique id
	 */
	static public String getUniqueId(Context context)
	{
		final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = ""
				+ android.provider.Settings.Secure.getString(context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);

		UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		return deviceUuid.toString();
	}

	public static boolean isIntentAvailable(Context context, String action)
	{
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	/**
	 * Checks if is network available.
	 *
	 * @param context
	 *            the context
	 * @return true, if is network available
	 */
	public static boolean isNetworkAvailable(Context context)
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}

	/**
	 * Constructs an intent for picking a photo from Gallery.
	 *
	 * @return the photo pick intent
	 */
	public static Intent getPhotoPickIntent()
	{
		Intent intent;

		//if (Build.VERSION.SDK_INT <19){
			intent = new Intent(Intent.ACTION_GET_CONTENT, null);
			intent.setType("image/*");
		/*} else {
		    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
		    intent.addCategory(Intent.CATEGORY_OPENABLE);
		    intent.setType("image/*");
		}*/

		return intent;

	}

	/**
	 * Constructs an intent for capturing a photo and storing it in a temporary file.
	 *
	 * @param f
	 *            the file
	 * @return the take pick intent
	 */
	public static Intent getTakePickIntent(File f)
	{
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		return intent;
	}

	/**
	 * Checks if is device in the landscape mode.
	 *
	 * @param activity
	 *            the activity
	 * @return true, if is device in the landscape mode
	 */
	public static boolean isDeviceInTheLandscapeMode(Activity activity)
	{
		int width = activity.getWindowManager().getDefaultDisplay().getWidth();
		int height = activity.getWindowManager().getDefaultDisplay().getHeight();

		boolean isLandscape = width > height;
		return isLandscape;
	}

	/**
	 * Gets the screen size.
     * NOTE: For the time being, this method is deprecated because it may not return correct values, depending on OS version.
     * Calls to it should be replaced with calls to getScreenSizeEx().
     * This change is out of scope right now, as it impacts code areas that will not be checked.
	 *
	 * @param context
	 *            the context
	 * @return the screen size
	 */
    @Deprecated
	public static Point getScreenSize(Context context)
	{
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		size.set(display.getWidth(), display.getHeight());
		return size;
	}

    /**
     * Gets the screen size.
     *
     * @param context
     *            the context
     * @return the screen size
     */
    public static Point getScreenSizeEx(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        size.set(0, 0);
        try
        {
            if (Build.VERSION.SDK_INT < 13)
            {
                size.set(display.getWidth(), display.getHeight());
            } else
            {
                display.getSize(size);
            }
        }
        catch (Exception e)
        {
        }

        return size;
    }

	public static String getVersion(Context context)
	{
		String versionName = "1.0.0";
		Resources resources = context.getResources();
		AssetManager assetManager = resources.getAssets();
		try
		{
			InputStream inputStream = assetManager.open("version.properties");
			Properties properties = new Properties();
			properties.load(inputStream);
			versionName = properties.getProperty("version");
		} catch (IOException e)
		{
			System.err.println("Failed to open property file");
			e.printStackTrace();
		}
		return versionName;
	}

	public static boolean isConnectedWifi(Context context){
		ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		return (conMan.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI);


	}

	public static boolean isOnWifi(Context context)
	{
		ConnectivityManager tempCm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetwork = tempCm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetwork != null && wifiNetwork.isConnected())
		{
			return true;
		}
		return false;
	}

	public static boolean isOnMobileData(Context context)
	{
		ConnectivityManager tempCm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobileNetwork = tempCm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileNetwork != null && mobileNetwork.isConnected())
		{
			return true;
		}
		return false;
	}

    public static boolean intentHasValidTarget(Context context, Intent intent)
    {
        PackageManager pm = context.getPackageManager();
        if (pm.resolveActivity(intent, 0) != null)
        {
            return true;
        }

        return false;
    }

	public static Intent getOpenUrlIntent(String url)
	{
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));

		return intent;
	}

	public static boolean canOpenUrl(Context context, String url)
	{
		return intentHasValidTarget(context, getOpenUrlIntent(url));
	}

	public static void openUrlOrShowToast(Context context, String url, String errorMessage)
	{
		Intent intent = getOpenUrlIntent(url);

		if (intentHasValidTarget(context, getOpenUrlIntent(url)))
		{
			context.startActivity(intent);
		}
		else
		{
			Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
		}
	}

    private static Intent getSmsIntent(String smsText)
    {
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
        smsIntent.setData(Uri.parse("sms:"));
        smsIntent.putExtra(Intent.EXTRA_TEXT, smsText);
        smsIntent.putExtra("sms_body", smsText);

        return smsIntent;
    }

    private static Intent getSmsToIntent(String phoneNumber, String smsText)
    {
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
        smsIntent.setData(Uri.parse("smsto:" + phoneNumber));
        smsIntent.putExtra(Intent.EXTRA_TEXT, smsText);
        smsIntent.putExtra("sms_body", smsText);

        return smsIntent;
    }

    public static boolean canSendSms(Context context, Intent intent)
    {
        return intentHasValidTarget(context, intent);
    }


    public static void sendSmsTo(Activity activity,String phoneNumber, String smsText, int requestCode)
    {
        Intent intent = getSmsToIntent(phoneNumber,smsText);
        if (canSendSms(activity, intent))
        {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    public static void sendSms(Activity activity, String smsText, int requestCode)
    {
        Intent intent = getSmsIntent(smsText);
        if (canSendSms(activity, intent))
        {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    private static Intent getEmailIntent(Context context, String emailAddress, String subject, String text)
    {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:?bcc=" + emailAddress));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, text);

        return emailIntent;

    }

    public static boolean canSendEmail(Context context, String emailAddress, String subject, String text)
    {
        return intentHasValidTarget(context, getEmailIntent(context, emailAddress, subject, text));
    }

    public static void sendEmail(Activity activity, String emailAddress, String subject, String text, int requestCode)
    {
        Intent intent = getEmailIntent(activity, emailAddress, subject, text);
        if (canSendEmail(activity, emailAddress, subject, text))
        {
            // intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            activity.startActivityForResult(intent, requestCode);
        }
    }

    @SuppressWarnings("unused")
    public static void shareUsingThirdPartyAppStandard(Context context, String chooserTitle, String shareSubject, String shareBody)
    {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
        context.startActivity(Intent.createChooser(sharingIntent, chooserTitle));
    }

    private static ArrayList<String> getExcludedPackagesList(Context context)
    {
        // Build a list of excluded PACKAGES
        ArrayList<String> excludedPackages = new ArrayList<String>();
        excludedPackages.add(context.getApplicationContext().getPackageName().toLowerCase());
        excludedPackages.add("bluetooth");

        // Get share intents for email clients in order to exclude them
        Intent emailIntentTemplate = new Intent(Intent.ACTION_SENDTO);
        emailIntentTemplate.setData(Uri.parse("mailto:"));
        List<ResolveInfo> emailClients = context.getPackageManager().queryIntentActivities(emailIntentTemplate, 0);
        for (ResolveInfo emailClient : emailClients)
        {
            excludedPackages.add(emailClient.activityInfo.packageName.toLowerCase());
        }

        // Get share intents for SMS clients in order to exclude them
        // Viber is excluded, so it is not needed
        /*Intent smsIntentTemplate = new Intent(Intent.ACTION_SENDTO);
        smsIntentTemplate.setData(Uri.parse("smsto:"));
        List<ResolveInfo> smsClients = context.getPackageManager().queryIntentActivities(smsIntentTemplate, 0);
        for (ResolveInfo smsClient : smsClients)
        {
            excludedPackages.add(smsClient.activityInfo.packageName.toLowerCase());
        }*/

        return excludedPackages;
    }

    private static ArrayList<String> getExcludedActivitiesList(Context context)
    {
        // Build a list of excluded ACTIVITY LABELS
        ArrayList<String> excludedActivities = new ArrayList<String>();
        excludedActivities.add("android system");
        excludedActivities.add("dropbox");
        excludedActivities.add("drive");
        excludedActivities.add("clipboard");
        excludedActivities.add("tv sideview");
        excludedActivities.add("direct message");

        return excludedActivities;
    }

	public static boolean isGooglePlayServicesAvailable(Context context) {
		try {
			return Build.VERSION.SDK_INT >= 8 && context.getPackageManager().getPackageInfo("com.google.android.gsf", 0) != null;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String getAppVersionName(Context context)
	{
		if (context == null)
		{
			return "1.0.0";
		}

		try
		{
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		}
		catch (PackageManager.NameNotFoundException e)
		{
			e.printStackTrace();

			Resources resources = context.getResources();
			AssetManager assetManager = resources.getAssets();

			try
			{
				InputStream inputStream = assetManager.open("version.properties");
				Properties properties = new Properties();
				properties.load(inputStream);
				return properties.getProperty("version");
			}
			catch (IOException ex)
			{
				e.printStackTrace();
			}
		}

		return "N/A";

	}

    public static String getDefaultSmsAppName(Context context){

        PackageManager pm = context.getApplicationContext().getPackageManager();

        //this is used just for analytics!
        try{

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                String defApp = Settings.Secure.getString(context.getContentResolver(), "sms_default_application");
                Intent iIntent = pm.getLaunchIntentForPackage(defApp);
                ResolveInfo mInfo = pm.resolveActivity(iIntent,0);

                return mInfo.loadLabel(pm).toString();
            }else{

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setType("vnd.android-dir/mms-sms");
                ResolveInfo mInfo = pm.resolveActivity(intent,0);
                return mInfo.loadLabel(pm).toString();

            }

        }catch (Exception ex){
            ex.printStackTrace();
        }

        return null;

    }

	public static String getLastMediaStoreImagePath(Context context)
	{
		String[] projection = new String[] {
				MediaStore.Images.ImageColumns.DATA,
				MediaStore.Images.ImageColumns.DATE_TAKEN};
		String orderByImageFiles = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC";

		Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				projection, null, null, orderByImageFiles);

		String imagePath = null;
		if (cursor != null)
		{
			if (cursor.moveToFirst())
			{
				imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
			}

			cursor.close();
		}

		return imagePath;
	}
}
