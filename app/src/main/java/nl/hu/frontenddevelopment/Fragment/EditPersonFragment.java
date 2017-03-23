package nl.hu.frontenddevelopment.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import nl.hu.frontenddevelopment.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditPersonFragment extends Fragment {


    public EditPersonFragment() {
        // Required empty public constructor
    }

    public static EditPersonFragment newInstance() {
        EditPersonFragment fragment = new EditPersonFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_person, container, false);
    }

}
