package org.altbeacon.beaconreference;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity {
      private String verificationid;
        private FirebaseAuth mAuth;

        private ProgressBar progressBar;
        private EditText editText;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_o_t_p);
            if (Build.VERSION.SDK_INT >= 21) {
                //getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorAccent1)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
                getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.intro_title_color)); //status bar or the time bar at the top
            }

            mAuth = FirebaseAuth.getInstance();

                    progressBar = findViewById(R.id.progressbar);
                    editText = findViewById(R.id.editTextCode);

                    String phonenumber = getIntent().getStringExtra("phonenumber");
                    sendVerificationCode(phonenumber);
            Log.i("phoneeeee",phonenumber+"  "+ "sentttt");
                    findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String code = editText.getText().toString().trim();

                            if ((code.isEmpty() || code.length() < 6)){

                                editText.setError("Enter code...");
                                editText.requestFocus();
                                return;
                            }
                    verifyCode(code);

                }
            });
        }

        private void verifyCode(String code){
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationid, code);
            signInWithCredential(credential);
        }

        private void signInWithCredential(PhoneAuthCredential credential) {
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Intent intent = new Intent(OTPActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                startActivity(intent);

                            } else {
                                Toast.makeText(OTPActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                    });
        }

        private void sendVerificationCode(String number){

//            PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                    number,
//                    60,
//                    TimeUnit.SECONDS,
//                     TaskExecutors.MAIN_THREAD,
//                    mCallBack
//            );
            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber(number)       // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(this)                 // Activity (for callback binding)
                            .setCallbacks(mCallBack)          // OnVerificationStateChangedCallbacks
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
        }


        private PhoneAuthProvider.OnVerificationStateChangedCallbacks
                mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationid = s;
            }

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                String code = phoneAuthCredential.getSmsCode();
                if (code != null){
                    progressBar.setVisibility(View.VISIBLE);
                    verifyCode(code);
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(OTPActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();

            }
        };
    }
