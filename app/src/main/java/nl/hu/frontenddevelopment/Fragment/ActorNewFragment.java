package nl.hu.frontenddevelopment.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
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

public class ActorNewFragment extends Fragment {
    private EditText title, description;
    public Button bAddActor, bRemoveActor;
    private DatabaseReference mDatabase;
    private Person currentPerson;

    public ActorNewFragment() {
        // Required empty public constructor
    }

    public static ActorNewFragment newInstance(String projectID, String actorID, String actorTitle, String actorDescription) {
        ActorNewFragment fragment = new ActorNewFragment();
        Bundle args = new Bundle();
        args.putString("project_id", projectID);
        args.putString("actor_id", actorID);
        args.putString("actor_title", actorTitle);
        args.putString("actor_description", actorDescription);
        fragment.setArguments(args);
        return fragment;
    }

    public static ActorNewFragment newInstance(String projectID) {
        ActorNewFragment fragment = new ActorNewFragment();
        Bundle args = new Bundle();
        args.putString("project_id", projectID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("persons").addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Person person = dataSnapshot.getValue(Person.class);
                if(person.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    currentPerson = person;
                    currentPerson.setKey(dataSnapshot.getKey());
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_actor_new, container, false);
        title = (EditText) rootView.findViewById(R.id.actor_new_title);
        description = (EditText) rootView.findViewById(R.id.actor_new_description);
        bAddActor = (Button) rootView.findViewById(R.id.button_add_new_actor);

        if(getArguments().getString("actor_id") != null) {
            description.setText(getArguments().getString("actor_description"));
            title.setText(getArguments().getString("actor_title"));
            bRemoveActor = (Button) rootView.findViewById(R.id.button_delete_actor);
            bRemoveActor.setVisibility(View.VISIBLE);
            bAddActor.setText(R.string.button_save);
            bRemoveActor.setOnClickListener(b -> deleteActor(getArguments().getString("actor_id")));
            bAddActor.setOnClickListener(b -> editActor(getArguments().getString("actor_id")));
        } else {
            bAddActor.setOnClickListener(e -> addNewActor(getArguments().getString("project_id"), title.getText().toString(), description.getText().toString()));
        }
        return rootView;
    }

    private void addNewActor(String projectID, String title, String description){
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Actor actor = new Actor();
        actor.setTitle(title);
        actor.setDescription(description);
        ActorPerson actorPerson = new ActorPerson();
        actorPerson.setCanEdit(true);
        actorPerson.setActorID(currentPerson.getKey());

        String key = mDatabase.child("projects").child(projectID).child("actors").push().getKey();
        mDatabase.child("projects").child(projectID).child("actors").child(key).setValue(actor);
        String personActorKey = mDatabase.child("projects").child(projectID).child("actors").child(key).child("persons").push().getKey();
        mDatabase.child("projects").child(projectID).child("actors").child(key).child("persons").child(personActorKey).setValue(actorPerson);
        Toast.makeText(getActivity(), R.string.toast_add_succesful, Toast.LENGTH_SHORT).show();
        refreshFragment(getArguments().getString("project_id"));
    }

    private void deleteActor(String key){
        if(key != null && !key.equals("")) {
            mDatabase.child("projects").child(getArguments().getString("project_id")).child("actors").child(key).removeValue();
            Toast.makeText(getActivity(), R.string.toast_delete_succesful, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), R.string.toast_delete_failed, Toast.LENGTH_SHORT).show();
        }
        refreshFragment(getArguments().getString("project_id"));
    }

    private void editActor(String key){
        if(key != null && !key.equals("")) {
            mDatabase.child("projects").child(getArguments().getString("project_id")).child("actors").child(key).child("title").setValue(((EditText) getView().findViewById(R.id.actor_new_title)).getText().toString());
            mDatabase.child("projects").child(getArguments().getString("project_id")).child("actors").child(key).child("description").setValue(((EditText) getView().findViewById(R.id.actor_new_description)).getText().toString());
            Toast.makeText(getActivity(), R.string.toast_change_succesful, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), R.string.toast_change_failed, Toast.LENGTH_SHORT).show();
        }
        refreshFragment(getArguments().getString("project_id"));
    }

    private void refreshFragment(String projectID){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(ActorOverviewFragment.newInstance(projectID)).commit();

        this.getFragmentManager().beginTransaction()
            .replace(R.id.contentFragment, ActorOverviewFragment.newInstance(projectID))
            .addToBackStack(null)
            .commit();
    }
}