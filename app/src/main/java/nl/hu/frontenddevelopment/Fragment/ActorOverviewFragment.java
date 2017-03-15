package nl.hu.frontenddevelopment.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.hu.frontenddevelopment.Controller.ActorAdapter;
import nl.hu.frontenddevelopment.Controller.ProjectAdapter;
import nl.hu.frontenddevelopment.Model.Actor;
import nl.hu.frontenddevelopment.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActorOverviewFragment extends Fragment {


    public static ActorOverviewFragment newInstance() {
        ActorOverviewFragment fragment = new ActorOverviewFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_actor_overview, container, false);
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.recyclerview_actor);
        rv.setHasFixedSize(true);
        ActorAdapter adapter = new ActorAdapter("neger");
        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return rootView;
    }

}
