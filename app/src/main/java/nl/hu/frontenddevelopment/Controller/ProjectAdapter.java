package nl.hu.frontenddevelopment.Controller;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

import nl.hu.frontenddevelopment.Fragment.ProjectDetailFragment;
import nl.hu.frontenddevelopment.Fragment.ProjectOverviewFragment;
import nl.hu.frontenddevelopment.Model.Project;
import nl.hu.frontenddevelopment.R;
import nl.hu.frontenddevelopment.View.MainActivity;
import nl.hu.frontenddevelopment.View.ProjectOverviewActivity;

/**
 * Created by Lars on 2/15/2017.
 */

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.MyViewHolder>{

    private ArrayList<Project> projects;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public CardView mCardView;
        public TextView title,description;

        public MyViewHolder(View v) {
            super(v);
            mCardView = (CardView) v.findViewById(R.id.cardview);
            title = (TextView) v.findViewById(R.id.project_title);
            description = (TextView) v.findViewById(R.id.project_description);
            v.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            Project project = projects.get(pos);
            ((ProjectOverviewActivity) context).setDetailFragment();
        }
    }

    public ProjectAdapter(Context context, ArrayList<Project> projects) {
        this.projects = projects;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ProjectAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.title.setText(projects.get(position).name);
        holder.description.setText(projects.get(position).description);
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }
}