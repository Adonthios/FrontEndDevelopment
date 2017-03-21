package nl.hu.frontenddevelopment.Controller;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import nl.hu.frontenddevelopment.Model.Actor;
import nl.hu.frontenddevelopment.R;
import nl.hu.frontenddevelopment.View.ActorActivity;

public class ActorAdapter extends RecyclerView.Adapter<ActorAdapter.MyViewHolder>{

    private ArrayList<Actor> actors = new ArrayList<>();
    private Context context;
    private DatabaseReference mFirebaseDatabaseReference;
    private static String TAG = "ProjectAdapter";

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public CardView mCardView;
        public TextView title,description;

        public MyViewHolder(View v) {
            super(v);
            context = v.getContext();
            mCardView = (CardView) v.findViewById(R.id.cardview_actor);
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
    }

    @Override
    public int getItemCount() {
        return actors.size();
    }
}