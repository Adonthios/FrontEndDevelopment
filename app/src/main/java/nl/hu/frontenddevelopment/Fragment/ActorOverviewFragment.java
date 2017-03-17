package nl.hu.frontenddevelopment.Fragment;


import android.content.Intent;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class ActorOverviewFragment extends Fragment {
    FloatingActionButton fabNewActor;

    public static ActorOverviewFragment newInstance(String projectId) {
        ActorOverviewFragment fragment = new ActorOverviewFragment();
        Bundle args = new Bundle();
        args.putString("project_id",projectId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        fabNewActor = (FloatingActionButton) getView().findViewById(R.id.fab_project_edit);

        // TODO: 3/15/2017 start ActorNewFragment
        fabNewActor.setOnClickListener(e -> goToActorNewActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_actor_overview, container, false);
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.recyclerview_actor);
        rv.setHasFixedSize(true);
        ActorAdapter adapter = new ActorAdapter(getArguments().getString("project_id"));
        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return rootView;
    }

    private void goToActorNewActivity(){
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, ProjectOverviewFragment.newInstance()).commitAllowingStateLoss();
        //startActivity(new Intent(getActivity(), ProjectNewActivity.class));
    }
}
