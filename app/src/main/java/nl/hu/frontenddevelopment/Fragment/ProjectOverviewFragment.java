package nl.hu.frontenddevelopment.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.hu.frontenddevelopment.Controller.ProjectAdapter;
import nl.hu.frontenddevelopment.R;
import nl.hu.frontenddevelopment.View.ProjectActivity;

public class ProjectOverviewFragment extends Fragment {
    private FloatingActionButton fabNewProject;

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    public static ProjectOverviewFragment newInstance() { return new ProjectOverviewFragment(); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_project_overview, container, false);
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        rv.setHasFixedSize(true);
        ProjectAdapter adapter = new ProjectAdapter(getContext());
        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        fabNewProject = (FloatingActionButton) rootView.findViewById(R.id.fab_project_new);
        fabNewProject.setOnClickListener(e -> ((ProjectActivity) getContext()).setNewProject());

        return rootView;
    }
}