package nl.hu.frontenddevelopment.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import nl.hu.frontenddevelopment.Fragment.ActorOverviewFragment;
import nl.hu.frontenddevelopment.Model.Actor;
import nl.hu.frontenddevelopment.Model.Project;
import nl.hu.frontenddevelopment.R;

public class ActorOverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor_overview);
        setOverviewFragment(getIntent().getExtras().getString("project_id"));
    }

    public void setOverviewFragment(String projectId){
        getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, ActorOverviewFragment.newInstance(projectId)).commitAllowingStateLoss();
    }
}
