package nl.hu.frontenddevelopment.Controller;

import android.content.Context;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;
import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import nl.hu.frontenddevelopment.Model.Actor;
import nl.hu.frontenddevelopment.Model.Person;
import nl.hu.frontenddevelopment.R;
import nl.hu.frontenddevelopment.Utils.CircleTransform;
import nl.hu.frontenddevelopment.View.ActorActivity;

public class ActorAdapter extends RecyclerView.Adapter<ActorAdapter.MyViewHolder>{

    private ArrayList<Actor> actors = new ArrayList<>();
    private String projectId;
    private Context context;
    private DatabaseReference mFirebaseDatabaseReference;
    private static String TAG = "ProjectAdapter";

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public CardView mCardView;
        public TextView title,description;
        public ListView personList;

        public MyViewHolder(View v) {
            super(v);
            context = v.getContext();
            mCardView = (CardView) v.findViewById(R.id.cardview_actor);
            personList = (ListView) v.findViewById(R.id.person_list);
            title = (TextView) v.findViewById(R.id.actor_title);
            description = (TextView) v.findViewById(R.id.actor_description);
            v.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            Actor actor = actors.get(pos);
            ((ActorActivity) context).setDetailActorFragment(actor.getKey(), actor.getTitle(), actor.getDescription());
        }
    }

    public ActorAdapter(String selectedProject) {
        projectId = selectedProject;
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabaseReference.child("projects").child(selectedProject).child("actors").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Actor actor = dataSnapshot.getValue(Actor.class);
                actor.setKey(dataSnapshot.getKey());
                actors.add(actor);
                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Actor actor = dataSnapshot.getValue(Actor.class);
                actors.remove(actor);
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
    public ActorAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_actor_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.title.setText(actors.get(position).title);
        holder.description.setText(actors.get(position).getDescription());
        // TODO: 3/27/2017 Get only person for correct actor 
        ListAdapter personListAdapter = new FirebaseListAdapter<Person>((ActorActivity)context, Person.class, R.layout.actor_list_item,
                mFirebaseDatabaseReference.child("persons")) {
            @Override
            protected void populateView(View v, Person person, int position) {
                ((TextView)v.findViewById(R.id.actor_name)).setText(person.getName());
                ImageView userProfilePic = ((ImageView)v.findViewById(R.id.user_profile_pic));
                Glide.with(context).load("https://lh4.googleusercontent.com/-6Cewl5Wyx7I/AAAAAAAAAAI/AAAAAAAAAAA/tWZWErkqLCE/W40-H40/photo.jpg?sz=64").crossFade().thumbnail(0.3f).bitmapTransform(new CircleTransform(context)).diskCacheStrategy(DiskCacheStrategy.ALL).into(userProfilePic);
            }
        };
        holder.personList.setAdapter(personListAdapter);
    }

    @Override
    public int getItemCount() {
        return actors.size();
    }
}