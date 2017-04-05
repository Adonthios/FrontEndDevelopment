package nl.hu.frontenddevelopment.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import nl.hu.frontenddevelopment.Model.Actor;
import nl.hu.frontenddevelopment.Model.ActorPerson;
import nl.hu.frontenddevelopment.Model.Person;
import nl.hu.frontenddevelopment.R;
import nl.hu.frontenddevelopment.Utils.CircleTransform;
import nl.hu.frontenddevelopment.View.ActorActivity;

public class PersonAddFragment extends ListFragment {
    private EditText name, function;
    public Button bAddPerson;
    private DatabaseReference mDatabase;

    public PersonAddFragment() {
        // Required empty public constructor
    }

    public static PersonAddFragment newInstance(String projectID, String actorID) {
        PersonAddFragment fragment = new PersonAddFragment();
        Bundle args = new Bundle();
        args.putString("project_id", projectID);
        args.putString("actor_id", actorID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        ListAdapter personListAdapter = new FirebaseListAdapter<Person>(getActivity(), Person.class, R.layout.actor_add_list_item,
                mDatabase.child("persons")) {
            @Override
            protected void populateView(View v, Person person, int position) {
                DatabaseReference itemRef = getRef(position);
                String itemKey = itemRef.getKey();
                person.setKey(itemKey);
                Log.d("PERSON KEY=", person.getKey());
                ImageView userProfilePic = ((ImageView)v.findViewById(R.id.user_profile_pic));
                Glide.with(getActivity()).load(person.getProfilePhoto()).crossFade().thumbnail(0.3f).bitmapTransform(new CircleTransform(getActivity())).diskCacheStrategy(DiskCacheStrategy.ALL).into(userProfilePic);
                ((TextView)v.findViewById(R.id.actor_name)).setText(person.getName());
                CheckBox checkBox = (CheckBox) v.findViewById(R.id.can_edit);

                Button button = (Button) v.findViewById(R.id.button_add_person);
                button.setText("add");
                button.setBackgroundColor(Color.GREEN);
                button.setOnClickListener(e -> addPersonToActor(person.getKey(), checkBox.isChecked()));

                mDatabase.child("projects").child(getArguments().getString("project_id"))
                        .child("actors").child(getArguments().getString("actor_id")).child("persons").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        ActorPerson actorPerson =  dataSnapshot.getValue(ActorPerson.class);
                        if(actorPerson.getActorID().equals(person.getKey())){
                            Button button = (Button) v.findViewById(R.id.button_add_person);
                            checkBox.setChecked(actorPerson.isCanEdit());
                            button.setBackgroundColor(Color.RED);
                            button.setText("remove");
                            button.setOnClickListener(e -> removePersonFromActor(dataSnapshot.getKey()));
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };
        this.setListAdapter(personListAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_person_add, container, false);
    }

    private void addPersonToActor(String id, boolean canEdit){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        ActorPerson actorPerson = new ActorPerson(id, canEdit);
        mDatabase.child("projects").child(getArguments().getString("project_id")).child("actors").child(getArguments().getString("actor_id")).child("persons").push().setValue(actorPerson);
        refreshFragment();
     }

    private void removePersonFromActor(String personId){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("projects").child(getArguments().getString("project_id")).child("actors").child(getArguments().getString("actor_id")).child("persons").child(personId).removeValue();
        refreshFragment();
    }

    private void refreshFragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(ActorOverviewFragment.newInstance(getArguments().getString("project_id"))).commit();

        this.getFragmentManager().beginTransaction()
            .replace(R.id.contentFragment, PersonAddFragment.newInstance(getArguments().getString("project_id"),getArguments().getString("actor_id"))).addToBackStack(null).commit();
    }
}