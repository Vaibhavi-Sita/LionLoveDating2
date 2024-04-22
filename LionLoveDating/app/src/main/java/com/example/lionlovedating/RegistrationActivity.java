package com.example.lionlovedating;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    private Button mRegister;

    private EditText mEmail, mPassword, mName;
    private RadioGroup mRadioGroup;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthSateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthSateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null) {
                    Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };
        mRegister = (Button) findViewById(R.id.register);

        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mName = (EditText) findViewById(R.id.name);

        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectId = mRadioGroup.getCheckedRadioButtonId();
                final RadioButton radioButton = (RadioButton) findViewById(selectId);
                if(radioButton.getText() == null){
                    return;
                }

                final String email= mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                final String name = mName.getText().toString();


                if(!isValidPSUEmail(email)){
                        Toast.makeText(RegistrationActivity.this, "invalid email! Sign in with a valid psu email", Toast.LENGTH_SHORT).show();
                }
                else {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {


                            if (!task.isSuccessful()) {
                                Toast.makeText(RegistrationActivity.this, "sign_up_error", Toast.LENGTH_SHORT).show();
                            }

                            else {
                                String userId = mAuth.getCurrentUser().getUid();
                                DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                                Map userInfo = new HashMap<>();
                                userInfo.put("name", name);
                                userInfo.put("sex", radioButton.getText().toString());
                                userInfo.put("profileImageUrl", "default");
                                currentUserDb.updateChildren(userInfo);
                            }
                        }
                    });
                }

            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthSateListener);
    }

    @Override
    protected void onStop() {

        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthSateListener);
    }

    public static boolean isValidPSUEmail(String email) {
        // Regex pattern to match "abc1234@psu.edu"
        String regex = "^[a-z]{3}\\d{4}@psu\\.edu$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        // Returning true if email matches the pattern, otherwise false
        return matcher.matches();
    }
}