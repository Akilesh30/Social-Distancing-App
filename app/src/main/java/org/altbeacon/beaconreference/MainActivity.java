package org.altbeacon.beaconreference;
import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.altbeacon.beacon.BeaconManager;

public class MainActivity extends Activity  {
	protected static final String TAG = "MonitoringActivity";
	private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;
	private static final int PERMISSION_REQUEST_BACKGROUND_LOCATION = 2;
	Button button;
	Button button1;
	Button buttoncopy;
	private LocationSettingsRequest.Builder builder;
	private final int REQUEST_CHECK_CODE=8989;
	public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
	LocationManager locationm;
	int requestCodeForEnable;
	//public static int oncount=0;
	BluetoothAdapter bluetoothAdapter;
	Intent btEnablingIntent;
	RangingActivity rangingActivity=new RangingActivity();
	BeaconReferenceApplication beaconReferenceApplication=new BeaconReferenceApplication();
	Dialog dialog;
public static boolean buttondisplay=false;




	@SuppressLint("ResourceType")
	@RequiresApi(api = Build.VERSION_CODES.M)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_monitoring);
		checkLocationPermission();
		btEnablingIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		startActivity(btEnablingIntent);
		requestCodeForEnable = 1;
		dialog=new Dialog(this);

		bluetoothOnMethod();

// clear FLAG_TRANSLUCENT_STATUS flag:


// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window

// finally change the color
		if (Build.VERSION.SDK_INT >= 21) {
			//getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorAccent1)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
			getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorAccent1)); //status bar or the time bar at the top
		}

		button = (Button) findViewById(R.id.button4);

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				openservices();
			}
		});
		buttoncopy = (Button) findViewById(R.id.Buttoncopy);

		button = (Button) findViewById(R.id.button6);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				open(view);
			}
		});
		button = (Button) findViewById(R.id.button3);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				pdfs(view);
			}
		});
		button = (Button) findViewById(R.id.button5);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				myths(view);
			}
		});

		button = (Button) findViewById(R.id.Button1);
		if(!buttondisplay){
			button.setVisibility(View.VISIBLE);
			buttoncopy.setVisibility(View.GONE);
		}
		else{
			button.setVisibility(View.GONE);
			buttoncopy.setVisibility(View.VISIBLE);
		}
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				rangingActivity.on(1);
				buttondisplay=true;
				//button.setEnabled(false);
				//button.setText("Social Distancing is running");
				button.setVisibility(View.GONE);
				buttoncopy.setVisibility(View.VISIBLE);
				Intent intent = new Intent(MainActivity.this,RangingActivity.class);
				startActivity(intent);
			}
		});
		buttoncopy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				//buttoncopy.setText("Social Distancing is running");
				Log.i("buttoncopy", "clickedddd");
				stopService(new Intent(MainActivity.this,BeaconReferenceApplication.class));

				//beaconReferenceApplication.disableMonitoring();
				//onEnableClicked();

				Showpopup();

			}
		});
	    button1 = (Button) findViewById(R.id.button2);
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				analytics(view);
			}
		});

	}

	public void Showpopup(){
		Button btnFollow;
		ImageView btnFollow1;
		dialog.setContentView(R.layout.exitpopup);
		btnFollow = (Button) dialog.findViewById(R.id.confirmbutton);
		btnFollow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				buttondisplay=false;
				buttoncopy.setVisibility(View.GONE);
				button.setVisibility(View.VISIBLE);
				rangingActivity.on(0);
				onEnableClicked();
				android.os.Process.killProcess(android.os.Process.myPid());
				System.exit(10);
			}
		});

		btnFollow1 = (ImageView) dialog.findViewById(R.id.closepopup);
		btnFollow1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.show();



	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == requestCodeForEnable) {
			if (resultCode == RESULT_OK) {
				Toast.makeText(getApplicationContext(), "Bluetooth is enabled", Toast.LENGTH_LONG).show();
			}
			else if(resultCode==RESULT_CANCELED) {
				Toast.makeText(getApplicationContext(), "Bluetooth enabling cancelled", Toast.LENGTH_LONG).show();
			}
		}
	}

