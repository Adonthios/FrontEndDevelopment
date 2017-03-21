package nl.hu.frontenddevelopment.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import nl.hu.frontenddevelopment.Controller.ActorAdapter;
import nl.hu.frontenddevelopment.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActorDetailFragment extends Fragment {
    TextView title, description;

    public ActorDetailFragment() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_actor_detail, container, false);

        title = (TextView) rootView.findViewById(R.id.actor_title);
        description = (TextView) rootView.findViewById(R.id.actor_description);

        title.setText(getArguments().getString("actor_title"));
        description.setText(getArguments().getString("actor_description"));

        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.recyclerview_actor);
        rv.setHasFixedSize(true);

        // TODO: 3/21/2017 Create adapter and load the persons for this Actor (card_actor_detail_item)
       /* ActorAdapter adapter = new ActorAdapter(getArguments().getString("project_id"));
        rv.setAdapter(adapter);*/

        return rootView;
    }

}
