package nl.hu.frontenddevelopment.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import nl.hu.frontenddevelopment.Model.Actor;
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
                if(!mDatabase.child("projects").child(getArguments().getString("project_id"))
                        .child("actors").child(getArguments().getString("actor_id")).child("persons").child("key").equals(person.getKey())){
                    ((TextView)v.findViewById(R.id.actor_name)).setText(person.getName());
                    ImageView userProfilePic = ((ImageView)v.findViewById(R.id.user_profile_pic));
                    Glide.with(getActivity()).load("https://lh4.googleusercontent.com/-6Cewl5Wyx7I/AAAAAAAAAAI/AAAAAAAAAAA/tWZWErkqLCE/W40-H40/photo.jpg?sz=64").crossFade().thumbnail(0.3f).bitmapTransform(new CircleTransform(getActivity())).diskCacheStrategy(DiskCacheStrategy.ALL).into(userProfilePic);

                    Button button = (Button) v.findViewById(R.id.button_add_person);
                    button.setText("add");
                    button.setBackgroundColor(Color.GREEN);
                    button.setOnClickListener(e -> addPersonToActor("",""));
                } else {
                    Button button = (Button) v.findViewById(R.id.button_add_person);
                    button.setBackgroundColor(Color.RED);
                    button.setText("remove");
                    button.setOnClickListener(e -> removePersonFromActor(person.getKey()));
                }
            }
        };
        this.setListAdapter(personListAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_person_add, container, false);
    }

    // TODO: 3/27/2017  correcly add person to actor and remove him correctly
    private void addPersonToActor(String name, String function){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //mDatabase.child("projects").child(getArguments().getString("project_id")).child("actors").child(getArguments().getString("actor_id")).child("persons").push().setValue("");
     }

    private void removePersonFromActor(String personId){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //mDatabase.child("projects").child(getArguments().getString("project_id")).child("actors").child(getArguments().getString("actor_id")).child("persons").child(personId).removeValue();
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