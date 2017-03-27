package nl.hu.frontenddevelopment.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import nl.hu.frontenddevelopment.R;
import nl.hu.frontenddevelopment.View.GoogleSignInActivity;
import nl.hu.frontenddevelopment.View.ProjectActivity;

public class EditPersonFragment extends Fragment {
    private EditText name, function, phonenumber, sidenotes;
    public Button bSavePerson;
    private DatabaseReference mDatabase;

    public EditPersonFragment() {
        // Required empty public constructor
    }

 //   public static EditPersonFragment newInstance(String personID, String name, String function, String phonenumber, String sidenotes) {
    public static EditPersonFragment newInstance(String personID, String name, String phonenumber, String sidenotes) {
        EditPersonFragment fragment = new EditPersonFragment();
        Bundle args = new Bundle();
        args.putString("person_id", personID);
        args.putString("person_name", name);
     //   args.putString("person_function", function);
        args.putString("person_phonenumber", phonenumber);
        args.putString("person_sidenotes", sidenotes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_edit_person, container, false);
        name = (EditText) rootView.findViewById(R.id.person_name);
    //    function = (EditText) rootView.findViewById(R.id.person_function);
        phonenumber = (EditText) rootView.findViewById(R.id.person_phonenumber);
        sidenotes = (EditText) rootView.findViewById(R.id.person_sidenotes);
        bSavePerson = (Button) rootView.findViewById(R.id.button_save_person);

        name.setText(getArguments().getString("person_name"));
    //    function.setText(getArguments().getString("person_function"));
        phonenumber.setText(getArguments().getString("person_phonenumber"));
        sidenotes.setText(getArguments().getString("person_sidenotes"));
        bSavePerson.setText(R.string.button_save);
        bSavePerson.setOnClickListener(b -> savePerson(getArguments().getString("person_id")));

        return rootView;
    }

    private void savePerson(String key){
        if(key != null && !key.equals("")) {
            mDatabase.child("persons").child(key).child("name").setValue(((EditText) getView().findViewById(R.id.person_name)).getText().toString());
            mDatabase.child("persons").child(key).child("phonenumber").setValue(((EditText) getView().findViewById(R.id.person_phonenumber)).getText().toString());
            mDatabase.child("persons").child(key).child("sidenote").setValue(((EditText) getView().findViewById(R.id.person_sidenotes)).getText().toString());
            Toast.makeText(getActivity(), R.string.toast_change_succesful, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), R.string.toast_change_failed, Toast.LENGTH_SHORT).show();
        }
        refreshFragment();
    }

    private void refreshFragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(ProjectOverviewFragment.newInstance()).commit();

        this.getFragmentManager().beginTransaction()
            .replace(R.id.contentFragment, ProjectOverviewFragment.newInstance())
            .addToBackStack(null)
            .commit();
    }
}