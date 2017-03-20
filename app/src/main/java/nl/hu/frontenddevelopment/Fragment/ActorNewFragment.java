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

public class ActorNewFragment extends Fragment {
    private EditText title, description;
    public Button bAddActor, bRemoveActor;
    private DatabaseReference mDatabase;

    public ActorNewFragment() {
        // Required empty public constructor
    }

    public static ActorNewFragment newInstance(String actorID, String actorTitle, String actorDescription) {
        ActorNewFragment fragment = new ActorNewFragment();
        Bundle args = new Bundle();
        args.putString("actor_id", actorID);
        args.putString("actor_title", actorTitle);
        args.putString("actor_description", actorDescription);
        fragment.setArguments(args);
        return fragment;
    }

    public static ActorNewFragment newInstance() {
        return new ActorNewFragment();
    }

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

        if(getArguments() != null) {
            description.setText(getArguments().getString("actor_description"));
            title.setText(getArguments().getString("actor_title"));
            bRemoveActor = (Button) rootView.findViewById(R.id.button_delete_actor);
            bRemoveActor.setVisibility(View.VISIBLE);
            bAddActor.setText("Save Changes");
            bRemoveActor.setOnClickListener(b -> deleteActor(getArguments().getString("actor_id")));
            bAddActor.setOnClickListener(b -> editActor(getArguments().getString("actor_id")));
        } else {
            bAddActor.setOnClickListener(e -> addNewActor(title.getText().toString(), description.getText().toString()));
        }
        return rootView;
    }

    private void addNewActor(String title, String description){
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Actor actor = new Actor();
        actor.setTitle(title);
        actor.setDescription(description);

     //   mDatabase.child("projects").child(projectId).child("actors").push().setValue(actor);
        mDatabase.child("projects").push().setValue(actor);
        refreshFragment();

        refreshFragment();
    }

    private void deleteActor(String key){
        mDatabase.child("actors").child(key).removeValue();
        refreshFragment();
    }

    private void editActor(String key){
        mDatabase.child("actors").child(key).child("title").setValue(((EditText) getView().findViewById(R.id.actor_new_title)).getText().toString());
        mDatabase.child("actors").child(key).child("description").setValue(((EditText) getView().findViewById(R.id.actor_new_description)).getText().toString());
        refreshFragment();
    }

    private void refreshFragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(ActorOverviewFragment.newInstance()).commit();

        this.getFragmentManager().beginTransaction()
            .replace(R.id.contentFragment, ActorOverviewFragment.newInstance())
            .addToBackStack(null)
            .commit();
    }
}