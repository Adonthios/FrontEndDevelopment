package nl.hu.frontenddevelopment.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import nl.hu.frontenddevelopment.Model.Actor;
import nl.hu.frontenddevelopment.R;
import nl.hu.frontenddevelopment.View.ActorOverviewActivity;
import nl.hu.frontenddevelopment.View.ProjectOverviewActivity;


public class ActorNewFragment extends Fragment {
    private EditText title, description;
    public Button bAddActor;
    private DatabaseReference mDatabase;

    public ActorNewFragment() {
        // Required empty public constructor
    }

    public static ActorNewFragment newInstance(String projectID) {
        ActorNewFragment fragment = new ActorNewFragment();
        Bundle args = new Bundle();
        args.putString("project_id",projectID);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        title = (EditText) getView().findViewById(R.id.new_actor_title);
        description = (EditText) getView().findViewById(R.id.new_actor_title);


        bAddActor = (Button) getActivity().findViewById(R.id.button_add_new_actor);
        bAddActor.setOnClickListener(e -> addNewActor(getArguments().getString("project_id"), title.getText().toString(),
                description.getText().toString()));
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_actor_new, container, false);
    }

    private void addNewActor(String projectId, String title, String description){
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Actor actor = new Actor();
        actor.setTitle(title);
        actor.setDescription(description);

        mDatabase.child("projects").child(projectId).push().setValue(actor);
    }
}