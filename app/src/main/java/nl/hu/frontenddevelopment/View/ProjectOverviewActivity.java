package nl.hu.frontenddevelopment.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import nl.hu.frontenddevelopment.Fragment.ActorOverviewFragment;
import nl.hu.frontenddevelopment.Fragment.ProjectOverviewFragment;
import nl.hu.frontenddevelopment.R;

public class ProjectOverviewActivity extends BaseActivity implements View.OnClickListener {
    // Firebase instance variables
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_overview);
        setOverviewFragment();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // User is signed out
                    startLoginChooserActivity();
                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.sign_out_button) {
            signOut();
        }
    }

    private void signOut() {
        mAuth.signOut();
    }

    protected void startLoginChooserActivity() {
        startActivity(new Intent(this, ChooserActivity.class));
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void setDetailProject(){
        View v = findViewById(R.id.contentFragment);
        String tag = v.getTag().toString();
        if(tag.equals("tablet")){
            getSupportFragmentManager().beginTransaction().replace(R.id.detailFragment, ActorOverviewFragment.newInstance()).commitAllowingStateLoss();
        }else {
            startActivity(new Intent(this, ActorOverviewActivity.class));
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