//	@Override
//	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//		switch (requestCode) {
//			case PERMISSION_REQUEST_FINE_LOCATION: {
//				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//					Log.d(TAG, "fine location permission granted");
//				} else {
//					final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//					builder.setTitle("Functionality limited");
//					builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons.");
//					builder.setPositiveButton(android.R.string.ok, null);
//					builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//
//						@Override
//						public void onDismiss(DialogInterface dialog) {
//						}
//
//					});
//					builder.show();
//				}
//				return;
//			}
//			case PERMISSION_REQUEST_BACKGROUND_LOCATION: {
//				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//					Log.d(TAG, "background location permission granted");
//				} else {
//					final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//					builder.setTitle("Functionality limited");
//					builder.setMessage("Since background location access has not been granted, this app will not be able to discover beacons when in the background.");
//					builder.setPositiveButton(android.R.string.ok, null);
//					builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//
//						@Override
//						public void onDismiss(DialogInterface dialog) {
//						}
//
//					});
//					builder.show();
//				}
//				return;
//			}
//		}
//	}

	public void onRangingClicked(View view) {
		Intent myIntent = new Intent(this, RangingActivity.class);
		this.startActivity(myIntent);
	}
	public void onEnableClicked() {
		BeaconReferenceApplication application = ((BeaconReferenceApplication) this.getApplicationContext());
		//if (BeaconManager.getInstanceForApplication(this).getMonitoredRegions().size() > 0) {
			application.disableMonitoring();
			Log.i("Disabledd","disabledd");
		//	((Button)findViewById(R.id.enableButton)).setText("Re-Enable Monitoring");
		//}
//		else {
//			((Button)findViewById(R.id.enableButton)).setText("Disable Monitoring");
//			application.enableMonitoring();
//		}
	}


	@Override
	public void onResume() {
		super.onResume();
		BeaconReferenceApplication application = ((BeaconReferenceApplication) this.getApplicationContext());
		application.setMainActivity(this);
		updateLog(application.getLog());
	}

	@Override
	public void onPause() {
		super.onPause();
		((BeaconReferenceApplication) this.getApplicationContext()).setMainActivity(null);
	}

	private void verifyBluetooth() {
		try {
			if (!BeaconManager.getInstanceForApplication(this).checkAvailability()) {
				final AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Bluetooth not enabled");
				builder.setMessage("Please enable bluetooth in settings and restart this application.");
				builder.setPositiveButton(android.R.string.ok, null);
				builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						//finish();
						//System.exit(0);
					}
				});
				builder.show();
			}
		}
		catch (RuntimeException e) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Bluetooth LE not available");
			builder.setMessage("Sorry, this device does not support Bluetooth LE.");
			builder.setPositiveButton(android.R.string.ok, null);
			builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					//finish();
					//System.exit(0);
				}

			});
			builder.show();

		}

	}

	public void updateLog(final String log) {
		runOnUiThread(new Runnable() {
			public void run() {
				//EditText editText = (EditText)MonitoringActivity.this
				//	.findViewById(R.id.monitoringText);
				//editText.setText(log);
			}
		});
	}  void bluetoothOnMethod() {
//        bluetoothAdapter.startDiscovery();

		if (bluetoothAdapter == null) {
			//
		} else {
			if (!bluetoothAdapter.isEnabled()) {
				startActivityForResult(btEnablingIntent, requestCodeForEnable);
			}
		}

	}
	@RequiresApi(api = Build.VERSION_CODES.M)
	@SuppressLint("BatteryLife")
	public boolean checkLocationPermission() {


		Intent intent = new Intent();
		String packageName = getPackageName();
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		if (pm.isIgnoringBatteryOptimizations(packageName))
			intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
		else {
			intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
			intent.setData(Uri.parse("package:" + packageName));
		}
		startActivity(intent);

		LocationRequest request = new LocationRequest()
				.setFastestInterval(1500)
				.setInterval(3000)
				.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		builder = new LocationSettingsRequest.Builder().addLocationRequest(request);
		Task<LocationSettingsResponse> result = LocationServices.getSettingsClient( this).checkLocationSettings(builder.build());
		result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
			@Override
			public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
				try{
					task.getResult(ApiException.class);
				}catch (ApiException e){
					switch (e.getStatusCode())
					{
						case LocationSettingsStatusCodes
								.RESOLUTION_REQUIRED:
							try {
								ResolvableApiException resolvableApiException= (ResolvableApiException) e;
								resolvableApiException.startResolutionForResult(MainActivity.this,REQUEST_CHECK_CODE);
							} catch (IntentSender.SendIntentException ex) {
								ex.printStackTrace();
							}catch (ClassCastException ex){
							}break;
						case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
						{ break;}
					}
				}
			}
		});
