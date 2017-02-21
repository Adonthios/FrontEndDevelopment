package nl.hu.frontenddevelopment.View;

import android.os.Bundle;
import android.view.View;

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

    public void setDetailFragment(){
        View v = findViewById(R.id.contentFragment);
        String tag = v.getTag().toString();
        if(tag.equals("tablet")){
            getSupportFragmentManager().beginTransaction(). addToBackStack(null).replace(R.id.detailFragment, ProjectDetailFragment.newInstance()).commitAllowingStateLoss();
        }else {
            getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.contentFragment, ProjectDetailFragment.newInstance()).commitAllowingStateLoss();
        }
    }
    public void setOverviewFragment(){
        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.contentFragment, ProjectOverviewFragment.newInstance()).commitAllowingStateLoss();
    }
}