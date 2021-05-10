package org.altbeacon.beaconreference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import android.app.Activity;

import android.app.Dialog;
import android.app.Notification;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.altbeacon.beacon.AltBeacon;
import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

public class RangingActivity extends Activity implements BeaconConsumer {
    protected static final String TAG = "RangingActivity";
    Dialog myDialog;
    private NotificationManagerCompat notificationManager;
    public static int distance_count=0;
    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
    public static int oncount=0;
    public int distCount=0;
    public int daycount=0;
    private TextView dateTimeDisplay;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat dateFormat1;
    public String date;
    public String date1;
    static int todaysday=1;
    static int todays=1;
    static int deleteday=1;
    static String previous_date="";
    FirebaseDatabase database;
    DatabaseReference reference;
    Map<String,Integer> map = new HashMap<>();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static String phoneshared="";
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranging);
        myDialog = new Dialog(this);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("countTable");
        sharedPreferences=getSharedPreferences("Phone",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        phoneshared=sharedPreferences.getString("phoneshared",null);
        Beacon beacon = new Beacon.Builder()
                .setId1("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6")
                .setId2("1")
                .setId3("2")
                .setManufacturer(0x0118) // Radius Networks.  Change this for other beacon layouts
                .setTxPower(-59)
                .setDataFields(Arrays.asList(new Long[]{0l})) // Remove this for beacon layouts without d: fields
                .build();

        // Change the layout below for other beacon types
        BeaconParser beaconParser = new BeaconParser()
                .setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25");
        BeaconTransmitter beaconTransmitter = new BeaconTransmitter(getApplicationContext(), beaconParser);
        beaconTransmitter.startAdvertising(beacon, new AdvertiseCallback() {
            @Override
            public void onStartFailure(int errorCode) {
                Log.e("TAG", "Advertisement start failed with code: " + errorCode);
            }
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                Log.i("TAG", "Advertisement start succeeded.");
            }
        });

ShowPopup();
    }
    public void ShowPopup() {
        TextView txtclose;
        Button btnFollow;
        myDialog.setContentView(R.layout.custompopup);

        btnFollow = (Button) myDialog.findViewById(R.id.btnfollow);
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
                finish();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
        myDialog.setCancelable(false);
        myDialog.setCanceledOnTouchOutside(false);

    }


    public void on(int oncount1){
        oncount=oncount1;
        Log.i("Oncount","Oncount is =========================1111111");
    }
    @Override 
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override 
    protected void onPause() {
        super.onPause();
        beaconManager.unbind(this);
    }

    @Override 
    protected void onResume() {
        super.onResume();
        beaconManager.bind(this);
    }

    @Override
    public void onBeaconServiceConnect() {
        final Vibrator v1 = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        RangeNotifier rangeNotifier = new RangeNotifier() {
           @RequiresApi(api = Build.VERSION_CODES.O)
           @Override
           public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {

              if (beacons.size() > 0) {
                  Log.d(TAG, "didRangeBeaconsInRegion called with beacon count:  "+beacons.size());
                  Beacon firstBeacon = beacons.iterator().next();
                if(firstBeacon.getDistance()<1 && oncount==1){
                   // sendOnChannel();
                    v1.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
                    logToDisplay("The first beacon " + firstBeacon.toString() + " is about " + firstBeacon.getDistance() + " meters away.");
                   distance_count++;
                    calendar = Calendar.getInstance();
                    dateFormat = new SimpleDateFormat("dd" );
                    dateFormat1 = new SimpleDateFormat("dd-MM-yyyy" );
                    date = dateFormat.format(new Date());
                    date1 = dateFormat1.format(new Date());
                    previous_date=sharedPreferences.getString("yesterday_date","");
                    if(!date1.matches(previous_date)){
                        reference.child(phoneshared).child(date).child("yValue").setValue(1);
                        editor.putString("yesterday_date",date1);
                        editor.commit();
                    }
                    else {
                        reference.child(phoneshared).child(date).child("yValue").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                int cnt= Integer.valueOf(snapshot.getValue().toString());
                                cnt++;
                                reference.child(phoneshared).child(String.valueOf(date)).child("yValue").setValue(cnt);
                                reference.child(phoneshared).child(String.valueOf(date)).child("yValue").removeEventListener(this);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                }
              }
           }
        };
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", Identifier.parse("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6"), Identifier.parse("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6"), null));
            beaconManager.addRangeNotifier(rangeNotifier);
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", Identifier.parse("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6"), null, null));
            beaconManager.addRangeNotifier(rangeNotifier);
        } catch (RemoteException e) {   }
    }
    public  void  saveData(int distance_cou){
        SharedPreferences sharedPreferences = getSharedPreferences("date", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(distance_cou==1) {
            distance_count= restoredata() + distance_cou;
        }
        else {
            distance_count=0+distance_cou;
        }
        editor.putString("Date",date);
        editor.putInt(date, distance_count);
        editor.apply();
    }
//    public void savedate(int tod){
//        SharedPreferences sharedPreferences = getSharedPreferences(date, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt("todays",todaysday);
//        editor.apply();
//    }
public int restoredata(){
    SharedPreferences sharedPreferences = getSharedPreferences("date", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPreferences.edit();
    distCount=sharedPreferences.getInt(date,0);

    Log.i("distCC", String.valueOf(distCount));
    return distCount;
}

    private void logToDisplay(final String line) {
       runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void run() {
//                EditText editText = (EditText)RangingActivity.this.findViewById(R.id.rangingText);
//                editText.append(line+"\n");
            }
        });
    }

//    public void sendOnChannel(){
//        String title = "Hogist Social Distancing";
//        String mess="Maintain Social Distancing "+"and "+"Wear Mask";
//        Notification notification=new NotificationCompat.Builder(this,BeaconReferenceApplication.CHANNEL_1_ID)
//                .setSmallIcon(R.drawable.ic_safety)
//                .setContentTitle(title)
//                .setContentText(mess)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setCategory(NotificationCompat.CATEGORY_REMINDER)
//                .build();
//        notificationManager.notify(1,notification);
//    }
}
