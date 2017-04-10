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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import nl.hu.frontenddevelopment.Model.Actor;
import nl.hu.frontenddevelopment.Model.ActorPerson;
import nl.hu.frontenddevelopment.Model.Person;
import nl.hu.frontenddevelopment.R;
import nl.hu.frontenddevelopment.Utils.CircleTransform;
import nl.hu.frontenddevelopment.View.ActorActivity;

public class ActorAdapter extends RecyclerView.Adapter<ActorAdapter.MyViewHolder> {

    private ArrayList<Actor> actors = new ArrayList<>();
    private ArrayList<Person> persons = new ArrayList<>();
    private String projectId, actorId;
    private Person currentUser;
    private Context context;
    private DatabaseReference mFirebaseDatabaseReference;
    private static String TAG = "ProjectAdapter";

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public CardView mCardView;
        public TextView title, description, editActor;
        public ListView personList;
        private FloatingActionButton fabArchive;


        public MyViewHolder(View v) {
            super(v);
            context = v.getContext();
            mCardView = (CardView) v.findViewById(R.id.cardview_actor);
            personList = (ListView) v.findViewById(R.id.person_list);
            editActor = (TextView) v.findViewById(R.id.actor_edit);
            editActor.setOnClickListener(e -> editActor(actorId));
            title = (TextView) v.findViewById(R.id.actor_title);
            fabArchive = (FloatingActionButton) v.findViewById(R.id.fab_archive_actor);
            description = (TextView) v.findViewById(R.id.actor_description);

        }

        private void addPerson(String actorId){
            ((ActorActivity) context).addPersonToActor(actorId);
        }

        private void toArchive(String actorId){
            ((ActorActivity) context).toArchive(actorId);
        }

        private void editActor(String actorId){
            ((ActorActivity) context).setEditActorFragment(actorId, title.getText().toString(), description.getText().toString());
        }
    }

    public ActorAdapter(String selectedProject) {
        projectId = selectedProject;
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabaseReference.child("projects").child(selectedProject).child("actors").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, dataSnapshot.toString());
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

        mFirebaseDatabaseReference.child("persons").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Person person = dataSnapshot.getValue(Person.class);
                if(person.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    currentUser = person;
                    person.setKey(dataSnapshot.getKey());
                }
                person.setKey(dataSnapshot.getKey());
                persons.add(person);
                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                notifyDataSetChanged();
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

        mFirebaseDatabaseReference.child("persons").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Person person = dataSnapshot.getValue(Person.class);
                person.setKey(dataSnapshot.getKey());
                if(!persons.contains(person)){
                    persons.add(person);
                }
                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                notifyDataSetChanged();
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
        actorId = actors.get(position).getKey();
        ArrayList<Person> addedPersons = new ArrayList<>();
        holder.title.setText(actors.get(position).title);
        holder.description.setText(actors.get(position).getDescription());

        ListAdapter personListAdapter = new FirebaseListAdapter<ActorPerson>((ActorActivity)context, ActorPerson.class, R.layout.card_actor_persons,
                mFirebaseDatabaseReference.child("projects").child(projectId)
                        .child("actors").child(actors.get(position).getKey()).child("persons")) {
            @Override
            protected void populateView(View v, ActorPerson actorPerson, int position) {
                addedPersons.clear();
                for (int i=0; i < persons.size(); i++ ){
                    Person person = persons.get(i);

                    if(actorPerson.getActorID().equals(person.getKey()) && !addedPersons.contains(person)){
                        addedPersons.add(person);
                        String baseName = context.getResources().getString(R.string.hint_name) + ": ";
                        String baseEmail = context.getResources().getString(R.string.hint_email) + ": ";
                        String baseNumber = context.getResources().getString(R.string.hint_number) + ": ";
                        String baseNotes = context.getResources().getString(R.string.hint_notes) + ": \n";
                        String functionTeamlid = context.getResources().getString(R.string.hint_function)+ ": " + context.getResources().getString(R.string.teamlid);
                        String functionAnalist = context.getResources().getString(R.string.hint_function)+ ": " + context.getResources().getString(R.string.analist);

                        ((TextView)v.findViewById(R.id.actor_name)).setText(baseName + person.getName());
                        ((TextView)v.findViewById(R.id.actor_email)).setText(baseEmail + person.getEmail());
                        ((TextView)v.findViewById(R.id.actor_number)).setText(baseNumber + person.getPhonenumber());

                        if(actorPerson.getNotes() != null){
                            ((TextView)v.findViewById(R.id.actor_notes)).setText(baseNotes + actorPerson.getNotes());
                        }

                        if(actorPerson.getActorID().equals(currentUser.getKey()) && actorPerson.canEdit){
                                ((TextView)v.findViewById(R.id.actor_function)).setText(functionAnalist);
                            holder.editActor.setVisibility(View.VISIBLE);
                            holder.description.setOnClickListener(e -> holder.addPerson(actorId));
                            holder.title.setOnClickListener(e -> holder.addPerson(actorId));
                            holder.fabArchive.setVisibility(View.VISIBLE);
                            holder.fabArchive.setOnClickListener(fab -> holder.toArchive(actorId));
                        }
                        ((TextView)v.findViewById(R.id.actor_function)).setText(functionTeamlid);

                        ImageView userProfilePic = ((ImageView)v.findViewById(R.id.user_profile_pic));
                        Glide.with(context).load(person.getProfilePhoto()).crossFade().thumbnail(0.3f).bitmapTransform(new CircleTransform(context)).diskCacheStrategy(DiskCacheStrategy.ALL).into(userProfilePic);
                        break;
                    }
                }
            }
        };

        holder.personList.setAdapter(personListAdapter);

    // Create new views (invoked by the layout manager)

}

    @Override
    public int getItemCount() {
        return actors.size();
    }
}