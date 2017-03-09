package nl.hu.frontenddevelopment.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import nl.hu.frontenddevelopment.Fragment.ProjectDetailFragment;
import nl.hu.frontenddevelopment.Fragment.ProjectOverviewFragment;
import nl.hu.frontenddevelopment.R;

public class ProjectOverviewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_overview);
        setOverviewFragment();
    }

    public void setDetailProject(){
        View v = findViewById(R.id.contentFragment);
        String tag = v.getTag().toString();
        if(tag.equals("tablet")){
            getSupportFragmentManager().beginTransaction().replace(R.id.detailFragment, ProjectDetailFragment.newInstance()).commitAllowingStateLoss();
        }else {
            startActivity(new Intent(this, ProjectDetailActivity.class));
        }
    }
    public void setOverviewFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, ProjectOverviewFragment.newInstance()).commitAllowingStateLoss();
    }
    public void editProject(String title, String description, String key){
        startActivity(new Intent(this,ProjectNewActivity.class).putExtra("project_title", title)
                .putExtra("project_description", description).putExtra("project_key", key));
    }
}