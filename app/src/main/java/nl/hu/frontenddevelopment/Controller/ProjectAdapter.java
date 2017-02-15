package nl.hu.frontenddevelopment.Controller;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nl.hu.frontenddevelopment.Model.Project;
import nl.hu.frontenddevelopment.R;

/**
 * Created by Lars on 2/15/2017.
 */

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>{

    public static class ProjectViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView title, description;

        ProjectViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cardview);
            title = (TextView)itemView.findViewById(R.id.project_title);
            description = (TextView)itemView.findViewById(R.id.project_description);
        }
    }

    List<Project> projects;

    public ProjectAdapter(List<Project> projects){
        this.projects = projects;
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_layout, viewGroup, false);
        ProjectViewHolder bvh = new ProjectViewHolder(v);
        return bvh;
    }

    @Override
    public void onBindViewHolder(ProjectViewHolder bookViewHolder, int i) {
        bookViewHolder.title.setText(projects.get(i).name);
        bookViewHolder.description.setText(projects.get(i).description);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}