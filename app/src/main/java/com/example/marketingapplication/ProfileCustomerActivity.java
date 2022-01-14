package com.example.marketingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileCustomerActivity extends AppCompatActivity {

    private TextView TV_FullName, TV_Email;
    private Button BTN_Edit;
    private ImageButton IB_Undo;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_customer);

        TV_FullName = findViewById(R.id.TV_FullName);
        TV_Email = findViewById(R.id.TV_Email);
        BTN_Edit = findViewById(R.id.BTN_Edit);
        IB_Undo = findViewById(R.id.IB_Undo);

        firebaseAuth = FirebaseAuth.getInstance();
        loadMyInfo();

        BTN_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open edit profile activity
                startActivity(new Intent(ProfileCustomerActivity.this, ProfileEditUserActivity.class));
            }
        });

        IB_Undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void loadMyInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()){
                            String name = "" + ds.child("full name").getValue();
                            String email = "" + ds.child("email").getValue();

                            TV_FullName.setText(name);
                            TV_Email.setText(email);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}