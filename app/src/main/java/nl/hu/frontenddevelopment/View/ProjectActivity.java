package nl.hu.frontenddevelopment.View;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URI;

import nl.hu.frontenddevelopment.Fragment.ActorOverviewFragment;
import nl.hu.frontenddevelopment.Fragment.EditPersonFragment;
import nl.hu.frontenddevelopment.Fragment.ProjectNewFragment;
import nl.hu.frontenddevelopment.Fragment.ProjectOverviewFragment;
import nl.hu.frontenddevelopment.Model.Person;
import nl.hu.frontenddevelopment.R;
import nl.hu.frontenddevelopment.Utils.CircleTransform;

public class ProjectActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setProjectOverviewFragment();
    }

    public void setDetailProject(String projectId){
        View v = findViewById(R.id.contentFragment);
        String tag = v.getTag().toString();
        if(tag.equals("tablet")){
            getSupportFragmentManager().beginTransaction().replace(R.id.detailFragment, ActorOverviewFragment.newInstance(projectId)).addToBackStack(null).commit();
        }else {
            startActivity(new Intent(this, ActorActivity.class).putExtra("project_id", projectId));
        }
    }

    public void setProjectOverviewFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, ProjectOverviewFragment.newInstance()).addToBackStack(null).commit();
    }

    public boolean isTablet(){
        View v = findViewById(R.id.contentFragment);
        String tag = v.getTag().toString();

        if(tag.equals("tablet")){
                return true;
        }
        return false;
    }

    public void setEditProject(String id, String title, String description){
        View v = findViewById(R.id.contentFragment);
        String tag = v.getTag().toString();
        if(tag.equals("tablet")){
            getSupportFragmentManager().beginTransaction().replace(R.id.detailFragment, ProjectNewFragment.newInstance(id,title,description)).addToBackStack(null).commit();
        } else{
            getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, ProjectNewFragment.newInstance(id,title,description)).addToBackStack(null).commit();
        }
    }

    public void setNewProject(){
        View v = findViewById(R.id.contentFragment);
        String tag = v.getTag().toString();
        if(tag.equals("tablet")){
            getSupportFragmentManager().beginTransaction().replace(R.id.detailFragment, ProjectNewFragment.newInstance()).addToBackStack(null).commit();
        } else{
            getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, ProjectNewFragment.newInstance()).addToBackStack(null).commit();
        }
    }
}