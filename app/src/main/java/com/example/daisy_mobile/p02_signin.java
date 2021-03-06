package com.example.daisy_mobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.AsyncListUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class p02_signin extends AppCompatActivity {
    public static String user_id;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p02_signin);
        pref = getApplicationContext().getSharedPreferences("myPref", 0);
        editor = pref.edit();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        EditText emailet = (EditText) findViewById(R.id.p02_et_email);
        EditText passwordet = (EditText) findViewById(R.id.p02_et_password);


        TextView next = (TextView) findViewById(R.id.et_p02_register);
        next.setOnClickListener(view -> {
            next.setTextColor(Color.parseColor("#FFFFFF"));
            startActivity(new Intent(p02_signin.this, p03_signup.class));

        });

        Button signin = (Button) findViewById(R.id.p02_bt_signin);
        signin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String email = emailet.getText().toString();
                String password = passwordet.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //   auth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString());
                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(p02_signin.this, task -> {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.

                            if (!task.isSuccessful()) {
                                // there was an error
                                if (password.length() < 6) {
                                    passwordet.setError(getString(R.string.minimum_password));
                                } else {
                                    Toast.makeText(p02_signin.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                user_id = auth.getCurrentUser().getUid();
                                editor.putString("USER_ID", user_id);
                                editor.commit();
                                //kiem tra xem id tren cua users hay kitchens
                                FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                                DocumentReference docIdRef = rootRef.collection("users").document(user_id);
                                docIdRef.get().addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        DocumentSnapshot document = task1.getResult();
                                        if (document.exists()) {
                                            Intent intent = new Intent(p02_signin.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Intent intent = new Intent(p02_signin.this, MainActivity2.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                });

                            }
                        });
            }

        });

    }
}