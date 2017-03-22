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
import nl.hu.frontenddevelopment.R;

public class PersonAddFragment extends Fragment {
    private EditText title, description;
    public Button bAddActor, bRemoveActor;
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
        View rootView =  inflater.inflate(R.layout.fragment_actor_new, container, false);
        title = (EditText) rootView.findViewById(R.id.actor_new_title);
        description = (EditText) rootView.findViewById(R.id.actor_new_description);
        bAddActor = (Button) rootView.findViewById(R.id.button_add_new_actor);

        if(getArguments().getString("actor_id") != null) {
            description.setText(getArguments().getString("actor_description"));
            title.setText(getArguments().getString("actor_title"));
            bRemoveActor = (Button) rootView.findViewById(R.id.button_delete_actor);
            bRemoveActor.setVisibility(View.VISIBLE);
            bAddActor.setText("Save Changes");
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

        mDatabase.child("projects").child(projectID).child("actors").push().setValue(actor);

        refreshFragment(getArguments().getString("project_id"));
    }

    private void deleteActor(String key){
        mDatabase.child("projects").child(getArguments().getString("project_id")).child("actors").child(key).removeValue();
        refreshFragment(getArguments().getString("project_id"));
    }

    private void editActor(String key){
        mDatabase.child("projects").child(getArguments().getString("project_id")).child("actors").child(key).child("title").setValue(((EditText) getView().findViewById(R.id.actor_new_title)).getText().toString());
        mDatabase.child("projects").child(getArguments().getString("project_id")).child("actors").child(key).child("description").setValue(((EditText) getView().findViewById(R.id.actor_new_description)).getText().toString());
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