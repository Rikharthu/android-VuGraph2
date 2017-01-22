package com.example.uberv.vugraph2.fragments;

import android.content.Context;
import android.media.Rating;
import android.net.Network;
import android.support.transition.AutoTransition;
import android.support.transition.TransitionManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.uberv.vugraph2.AugmentedExperience;
import com.example.uberv.vugraph2.PhotoGettingSingleton;
import com.example.uberv.vugraph2.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link RecyclerView.Adapter} that can display a {@link AugmentedExperience} and makes a call to the
 * specified {@link AugmentedRealityFragment.OnARFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ARExperienceRVAdapter extends RecyclerView.Adapter<ARExperienceRVAdapter.ViewHolder> {

    private final List<AugmentedExperience> values;
    private final AugmentedRealityFragment.OnARFragmentInteractionListener mListener;
    private final Context context;
    private PhotoGettingSingleton photoGetting;

    public ARExperienceRVAdapter(List<AugmentedExperience> items, AugmentedRealityFragment.OnARFragmentInteractionListener listener,Context context) {
        values = items;
        mListener = listener;
        this.context=context;
        photoGetting=PhotoGettingSingleton.getInstance(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_preview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        AugmentedExperience item = values.get(position);
        holder.item=item;

        holder.headingTv.setText(item.getHeading());
        holder.subheadingTv.setText(item.getSubheading());
        holder.descriptionTv.setText(item.getDescription());
        holder.ratingBar.setRating(item.getVuforiaRating());
        // FIXME make cloudinary an instance variable in singleton
        String imageUrl = PhotoGettingSingleton.getCloudinary().url().generate(item.getImageName()+".png");
        // TODO check if not null
        holder.imageNiv.setImageUrl(imageUrl,photoGetting.getImageLoader());

        holder.startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View root;
        @BindView(R.id.heading)
        public TextView headingTv;
        @BindView(R.id.subheading)
        public TextView subheadingTv;
        @BindView(R.id.description)
        public TextView descriptionTv;
        @BindView(R.id.imageView)
        public NetworkImageView imageNiv;
        @BindView(R.id.rating)
        public RatingBar ratingBar;
        @BindView(R.id.start_preview_button)
        public Button startBtn;

        public AugmentedExperience item;


        public ViewHolder(View view) {
            super(view);
            root = view;
            ButterKnife.bind(this, view);
            AutoTransition autoTransition = new AutoTransition();
            TransitionManager.beginDelayedTransition((ViewGroup)root,autoTransition);
        }
    }
}
