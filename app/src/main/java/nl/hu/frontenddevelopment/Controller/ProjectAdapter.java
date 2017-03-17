package nl.hu.frontenddevelopment.Controller;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import nl.hu.frontenddevelopment.Model.Project;
import nl.hu.frontenddevelopment.R;
import nl.hu.frontenddevelopment.View.ProjectActivity;

/**
 * Created by Lars on 2/15/2017.
 */

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.MyViewHolder>{

    private ArrayList<Project> projects = new ArrayList<>();
    private Context context;
    private DatabaseReference mFirebaseDatabaseReference;
    private static String TAG = "ProjectAdapter";

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public CardView mCardView;
        public TextView title,description;
        public FloatingActionButton fab;

        public MyViewHolder(View v) {
            super(v);
            context = v.getContext();
            mCardView = (CardView) v.findViewById(R.id.cardview);
            title = (TextView) v.findViewById(R.id.project_title);
            description = (TextView) v.findViewById(R.id.project_description);
            fab = (FloatingActionButton) v.findViewById(R.id.fab_project_edit);
            fab.setOnClickListener(e -> editProject());
            v.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            Project project = projects.get(pos);
            ((ProjectActivity) context).setDetailProject(project.getKey());
        }

        private void editProject(){
            int pos = getAdapterPosition();
            Project project = projects.get(pos);
            ((ProjectActivity) context).setEditProject(project.getKey(),project.getTitle(),project.getDescription());
        }
    }

    public ProjectAdapter() {
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseDatabaseReference.child("projects").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Project project = dataSnapshot.getValue(Project.class);
                project.setKey(dataSnapshot.getKey());
                Log.d("KEY VALUE = ",dataSnapshot.getKey() );
                projects.add(project);
                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Project project = dataSnapshot.getValue(Project.class);
                projects.remove(project);
                notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ProjectAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_project_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.title.setText(projects.get(position).title);
        holder.description.setText(projects.get(position).description);
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }
}