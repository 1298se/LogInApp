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

public class UserProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Get user display name from Firebase
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();


        // Change textView to user display name
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.fragment_user_profile, container, false); //pass the correct layout name for the fragment

        TextView text = (TextView) view.findViewById(R.id.profileTextView);
        text.setText(user.getDisplayName());

        Button editUserProfileButton = (Button) view.findViewById(R.id.editUserProfileButton);
        editUserProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editProfile  = new Intent(getActivity(), EditUserProfile.class);
                startActivity(editProfile);
            }
        });

        return view;

    }
}
