package nl.hu.frontenddevelopment.Fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.hu.frontenddevelopment.Controller.ActorAdapter;
import nl.hu.frontenddevelopment.R;
import nl.hu.frontenddevelopment.View.ActorActivity;

public class ActorOverviewFragment extends Fragment {
    private FloatingActionButton fabNewActor;

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    public static ActorOverviewFragment newInstance(String projectId) {
        ActorOverviewFragment fragment = new ActorOverviewFragment();
        Bundle args = new Bundle();
        args.putString("project_id", projectId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_actor_overview, container, false);
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.recyclerview_actor);
        rv.setHasFixedSize(true);

        ActorAdapter adapter = new ActorAdapter(getArguments().getString("project_id"));
        rv.setAdapter(adapter);
        fabNewActor = (FloatingActionButton) rootView.findViewById(R.id.fab_add_actor);
        fabNewActor.setOnClickListener(e -> ((ActorActivity) getContext()).setNewActorFragment(getArguments().getString("project_id")));

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);
        rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        return rootView;
    }
}