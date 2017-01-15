package com.example.uberv.vugraph2;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apigee.sdk.data.client.callbacks.ApiResponseCallback;
import com.apigee.sdk.data.client.entities.User;
import com.apigee.sdk.data.client.response.ApiResponse;
import com.example.uberv.vugraph2.api.ApiBAAS;
import com.example.uberv.vugraph2.utils.PreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {


    private static final String LOG_TAG = RegisterActivity.class.getSimpleName();
    private TransitionManager mTransitionManager;
    private Scene mRegisterScene;
    private Scene mLoginScene;
    private Scene mCurrentScene;
    private boolean isRegisterScreen=false;

    private ApiBAAS mApiBAAS;

    @BindView(R.id.root_layout)
    ViewGroup rootLayout;
    @BindView(R.id.detail_container)
    ViewGroup detailContainer;
    @BindView(R.id.errorTv)
    TextView errorTv;
    @BindView(R.id.fullnameEt)
    EditText fullnameEt;
    @BindView(R.id.emailEt)
    EditText emailEt;
    @BindView(R.id.usernameEt)
    EditText usernameEt;
    @BindView(R.id.passwordEt)
    EditText passwordEt;
    @BindView(R.id.btnLoginRegister)
    Button loginRegisterButton;
    @BindView(R.id.btnLinkToLoginRegisterScreen)
    Button linkToLoginRegisterScreenButton;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ButterKnife.bind(this);

        mApiBAAS=ApiBAAS.getInstance(this);

        setupUI();
    }



    @OnClick(R.id.btnLinkToLoginRegisterScreen)
    public void onTransitionButtonClicked(View view){
        TransitionSet set = new TransitionSet();
        set.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);

        Scale scale = new Scale();
        scale.setDuration(150);
//        scale.addTarget(fullnameEt);

        // FIXME первый запуск работает коряво
        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(150);
