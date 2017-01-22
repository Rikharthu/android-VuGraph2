package com.example.uberv.vugraph2.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.cloudinary.Transformation;
import com.example.uberv.vugraph2.PhotoGettingSingleton;
import com.example.uberv.vugraph2.R;
import com.example.uberv.vugraph2.VuGraphUser;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class UserSettingsFragment extends android.support.v4.app.Fragment {

    public interface OnUserSettingsFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public static final String TITLE="Settings";
    public static final String LOG_TAG=UserSettingsFragment.class.getSimpleName();

    private static final String ARG_USER = "ARG_USER";

    private VuGraphUser user;
    // UI
    @BindView(R.id.fragment_user_settings_change_avatar_btn)
    Button changeAvatarBtn;
    @BindView(R.id.fragment_user_settings_change_email_btn)
    Button changeEmailBtn;
    @BindView(R.id.fragment_user_settings_fullname_tv)
    TextView fullnameTv;
    @BindView(R.id.fragment_user_settings_role_tv)
    TextView roleTv;
    @BindView(R.id.fragment_user_settings_joined_tv)
    TextView joinedTv;
    @BindView(R.id.fragment_user_settings_email_tv)
    TextView emailTv;
    @BindView(R.id.fragment_user_settings_avatar_niv)
    NetworkImageView avatarNiv;

    private OnUserSettingsFragmentInteractionListener mListener;

    public UserSettingsFragment() {
        // Required empty public constructor
    }


    public static UserSettingsFragment newInstance(VuGraphUser user) {
        UserSettingsFragment fragment = new UserSettingsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_USER,user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user=getArguments().getParcelable(ARG_USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootLayout = inflater.inflate(R.layout.fragment_user_settings, container, false);

        ButterKnife.bind(this,rootLayout);

        fullnameTv.setText(user.getName());
        roleTv.setText(user.getRole());
        // TODO replace to use apigee data
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("d/M/y");
        joinedTv.setText(dateFormat.format(date));
        emailTv.setText(user.getEmail());

        String avatarUrl = PhotoGettingSingleton.getCloudinary().url().transformation(new Transformation()
                .width(240).height(360).crop("crop")).generate(user.getAvatarUrl()+".png");
        avatarNiv.setImageUrl(avatarUrl,PhotoGettingSingleton.getInstance(getContext()).getImageLoader());

        return rootLayout;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUserSettingsFragmentInteractionListener) {
            mListener = (OnUserSettingsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnUserSettingsFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick(R.id.fragment_user_settings_change_avatar_btn)
    void onChangeAvatar(View view){
        Toast.makeText(getContext(), "Not implemented yet ;)", Toast.LENGTH_SHORT).show();
        Log.d(LOG_TAG,"changing avatar");
    }

    @OnClick(R.id.fragment_user_settings_change_email_btn)
    void onChangeEmail(View view){
        Toast.makeText(getContext(), "Not implemented yet ;)", Toast.LENGTH_SHORT).show();
        //ApiBAAS.getInstance(getContext()).getApigeeDataClient().updateEntity()
    }
}
