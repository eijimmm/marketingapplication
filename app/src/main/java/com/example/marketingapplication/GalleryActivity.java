package com.example.marketingapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class GalleryActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageButton IB_Undo;
    private ImageView IV_Gallery;
    private TextView TV_Date, TV_Caption;

    private String shopUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        IB_Undo = findViewById(R.id.IB_Undo);
        IV_Gallery = findViewById(R.id.IV_Gallery);
        TV_Date = findViewById(R.id.TV_Date);
        TV_Caption = findViewById(R.id.TV_Caption);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //get uid of the shop from intent
        shopUid = getIntent().getStringExtra("shopUid");

        IB_Undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //go previous activity
                onBackPressed();
            }
        });
        
        loadGalleryDetails();
    }

    private void loadGalleryDetails() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(shopUid).child("Gallery").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    String date = "" + ds.child("date").getValue();
                    String caption = "" + ds.child("pictureCaption").getValue();
                    String picture = ""+ds.child("pictureImage").getValue();

                    TV_Date.setText(date);
                    TV_Caption.setText(caption);

                    try{
                        Picasso.get().load(picture).placeholder(R.drawable.ic_baseline_store_24).into(IV_Gallery);
                    }
                    catch (Exception e){
                        IV_Gallery.setImageResource(R.drawable.ic_baseline_store_24);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}