//        changeBounds.excludeTarget(fullnameEt,true);
//        Fade fade = new Fade();
//        fade.setDuration(150);
//        set.addTransition(fade);

        if(!isRegisterScreen) {
            isRegisterScreen=true;
            set.addTransition(changeBounds);
            set.addTransition(scale);
        }else{
            isRegisterScreen=false;
            set.addTransition(scale);
            set.addTransition(changeBounds);
        }

        set.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                Log.d(LOG_TAG,"change screen transition "+transition.hashCode());
            }

            @Override
            public void onTransitionEnd(Transition transition) {

            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
        TransitionManager.beginDelayedTransition(detailContainer,set);

        setupUI();
    }

    private View.OnClickListener onRegisterButtonClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            disableUI(true);
            // Register user
            Toast.makeText(RegisterActivity.this, "Registering", Toast.LENGTH_SHORT).show();

            String fullname = fullnameEt.getText().toString().trim();
            String username = usernameEt.getText().toString().trim();
            String email = emailEt.getText().toString().trim();
            String password = passwordEt.getText().toString().trim();

            // TODO validate data


            hideError();
            mApiBAAS.getApigeeDataClient().createUserAsync(username, fullname, email, password, new ApiResponseCallback() {
                @Override
                public void onResponse(ApiResponse apiResponse) {
                    if(!apiResponse.completedSuccessfully()){
                        // error
                        String err=apiResponse.getErrorDescription();
                        String error = apiResponse.getError();
                        switch(error){
                            case "duplicate_unique_property_exists":
                                // FIXME не показывает текст повторно, если ошибка была спрятана по переходу на другой скрин
                                showError("Username or Email is already taken!");
                                errorTv.setVisibility(View.VISIBLE);
                                break;
                        }
                        Toast.makeText(RegisterActivity.this, err, Toast.LENGTH_SHORT).show();
                    }else{
                        // registered succesfully
                        Snackbar snackbar = Snackbar.make(rootLayout,"Registered Succefully!",Snackbar.LENGTH_LONG);
                        snackbar.getView().setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        snackbar.setActionTextColor(getResources().getColor(R.color.white));
                        snackbar.show();

                        // navigate to login screen
                        isRegisterScreen=false;
                        setupUI();
                    }
                    disableUI(false);
                }

                @Override
                public void onException(Exception e) {
                    Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    disableUI(false);
                }
            });
        }
    };

    private View.OnClickListener onLoginButtonClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Register user
            Toast.makeText(RegisterActivity.this, "Logging in", Toast.LENGTH_SHORT).show();
            disableUI(true);
            hideError();

            String email = emailEt.getText().toString().trim();
            String password = passwordEt.getText().toString().trim();

            // TODO validate data

            mApiBAAS.getApigeeDataClient().authorizeAppUserAsync(email, password, new ApiResponseCallback() {
                @Override
                public void onResponse(ApiResponse apiResponse) {
                    if(!apiResponse.completedSuccessfully()){
                        String error = apiResponse.getError();
                        switch(error){
                            case "invalid_grant":
                                // FIXME не показывает текст повторно, если ошибка была спрятана по переходу на другой скрин
                                showError("Invalid email or password");
                                break;
                            default :
                                showError("Could not log You in");
                        }
                        Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
//                        showError(error);
                    }else{
                        // save user data
                        Toast.makeText(RegisterActivity.this, "Logged in!", Toast.LENGTH_SHORT).show();
                        // save user data
                        VuGraphUser currentUser = new VuGraphUser(apiResponse.getUser());
                        currentUser.setAccessToken(apiResponse.getAccessToken());
                        String rawResponse = apiResponse.getRawResponse();
                        long expiresAt=-1;
                        try {
                            JSONObject responseJson = new JSONObject(rawResponse);
                            expiresAt=responseJson.getLong("expires_in")*1000+System.currentTimeMillis();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        currentUser.setExpiresAt(expiresAt-60000);
                        //currentUser.setExpiresAt(apiResponse);
                        PreferencesUtils.setCurrentUser(currentUser);
                        VuGraphUser testUser = PreferencesUtils.getCurrentUser();
                        Toast.makeText(RegisterActivity.this, testUser.toString(), Toast.LENGTH_SHORT).show();
                        disableUI(false);

                    }
                    disableUI(false);
                }

                @Override
                public void onException(Exception e) {
                    Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    disableUI(false);
                }
            });
        }
    };

    /** Depending on the isRegisterScreen change labels and click listeners to according screen*/
    private void setupUI(){
        // TODO refactor
        if(isRegisterScreen){
            // register screen
            fullnameEt.setVisibility(View.VISIBLE);
            usernameEt.setVisibility(View.VISIBLE);
            loginRegisterButton.setText(getResources().getString(R.string.btn_register));
            loginRegisterButton.setOnClickListener(onRegisterButtonClickListener);
            linkToLoginRegisterScreenButton.setText(getResources().getString(R.string.btn_link_to_login));
        }else{
            // login screen
            fullnameEt.setVisibility(View.GONE);
            usernameEt.setVisibility(View.GONE);
            loginRegisterButton.setText(getResources().getString(R.string.btn_login));
            loginRegisterButton.setOnClickListener(onLoginButtonClickListener);
            linkToLoginRegisterScreenButton.setText(getResources().getString(R.string.btn_link_to_register));
        }
        hideError();
    }

    private void disableUI(boolean disable){
        loginRegisterButton.setEnabled(!disable);
        linkToLoginRegisterScreenButton.setEnabled(!disable);
        mProgressBar.setVisibility(disable?View.VISIBLE:View.INVISIBLE);
    }

    private void showError(String error){
        TransitionSet set = new TransitionSet();
        set.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);
        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(150);
        set.addTransition(changeBounds);
        Fade fade = new Fade();
        fade.setDuration(150);
        set.addTransition(fade);

        set.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                Log.d(LOG_TAG,"showError transition "+(errorTv.getVisibility()==View.VISIBLE));
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                Log.d(LOG_TAG,"showError transition end"+(errorTv.getVisibility()==View.VISIBLE));
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });

        TransitionManager.beginDelayedTransition(detailContainer,set);

        errorTv.setVisibility(View.VISIBLE);
        errorTv.setText(error);
    }

    private void hideError(){
        TransitionSet set = new TransitionSet();
        Fade fade = new Fade();
        fade.setDuration(150);
        set.addTransition(fade);
        set.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);
        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(150);
        set.addTransition(changeBounds);
        set.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                Log.d(LOG_TAG,"hideError transition");
            }

            @Override
            public void onTransitionEnd(Transition transition) {

            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });

        TransitionManager.beginDelayedTransition(detailContainer,set);
        errorTv.setText("");
        errorTv.setVisibility(View.GONE);
    }


}