//		if (ContextCompat.checkSelfPermission(this,
//				Manifest.permission.ACCESS_FINE_LOCATION)
//				!= PackageManager.PERMISSION_GRANTED) {
//			if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//					Manifest.permission.ACCESS_FINE_LOCATION)) {
//				new androidx.appcompat.app.AlertDialog.Builder(this)
//						.setTitle(R.string.title_location_permission)
//						.setMessage(R.string.text_location_permission)
//						.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//							@Override
//							public void onClick(DialogInterface dialogInterface, int i) {
//								//Prompt the user once explanation has been shown
//								if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//									ActivityCompat.requestPermissions(MonitoringActivity.this,
//											new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
//											MY_PERMISSIONS_REQUEST_LOCATION);
//								}
//							}
//						})
//						.create()
//						.show();
//			} else {
//				ActivityCompat.requestPermissions(this,
//						new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//						MY_PERMISSIONS_REQUEST_LOCATION);
//				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//					ActivityCompat.requestPermissions(this,
//							new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
//							MY_PERMISSIONS_REQUEST_LOCATION);
//				}
//			}
//			return false;
//		} else {
//			return true;
//		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
					== PackageManager.PERMISSION_GRANTED) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
					if (this.checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
							!= PackageManager.PERMISSION_GRANTED) {
						if (!this.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
							final AlertDialog.Builder builder = new AlertDialog.Builder(this);
							builder.setTitle("This app needs background location access");
							builder.setMessage("Please grant location access so this app can detect beacons in the background.");
							builder.setPositiveButton(android.R.string.ok, null);
							builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

								@TargetApi(23)
								@Override
								public void onDismiss(DialogInterface dialog) {
									requestPermissions(new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
											PERMISSION_REQUEST_BACKGROUND_LOCATION);
								}

							});
							builder.show();
						}
						else {
							final AlertDialog.Builder builder = new AlertDialog.Builder(this);
							builder.setTitle("Functionality limited");
							builder.setMessage("Since background location access has not been granted, this app will not be able to discover beacons in the background.  Please go to Settings -> Applications -> Permissions and grant background location access to this app.");
							builder.setPositiveButton(android.R.string.ok, null);
							builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

								@Override
								public void onDismiss(DialogInterface dialog) {
								}

							});
							builder.show();
						}
					}
				}
				return true;
			} else {
				if (!this.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
					requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
									Manifest.permission.ACCESS_BACKGROUND_LOCATION},
							PERMISSION_REQUEST_FINE_LOCATION);
					return true;
				}
				else {
					final AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("Functionality limited");
					builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons.  Please go to Settings -> Applications -> Permissions and grant location access to this app.");
					builder.setPositiveButton(android.R.string.ok, null);
					builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

						@Override
						public void onDismiss(DialogInterface dialog) {
						}

					});
					builder.show();
					return false;
				}

			}
		}

		return false;
	}

	public void openservices(){
		Intent intnt=new Intent(this ,Services.class);
		startActivity(intnt);
	}
	public void open(View view)
	{
		Intent browserIntent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.hogist.com/#/"));
		startActivity(browserIntent);
	}
	public void pdfs(View view)
	{
		Intent browserIntent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.mohfw.gov.in/pdf/Illustrativeguidelineupdate.pdf"));
		startActivity(browserIntent);
	}
	public void myths(View view)
	{
		Intent browserIntent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://fssai.gov.in/cms/coronavirus.php"));
		startActivity(browserIntent);
	}
	public void analytics(View view){
		final Intent intent1 = new Intent(MainActivity.this, Analytics.class);
		startActivity(intent1);
	}
}
