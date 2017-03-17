package nl.hu.frontenddevelopment.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import nl.hu.frontenddevelopment.Model.Project;
import nl.hu.frontenddevelopment.R;
import nl.hu.frontenddevelopment.View.ProjectActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectNewFragment extends Fragment {
    private EditText title, description;
    private Button bAddProject, bRemoveProject;
    private DatabaseReference mDatabase;


    public static ProjectNewFragment newInstance(String projectId, String projectTitle, String projectDescription){
        ProjectNewFragment fragment = new ProjectNewFragment();
        Bundle args = new Bundle();
        args.putString("project_id", projectId);
        args.putString("project_title", projectTitle);
        args.putString("project_description", projectDescription);
        return fragment;
    }

    public static ProjectNewFragment newInstance(){
        ProjectNewFragment fragment = new ProjectNewFragment();
        Bundle args = new Bundle();
        // TODO: Check of we dit willen hebben.
        return fragment;
    }

    public ProjectNewFragment() {
     //   super.onCreate(savedInstanceState);
     //   setContentView(R.layout.activity_project_new);
        mDatabase =  FirebaseDatabase.getInstance().getReference();

        // TODO: Vandaag, Omzeten naar Bundle
        title = (EditText) getView().findViewById(R.id.project_new_title);
        description = (EditText) getView().findViewById(R.id.project_new_description);
        bAddProject = (Button) getView().findViewById(R.id.button_add_new_project);

        if(!isNewProject()){
            description.setText(getActivity().getIntent().getExtras().getString("project_description"));
            title.setText(getActivity().getIntent().getExtras().getString("project_title"));
            bRemoveProject = (Button) getView().findViewById(R.id.button_delete_project);
            bRemoveProject.setVisibility(View.VISIBLE);
            bAddProject.setText("Save Changes");
            bRemoveProject.setOnClickListener(b -> deleteProject(getArguments().getString("project_id")));
            bAddProject.setOnClickListener(b -> editProject(getArguments().getString("project_id")));
        } else {
            bAddProject.setOnClickListener(b -> addNewProject());
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_project_new, container, false);
    }

    private void addNewProject(){
        Project project = new Project(title.getText().toString() ,description.getText().toString());
        mDatabase.child("projects").push().setValue(project);
        refreshFragment();
    }

    private void deleteProject(String key){
        mDatabase.child("projects").child(key).removeValue();
        refreshFragment();
    }

    private void editProject(String key){
        mDatabase.child("projects").child(key).child("title").setValue(((EditText) getView().findViewById(R.id.project_new_title)).getText().toString());
        mDatabase.child("projects").child(key).child("description").setValue(((EditText) getView().findViewById(R.id.project_new_description)).getText().toString());
        refreshFragment();
    }

    private boolean isNewProject(){
        if(getArguments() != null){
            return false;
        }
        return true;
    }

    private void refreshFragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }
}
