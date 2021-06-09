package com.rtech.contactfirestone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.protobuf.Any;
import com.rtech.contactfirestone.databinding.ActivityMainBinding;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
private ActivityMainBinding mainBinding;
private FirebaseFirestore firebaseFirestore;
private DocumentReference documentReference;
private String KEY_NAME="name";
private String KEY_CONTACT="contact";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Create New Contact");
        mainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        firebaseFirestore = FirebaseFirestore.getInstance();
        documentReference = firebaseFirestore.collection("users").document();
        Initialize();
    }

    private void Initialize() {

        mainBinding.saveNo.setOnClickListener(this::onClick);
    }

    private void saveDate() {
        // This method for save data in Firestore
        String PersonName = mainBinding.edName.getText().toString().trim();
        String PersonContact = mainBinding.edContact.getText().toString().trim();
        if (PersonName.isEmpty()&PersonContact.isEmpty()) {
            Toast.makeText(this, "Enter Name and Contact", Toast.LENGTH_SHORT).show();
        }
        else {
            Map<String, Object> data = new HashMap<>();
            data.put(KEY_NAME, PersonName);
            data.put(KEY_CONTACT, PersonContact);

            firebaseFirestore.collection("users").document()
                    .set(data)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mainBinding.errors.setText("Data is Saved");
                            } else {
                                mainBinding.errors.setText(task.getException().getMessage());
                            }
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_no:
                saveDate();
                Intent intent = new Intent(getApplicationContext(),ContactListActivity.class);
                startActivity(intent);
                break;
        }
    }
}