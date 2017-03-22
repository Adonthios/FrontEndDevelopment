package nl.hu.frontenddevelopment.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import nl.hu.frontenddevelopment.Model.Actor;
import nl.hu.frontenddevelopment.Model.Person;
import nl.hu.frontenddevelopment.R;

public class PersonAddFragment extends Fragment {
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

    /*public static PersonAddFragment newInstance(String projectID) {
        PersonAddFragment fragment = new PersonAddFragment();
        Bundle args = new Bundle();
        args.putString("project_id", projectID);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_person_add, container, false);
        name = (EditText) rootView.findViewById(R.id.person_name);
        function = (EditText) rootView.findViewById(R.id.person_function);
        bAddPerson = (Button) rootView.findViewById(R.id.button_add_person_to_actor);

        bAddPerson.setOnClickListener(e -> addPersonToActor(getArguments().getString("project_id"), getArguments().getString("actor_id"), name.getText().toString(), function.getText().toString()));

        /*if(getArguments().getString("actor_id") != null) {
            // Cannot go here lol


          *//*  description.setText(getArguments().getString("actor_description"));
            title.setText(getArguments().getString("actor_title"));
            bRemoveActor = (Button) rootView.findViewById(R.id.button_delete_actor);
            bRemoveActor.setVisibility(View.VISIBLE);
            bAddActor.setText("Save Changes");
            bRemoveActor.setOnClickListener(b -> deleteActor(getArguments().getString("actor_id")));
            bAddActor.setOnClickListener(b -> editActor(getArguments().getString("actor_id")));*//*
        } else {
            bAddPerson.setOnClickListener(e -> addPersonToActor(getArguments().getString("project_id"), getArguments().getString("actor_id"), name.getText().toString()));
        }*/
        return rootView;
    }

    private void addPersonToActor(String projectID, String actorID, String name, String function){
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Person person = new Person();
        person.setName(name);
        person.setFunction(function);

        mDatabase.child("projects").child(projectID).child("actors").child(actorID).child("persons").push().setValue(person);

        refreshFragment(getArguments().getString("project_id"));
    }

   /* private void deleteActor(String key){
        mDatabase.child("projects").child(getArguments().getString("project_id")).child("actors").child(key).removeValue();
        refreshFragment(getArguments().getString("project_id"));
    }

    private void editActor(String key){
        mDatabase.child("projects").child(getArguments().getString("project_id")).child("actors").child(key).child("title").setValue(((EditText) getView().findViewById(R.id.actor_new_title)).getText().toString());
        mDatabase.child("projects").child(getArguments().getString("project_id")).child("actors").child(key).child("description").setValue(((EditText) getView().findViewById(R.id.actor_new_description)).getText().toString());
        refreshFragment(getArguments().getString("project_id"));
    }*/

    private void refreshFragment(String projectID){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(ActorOverviewFragment.newInstance(projectID)).commit();

        this.getFragmentManager().beginTransaction()
            .replace(R.id.contentFragment, ActorOverviewFragment.newInstance(projectID))
            .addToBackStack(null)
            .commit();
    }
}