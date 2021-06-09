package com.rtech.contactfirestone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ContactListActivity extends AppCompatActivity {
    private RecyclerView mFireStoreList;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    private FloatingActionButton fab;
    private DocumentReference documentReference;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Contacts");
        setContentView(R.layout.activity_contact_list);
        firebaseFirestore = FirebaseFirestore.getInstance();
        mFireStoreList = findViewById(R.id.fireStoreList);
        documentReference = firebaseFirestore.collection("users").document();
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        Query query = firebaseFirestore.collection("users").orderBy("name");

        FirestoreRecyclerOptions<ContactModel> options = new FirestoreRecyclerOptions.Builder<ContactModel>()
                .setQuery(query, ContactModel.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<ContactModel, ContactHolder>(options) {
            @NonNull
            @Override
            public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_list, parent, false);
                return new ContactHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ContactHolder holder, int position, @NonNull ContactModel model) {
                holder.list_name.setText(model.getName());
                holder.list_contact.setText(model.getContact());

            }
        };
        mFireStoreList.setHasFixedSize(true);
        mFireStoreList.setLayoutManager(new LinearLayoutManager(this));
        mFireStoreList.setAdapter(adapter);

    }



    private class ContactHolder extends RecyclerView.ViewHolder {
        TextView list_name, list_contact;
        ImageView lis_edit, list_delete;

        public ContactHolder(@NonNull View itemView) {
            super(itemView);
            list_name = itemView.findViewById(R.id.listname);
            list_contact = itemView.findViewById(R.id.listcontact);
            lis_edit = itemView.findViewById(R.id.edit);
            list_delete =itemView.findViewById(R.id.delete);
            builder = new AlertDialog.Builder(ContactListActivity.this);
            list_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Uncomment the below code to Set the message and title from the strings.xml file
                    builder.setMessage("Are want to Delete") .setTitle("Delete Contact");

                    //Setting message manually and performing action on button click
                    builder.setMessage("Do you want to close this application ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();

                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("AlertDialogExample");
                    alert.show();
                }
            });

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }
}