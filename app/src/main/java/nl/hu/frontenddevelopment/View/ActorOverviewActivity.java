package nl.hu.frontenddevelopment.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import nl.hu.frontenddevelopment.Fragment.ActorOverviewFragment;
import nl.hu.frontenddevelopment.Model.Actor;
import nl.hu.frontenddevelopment.R;

public class ActorOverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor_overview);
        setOverviewFragment();
    }

    public void setOverviewFragment(){
        getSupportFragmentManager().beginTransaction().replace(R.id.contentFragment, ActorOverviewFragment.newInstance()).commitAllowingStateLoss();
    }
}
