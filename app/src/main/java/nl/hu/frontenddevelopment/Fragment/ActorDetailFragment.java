package nl.hu.frontenddevelopment.Fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import nl.hu.frontenddevelopment.R;
import nl.hu.frontenddevelopment.View.ActorActivity;

public class ActorDetailFragment extends Fragment {
    TextView title, description;
    FloatingActionButton fab, fabAddPerson;

    public ActorDetailFragment() {
        // Required empty public constructor
    }

    public static ActorDetailFragment newInstance(String projectID, String actorID, String actorTitle, String actorDescription) {
        ActorDetailFragment fragment = new ActorDetailFragment();
        Bundle args = new Bundle();
        args.putString("project_id", projectID);
        args.putString("actor_id", actorID);
        args.putString("actor_title", actorTitle);
        args.putString("actor_description", actorDescription);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_actor_detail, container, false);

        title = (TextView) rootView.findViewById(R.id.actor_title);
        description = (TextView) rootView.findViewById(R.id.actor_description);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab_actor_edit);
        title.setText(getArguments().getString("actor_title"));
        description.setText(getArguments().getString("actor_description"));

        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.recyclerview_actor_detail);
        rv.setHasFixedSize(true);

        ActorActivity activity = (ActorActivity) getActivity();
        fab.setOnClickListener(e -> activity.setEditActorFragment(getArguments().getString("actor_id"),getArguments().getString("actor_title"),getArguments().getString("actor_description")));

        fabAddPerson = (FloatingActionButton) rootView.findViewById(R.id.fab_person_add);
        fabAddPerson.setOnClickListener(e -> ((ActorActivity) getContext()).addPersonToActor(getArguments().getString("actor_id")));

        return rootView;
    }
}