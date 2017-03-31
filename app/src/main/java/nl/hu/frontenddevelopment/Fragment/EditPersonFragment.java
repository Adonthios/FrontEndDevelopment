package nl.hu.frontenddevelopment.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.hu.frontenddevelopment.Model.Actor;
import nl.hu.frontenddevelopment.Model.ActorPerson;
import nl.hu.frontenddevelopment.Model.Person;
import nl.hu.frontenddevelopment.Model.Project;
import nl.hu.frontenddevelopment.R;
import nl.hu.frontenddevelopment.Utils.CircleTransform;
import nl.hu.frontenddevelopment.View.GoogleSignInActivity;
import nl.hu.frontenddevelopment.View.ProjectActivity;

public class EditPersonFragment extends Fragment {
    private static final int REQUEST_IMAGE_CAPTURE = 111;
    private EditText name, function, phonenumber, sidenotes;
    public Button bSavePerson;
    private ImageView profilePicture;
    private DatabaseReference mDatabase;

    public EditPersonFragment() {
        // Required empty public constructor
    }

 //   public static EditPersonFragment newInstance(String personID, String name, String function, String phonenumber, String sidenotes) {
    public static EditPersonFragment newInstance(String personID, String name, String phonenumber, String sidenotes, String profilePhote) {
        EditPersonFragment fragment = new EditPersonFragment();
        Bundle args = new Bundle();
        args.putString("person_id", personID);
        args.putString("person_name", name);
     //   args.putString("person_function", function);
        args.putString("person_phonenumber", phonenumber);
        args.putString("person_sidenotes", sidenotes);
        args.putString("profile_picture", profilePhote);
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
        profilePicture = (ImageView) rootView.findViewById(R.id.person_icon);
        Glide.with(getActivity()).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).crossFade().thumbnail(0.3f).bitmapTransform(new CircleTransform(getActivity())).diskCacheStrategy(DiskCacheStrategy.ALL).into(profilePicture);
        Log.d("URL", FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
        profilePicture.setOnClickListener(e -> onLaunchCamera());
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

    @Override
    public void onResume(){
        super.onResume();

    }

    public void onLaunchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            saveProfilePicture(imageBitmap);
        }
    }

    public void saveProfilePicture(Bitmap bitmap) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference profilePictures = storageRef.child("profilePictures/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = profilePictures.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Uri downloadUrl = taskSnapshot.getMetadata().getDownloadUrl();
                Glide.with(getActivity()).load(downloadUrl).crossFade().thumbnail(0.3f).bitmapTransform(new CircleTransform(getActivity())).diskCacheStrategy(DiskCacheStrategy.ALL).into(profilePicture);
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setPhotoUri(downloadUrl)
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    mDatabase.child("persons").addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                            Person person = dataSnapshot.getValue(Person.class);
                                            Log.d("Person key: ", person.key);
                                            Log.d("Current user key", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                            if(person.key != null && person.key.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                                Log.d("Snapshot key", dataSnapshot.getKey());
                                                mDatabase.child("persons").child(dataSnapshot.getKey()).child("profilePhoto").setValue(downloadUrl.toString());
                                            }
                                        }

                                        @Override
                                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                        }

                                        @Override
                                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                                        }

                                        @Override
                                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                        });
            }

    });

    }
}