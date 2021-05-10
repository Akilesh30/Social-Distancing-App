package org.altbeacon.beaconreference;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private Spinner spinner;
    private EditText editText;
    EditText CompanyName ;
    EditText Phone ;
    EditText Email ;
    String NameString;
    String PhoneNumber;
    String EmailId;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editText = findViewById(R.id.editTextPhone);
        if (Build.VERSION.SDK_INT >= 21) {
            //getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorAccent1)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.intro_title_color)); //status bar or the time bar at the top
        }
        sharedPreferences=getSharedPreferences("Phone",MODE_PRIVATE);
        editor=sharedPreferences.edit();
button=findViewById(R.id.buttonContinue);
        button.setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf"));
        button.setText("Login");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = editText.getText().toString().trim();
                if(TextUtils.isEmpty(number)){
                    Toast.makeText(RegisterActivity.this,"Please enter Phone No !",Toast.LENGTH_LONG).show();
                    return;
                }
                if (number.isEmpty() || number.length() < 10) {
                    editText.setError("Valid number is required");
                    editText.requestFocus();
                    return;
                }
                else {

                    String phonenumber = "+" + 91 + number;
                    editor.putString("phoneshared", phonenumber);
                    editor.commit();
                    Intent intent = new Intent(RegisterActivity.this, OTPActivity.class);
                    intent.putExtra("phonenumber", phonenumber);
                    Log.i("phoneeeee", phonenumber);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
        }
    }
}