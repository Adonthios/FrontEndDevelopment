package nl.hu.frontenddevelopment.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import nl.hu.frontenddevelopment.Fragment.ActorDetailFragment;
import nl.hu.frontenddevelopment.Fragment.ActorNewFragment;
import nl.hu.frontenddevelopment.Fragment.ActorOverviewFragment;
import nl.hu.frontenddevelopment.Fragment.PersonAddFragment;
import nl.hu.frontenddevelopment.R;

public class ActorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor);
        setOverviewFragment(getIntent().getExtras().getString("project_id"));
    }

    public void setOverviewFragment(String projectID){
        getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, ActorOverviewFragment.newInstance(projectID)).addToBackStack(null).commit();
    }

    public void setEditActorFragment(String actorID, String actorTitle, String actorDescription){
        View v = findViewById(R.id.contentFragment);
        String tag = v.getTag().toString();
        if(tag.equals("tablet")){
            getSupportFragmentManager().beginTransaction().replace(R.id.detailFragment, ActorNewFragment.newInstance(getIntent().getExtras().getString("project_id"), actorID, actorTitle, actorDescription)).addToBackStack(null).commit();
        } else{
            getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, ActorNewFragment.newInstance(getIntent().getExtras().getString("project_id"), actorID, actorTitle, actorDescription)).addToBackStack(null).commit();
        }
    }

    public void setNewActorFragment(String projectID){
        View v = findViewById(R.id.contentFragment);
        String tag = v.getTag().toString();
        if(tag.equals("tablet")){
            getSupportFragmentManager().beginTransaction().replace(R.id.detailFragment, ActorNewFragment.newInstance(projectID)).addToBackStack(null).commit();
        } else{
            getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, ActorNewFragment.newInstance(projectID)).addToBackStack(null).commit();
        }
    }

    public void setDetailActorFragment(String actorID, String actorTitle, String actorDescription){
        View v = findViewById(R.id.contentFragment);
        String tag = v.getTag().toString();
        if(tag.equals("tablet")){
            getSupportFragmentManager().beginTransaction().replace(R.id.detailFragment, ActorDetailFragment.newInstance(getIntent().getExtras().getString("project_id"),actorID, actorTitle, actorDescription)).addToBackStack(null).commit();
        } else{
            getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, ActorDetailFragment.newInstance(getIntent().getExtras().getString("project_id"),actorID, actorTitle, actorDescription)).addToBackStack(null).commit();
        }
    }

    public void addPersonToActor(String actorID) {
        View v = findViewById(R.id.contentFragment);
        String tag = v.getTag().toString();
        if(tag.equals("tablet")){
            getSupportFragmentManager().beginTransaction().replace(R.id.detailFragment, PersonAddFragment.newInstance(getIntent().getExtras().getString("project_id"), actorID)).addToBackStack(null).commit();
        } else{
            getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, PersonAddFragment.newInstance(getIntent().getExtras().getString("project_id"), actorID)).addToBackStack(null).commit();
        }
    }
}