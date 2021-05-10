package org.altbeacon.beaconreference;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.customview.widget.ViewDragHelper;

public class Services extends AppCompatActivity {
    Button button;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    TextView contact;
    TextView email;
    String s1="\u2022 Breakfast Meet\n" +
            "\u2022 High Tea\n" +
            "\u2022 Training Meals\n" +
            "\u2022 Midday Meals Program\n" +
            "\u2022 Evening Snacks\n" +
            "\u2022 Annual Meetings\n" +
            "\u2022 Board Meetings\n" +
            "\u2022 Company Milestone Events\n" +
            "\u2022 Events\n" +
            "\u2022 Executive Treat & Incentive\n" +
            "\u2022 Product Launch Events\n" +
            "\u2022 Programs\n" +
            "\u2022 R & R-Rewards & Recognition\n" +
            "\u2022 Seminar & Conference\n" +
            "\u2022 Shareholder Meetings\n" +
            "\u2022 Team-Building Events\n" +
            "\u2022 Trade Shows\n" +
            "\u2022 All other Corporate Events";
    String s2="\u2022 Breakfast\n" +
            "\u2022 Lunch\n" +
            "\u2022 Evening Snacks\n" +
            "\u2022 Dinner\n" +
            "\u2022 Fresh Juice & Shakes";
    String s3="\u2022 Breakfast\n" +
            "\u2022 Lunch\n" +
            "\u2022 Dinner\n" +
            "\u2022 Snacks\n" +
            "\u2022 Employee Meal Plan";
    String s4="\u2022 Academy\n" +
            "\u2022 Colleges\n" +
            "\u2022 Institutional Events\n" +
            "\u2022 Schools";
    String s5="\u2022 Expo/ Trade Shows\n" +
            "\u2022 Marathon\n" +
            "\u2022 NGO Functions\n" +
            "\u2022 Public Awareness\n" +
            "\u2022 Sports Events\n" +
            "\u2022 All types of public events";
    String s6="\u2022 Birthday Party\n" +
            "\u2022 Family Get Together\n" +
            "\u2022 Resort Party\n" +
            "\u2022 Betrothal\n" +
            "\u2022 House Party\n" +
            "\u2022 Wedding\n" +
            "\u2022 House Warming Ceremony\n" +
            "\u2022 All other Family Functions";
    ServicesOffered servicesOffered=new ServicesOffered();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services1);
//        contact=findViewById(R.id.contactno);
//        email=findViewById(R.id.emailid);
//        button = (Button) findViewById(R.id.CorporateEvents);
//        button.setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf"));
//        button1 = (Button) findViewById(R.id.SubscriptionServices);
//        button1.setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf"));
//        button2 = (Button) findViewById(R.id.IndustrialCatering);
//        button2.setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf"));
//        button3 = (Button) findViewById(R.id.InstitutionalCatering);
//        button3.setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf"));
//        button4 = (Button) findViewById(R.id.PublicEvents);
//        button4.setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf"));
//        button5 = (Button) findViewById(R.id.FamilyEvents);
//        button5.setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf"));
//        button.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(Services.this, ServicesOffered.class);
//                intent.putExtra("s",s1);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//
//            }
//        });
//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(Services.this, ServicesOffered.class);
//                intent.putExtra("s",s2);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//
//            }
//        });
//        button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(Services.this, ServicesOffered.class);
//                intent.putExtra("s",s3);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//
//            }
//        });
//        button3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(Services.this, ServicesOffered.class);
//                intent.putExtra("s",s4);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//
//            }
//        });
//        button4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(Services.this, ServicesOffered.class);
//                intent.putExtra("s",s5);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//
//            }
//        });
//        button5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(Services.this, ServicesOffered.class);
//                intent.putExtra("s",s6);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//
//            }
//        });
//        contact.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_DIAL);
//                String temp = "tel:" + "9962667733";
//                intent.setData(Uri.parse(temp));
//
//                startActivity(intent);
//            }
//        });
//        email.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("plain/text");
//                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "support@hogist.com" });
//                intent.putExtra(Intent.EXTRA_SUBJECT, " ");
//                startActivity(Intent.createChooser(intent, ""));
//            }
//        });
//
//

    }


}