package nl.hu.frontenddevelopment.Controller;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nl.hu.frontenddevelopment.Model.Project;
import nl.hu.frontenddevelopment.R;

/**
 * Created by Lars on 2/15/2017.
 */

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.MyViewHolder> {
    private ArrayList<Project> projects;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {


        public CardView mCardView;
        public TextView title,description;
        public MyViewHolder(View v) {
            super(v);
            mCardView = (CardView) v.findViewById(R.id.cardview);
            title = (TextView) v.findViewById(R.id.project_title);
            description = (TextView) v.findViewById(R.id.project_description);
        }
    }

    public ProjectAdapter(ArrayList<Project> projects) {
        this.projects = projects;
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