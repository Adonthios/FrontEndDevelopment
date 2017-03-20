package nl.hu.frontenddevelopment.Fragment;

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

public class ProjectNewFragment extends Fragment {
    private EditText title, description;
    private Button bAddProject, bRemoveProject;
    private DatabaseReference mDatabase;

    public ProjectNewFragment() {
        // Required empty public constructor
    }

    public static ProjectNewFragment newInstance(String projectId, String projectTitle, String projectDescription){
        ProjectNewFragment fragment = new ProjectNewFragment();
        Bundle args = new Bundle();
        args.putString("project_id", projectId);
        args.putString("project_title", projectTitle);
        args.putString("project_description", projectDescription);
        fragment.setArguments(args);
        return fragment;
    }

    public static ProjectNewFragment newInstance(){
        return new ProjectNewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview =  inflater.inflate(R.layout.fragment_project_new, container, false);
        title = (EditText) rootview.findViewById(R.id.project_new_title);
        description = (EditText) rootview.findViewById(R.id.project_new_description);
        bAddProject = (Button) rootview.findViewById(R.id.button_add_new_project);

        if(getArguments() != null){
            description.setText(getArguments().getString("project_description"));
            title.setText(getArguments().getString("project_title"));
            bRemoveProject = (Button) rootview.findViewById(R.id.button_delete_project);
            bRemoveProject.setVisibility(View.VISIBLE);
            bAddProject.setText("Save Changes");
            bRemoveProject.setOnClickListener(b -> deleteProject(getArguments().getString("project_id")));
            bAddProject.setOnClickListener(b -> editProject(getArguments().getString("project_id")));
        } else {
            bAddProject.setOnClickListener(b -> addNewProject());
        }

        return rootview;
    }

    private void addNewProject(){
        Project project = new Project(title.getText().toString(), description.getText().toString());
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

     private void refreshFragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(ProjectOverviewFragment.newInstance()).commit();

         this.getFragmentManager().beginTransaction()
             .replace(R.id.contentFragment, ProjectOverviewFragment.newInstance())
             .addToBackStack(null)
             .commit();
    }
}