package nl.hu.frontenddevelopment.View;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import nl.hu.frontenddevelopment.Fragment.ActorNewFragment;
import nl.hu.frontenddevelopment.Fragment.ActorOverviewFragment;
import nl.hu.frontenddevelopment.Fragment.PersonAddFragment;
import nl.hu.frontenddevelopment.Model.Actor;
import nl.hu.frontenddevelopment.R;

public class ActorActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOverviewFragment(getIntent().getExtras().getString("project_id"));
    }

    public void setOverviewFragment(String projectID){
        getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, ActorOverviewFragment.newInstance(projectID)).commit();
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

    public void addPersonToActor(String actorID) {
        View v = findViewById(R.id.contentFragment);
        String tag = v.getTag().toString();
        if(tag.equals("tablet")){
            getSupportFragmentManager().beginTransaction().replace(R.id.detailFragment, PersonAddFragment.newInstance(getIntent().getExtras().getString("project_id"), actorID)).addToBackStack(null).commit();
        } else{
            getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, PersonAddFragment.newInstance(getIntent().getExtras().getString("project_id"), actorID)).addToBackStack(null).commit();
        }
    }

    public void toArchive(String actorID){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("projects").child(getIntent().getExtras().getString("project_id")).child("actors").child(actorID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDatabase.child("projects").child(getIntent().getExtras().getString("project_id")).child("archive_actors").push().setValue(dataSnapshot.getValue());
                mDatabase.child("projects").child(getIntent().getExtras().getString("project_id")).child("actors").child(actorID).removeValue();
                refresh();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void refresh(){
        View v = findViewById(R.id.contentFragment);
        String tag = v.getTag().toString();
        if(tag.equals("tablet")){
            getSupportFragmentManager().beginTransaction().replace(R.id.detailFragment, ActorOverviewFragment.newInstance(getIntent().getExtras().getString("project_id"))).addToBackStack(null).commit();
        } else{
            getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, ActorOverviewFragment.newInstance(getIntent().getExtras().getString("project_id"))).addToBackStack(null).commit();
        }
    }
}