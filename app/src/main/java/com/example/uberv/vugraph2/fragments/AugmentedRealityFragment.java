package com.example.uberv.vugraph2.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apigee.sdk.data.client.callbacks.ApiResponseCallback;
import com.apigee.sdk.data.client.entities.Entity;
import com.apigee.sdk.data.client.response.ApiResponse;
import com.example.uberv.vugraph2.AugmentedExperience;
import com.example.uberv.vugraph2.R;
import com.example.uberv.vugraph2.api.ApiBAAS;

import java.util.ArrayList;
import java.util.List;


public class AugmentedRealityFragment extends Fragment {
    public static final String LOG_TAG=AugmentedRealityFragment.class.getSimpleName();

    /** Used by activity to show fragment's title on it's toolbar */
    public static final String TITLE = "Augmented Reality";
    private static final String KEY_DATA="KEY_DATA";

    private List<AugmentedExperience> data;
    private OnARFragmentInteractionListener mListener;


    public AugmentedRealityFragment() {
    }

    @SuppressWarnings("unused")
    public static AugmentedRealityFragment newInstance(ArrayList<AugmentedExperience> data) {
        AugmentedRealityFragment fragment = new AugmentedRealityFragment();
        Bundle args = new Bundle();
        // TODO add args
        args.putParcelableArrayList(KEY_DATA,data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            // retrieve arguments
            data=getArguments().getParcelableArrayList(KEY_DATA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_augmented_reality, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            recyclerView.setAdapter(new ARExperienceRVAdapter(data, mListener,getContext()));
        }

        ApiBAAS.getInstance(getContext()).getApigeeDataClient().getEntitiesAsync("experience", "", new ApiResponseCallback() {
            @Override
            public void onResponse(ApiResponse apiResponse) {
                if(apiResponse!=null) {
                    List<Entity> entities = apiResponse.getEntities();
                    // TODO map entitites to experiences
                    Log.d(LOG_TAG, entities.toString());
                }
            }

            @Override
            public void onException(Exception e) {

            }
        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnARFragmentInteractionListener) {
            mListener = (OnARFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnARFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnARFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(AugmentedExperience experience);
    }
}
