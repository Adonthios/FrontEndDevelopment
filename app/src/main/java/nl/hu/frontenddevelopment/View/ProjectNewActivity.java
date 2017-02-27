package nl.hu.frontenddevelopment.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import nl.hu.frontenddevelopment.Model.Project;
import nl.hu.frontenddevelopment.R;

public class ProjectNewActivity extends AppCompatActivity {
    private EditText title,description;
    private Button bAddProject;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_new);

        mDatabase =  FirebaseDatabase.getInstance().getReference();

        title = (EditText) findViewById(R.id.project_new_title);
        description = (EditText) findViewById(R.id.project_new_description);
        bAddProject = (Button) findViewById(R.id.button_add_new_project);
        bAddProject.setOnClickListener(v -> addNewProject());
    }

    private void addNewProject(){
        Project project = new Project(title.getText().toString() ,description.getText().toString());
        mDatabase.child("projects").push().setValue(project);
        startActivity(new Intent(this,ProjectOverviewActivity.class));
    }
}
