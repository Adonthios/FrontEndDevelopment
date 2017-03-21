package nl.hu.frontenddevelopment.Controller;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import nl.hu.frontenddevelopment.Model.Actor;
import nl.hu.frontenddevelopment.Model.Person;
import nl.hu.frontenddevelopment.Model.Project;
import nl.hu.frontenddevelopment.R;

/**
 * Created by Lars on 2/15/2017.
 */

//TODO: Check of goed herschreven is naar Person

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.MyViewHolder>{

    private ArrayList<Person> persons = new ArrayList<>();
    private Context context;
    private DatabaseReference mFirebaseDatabaseReference;
    private static String TAG = "PersonAdapter";

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView profilePicture;
        public TextView name;
        public FloatingActionButton fab;

        public MyViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.actor_title);
            profilePicture = (ImageView) v.findViewById(R.id.user_profile_pic);
            v.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();


        }
        private void editActor(){
            int pos = getAdapterPosition();
        }
    }

    public PersonAdapter(String selectedProject, String selectedActor) {
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabaseReference.child("projects").child(selectedProject).child("actors").child(selectedActor).child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Person person = dataSnapshot.getValue(Person.class);
                person.setKey(dataSnapshot.getKey());
                persons.add(person);
                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Person person = dataSnapshot.getValue(Person.class);
                persons.remove(person);
                notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PersonAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_actor_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.name.setText(persons.get(position).getName());
        FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();
        holder.profilePicture.setImageURI(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl());
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }
}