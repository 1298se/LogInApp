package song.tang.edu.loginapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfileFragment extends Fragment {
    private Button editUserProfileButton, signOutButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Get user display name from Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();


        // Change textView to user display name
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.fragment_user_profile, container, false); //pass the correct layout name for the fragment

        TextView text = (TextView) view.findViewById(R.id.profileTextView);
        text.setText(user.getDisplayName());

        editUserProfileButton = (Button) view.findViewById(R.id.editUserProfileButton);
        editUserProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editProfile  = new Intent(getActivity(), EditUserProfile.class);
                startActivity(editProfile);
            }
        });

        // Sign Out button
        signOutButton = (Button) view.findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                getActivity().finish();
                Intent signOut = new Intent(getActivity().getBaseContext(), MainActivity.class);
                signOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(signOut);

            }
        });
        // Add profile picture Firebase Database
        return view;

    }
}
