package nl.hu.frontenddevelopment.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import nl.hu.frontenddevelopment.Model.Project;
import nl.hu.frontenddevelopment.R;

public class ProjectNewActivity extends AppCompatActivity {
    private EditText title,description;
    private Button bAddProject, bRemoveProject;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_new);
        mDatabase =  FirebaseDatabase.getInstance().getReference();

        title = (EditText) findViewById(R.id.project_new_title);
        description = (EditText) findViewById(R.id.project_new_description);
        bAddProject = (Button) findViewById(R.id.button_add_new_project);

        if(!isNewProject()){
            description.setText(getIntent().getExtras().getString("project_description"));
            title.setText(getIntent().getExtras().getString("project_title"));
            bRemoveProject = (Button) findViewById(R.id.button_delete_project);
            bRemoveProject.setVisibility(View.VISIBLE);
            bAddProject.setText("Save Changes");
            bRemoveProject.setOnClickListener(b -> deleteProject(getIntent().getExtras().getString("project_key")));
            bAddProject.setOnClickListener(b -> editProject(getIntent().getExtras().getString("project_key")));
        } else {
            bAddProject.setOnClickListener(b -> addNewProject());
        }
    }

    private void addNewProject(){
        Project project = new Project(title.getText().toString() ,description.getText().toString());
        mDatabase.child("projects").push().setValue(project);
        startActivity(new Intent(this,ProjectOverviewActivity.class));
    }

    private void deleteProject(String key){
        mDatabase.child("projects").child(key).removeValue();
        startActivity(new Intent(this,ProjectOverviewActivity.class));
    }

    private void editProject(String key){
        mDatabase.child("projects").child(key).child("title").setValue(((EditText) findViewById(R.id.project_new_title)).getText().toString());
        mDatabase.child("projects").child(key).child("description").setValue(((EditText) findViewById(R.id.project_new_description)).getText().toString());
        startActivity(new Intent(this,ProjectOverviewActivity.class));
    }

    private boolean isNewProject(){
        if(getIntent().getExtras() != null){
            return false;
        }
        return true;
    }
}
