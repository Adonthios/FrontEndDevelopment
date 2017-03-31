package nl.hu.frontenddevelopment.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import nl.hu.frontenddevelopment.Model.Person;
import nl.hu.frontenddevelopment.R;

public class BaseAuthActivity extends AppCompatActivity {
    public ProgressDialog mProgressDialog;
    private DatabaseReference mDatabase;
    private static Boolean hasToAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_base_auth);
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    protected void checkUserExisted(Person person, String userID) {
        hasToAdd = true;
        mDatabase.child("persons").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hasToAdd = true;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Person person = snapshot.getValue(Person.class);

                    Log.d("PERSON","ID = " + person.getKey());
                    if(person.getKey().equals(userID)){
                        hasToAdd = false;
                        break;
                    }
                }
                if (hasToAdd) addToPersonDatabase(person,userID);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void addToPersonDatabase(Person person, String userID) {
        person.setKey(userID);
        //person.setProfilePhoto(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
        mDatabase.child("persons").push().setValue(person);
    }

    protected void goToHomeActivity() {
        startActivity(new Intent(this, ProjectActivity.class));
    }
}
