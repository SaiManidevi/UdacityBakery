package com.example.android.udacitybakery.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.udacitybakery.R;
import com.example.android.udacitybakery.databinding.FragmentDetailStepBinding;
import com.example.android.udacitybakery.model.Bakery;
import com.example.android.udacitybakery.utilities.BakeryConstants;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import static com.example.android.udacitybakery.utilities.BakeryConstants.KEY_CURRENT_STEP;
import static com.example.android.udacitybakery.utilities.BakeryConstants.STATE_PLAYBACK_POSITION;
import static com.example.android.udacitybakery.utilities.BakeryConstants.STATE_STEP_DESC;
import static com.example.android.udacitybakery.utilities.BakeryConstants.STATE_STEP_URL;

public class DetailStepFragment extends Fragment {
    private FragmentDetailStepBinding binding;
    private String mCurrentStepDescription;
    private String mCurrentStepVideoUrl;
    private Bakery.StepsList mCurrentRecipeStep;
    private SimpleExoPlayer mExoPlayer;
    private long mPlaybackPosition = 0;

    // Define a new interface OnButtonClickListener that triggers a callback in the host activity
    OnButtonClickListener mCallback;

    public DetailStepFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnButtonClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnButtonClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_step, container, false);
        View rootView = binding.getRoot();
        if (savedInstanceState == null) {
            mCurrentStepDescription = mCurrentRecipeStep.getDescription();
            mCurrentStepVideoUrl = mCurrentRecipeStep.getVideoURL();
        }
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onNextButtonClicked(mCurrentRecipeStep);
            }
        });
        binding.btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onPreviousButtonClicked(mCurrentRecipeStep);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mPlaybackPosition = savedInstanceState.getLong(STATE_PLAYBACK_POSITION, 0);
            mCurrentStepDescription = savedInstanceState.getString(STATE_STEP_DESC);
            mCurrentStepVideoUrl = savedInstanceState.getString(STATE_STEP_URL);
            mCurrentRecipeStep = (Bakery.StepsList) savedInstanceState.getSerializable(KEY_CURRENT_STEP);
        }
    }

    public void setCurrentRecipeStep(Bakery.StepsList currentRecipeStep) {
        mCurrentRecipeStep = currentRecipeStep;
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.tvStepDescription.setText(mCurrentStepDescription);
        if (!mCurrentStepVideoUrl.isEmpty()) {
            binding.ivNoVideoImage.setVisibility(View.GONE);
            setVisibilityOfExoPlayer();
            initializePlayer(Uri.parse(mCurrentStepVideoUrl));
        } else {
            binding.exoPlayerView.setVisibility(View.GONE);
            setVisibilityOfImageView();
            binding.ivNoVideoImage.setImageResource(R.drawable.no_video_image);
        }
    }

    /**
     * Initialize ExoPlayer.
     *
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

            binding.exoPlayerView.setPlayer(mExoPlayer);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "UdacityBakery");
            ExtractorMediaSource mediaSource = new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(
                    getContext(), userAgent, bandwidthMeter))
                    .createMediaSource(mediaUri);
            mExoPlayer.prepare(mediaSource);
            if (mPlaybackPosition != 0)
                mExoPlayer.seekTo(mPlaybackPosition);
            mExoPlayer.setPlayWhenReady(true);
            setVisibilityOfExoPlayer();
        }
    }

    //Release the ExoPlayer only when there is a proper
    // Video Url for the current displaying step

    @Override
    public void onPause() {
        super.onPause();
        if (!mCurrentStepVideoUrl.isEmpty() && mExoPlayer != null) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!mCurrentStepVideoUrl.isEmpty() && mExoPlayer != null) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!mCurrentStepVideoUrl.isEmpty() && mExoPlayer != null) {
            releasePlayer();
        }
    }

    /**
     * Before releasing the ExoPlayer, get the current position
     * of the video being played
     */
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mPlaybackPosition = mExoPlayer.getCurrentPosition();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    public void setVisibilityOfExoPlayer() {
        if (View.GONE == binding.exoPlayerView.getVisibility()) {
            binding.exoPlayerView.setVisibility(View.VISIBLE);
        }
    }

    public void setVisibilityOfImageView() {
        if (View.GONE == binding.ivNoVideoImage.getVisibility()) {
            binding.exoPlayerView.setVisibility(View.VISIBLE);
        }
    }

    // OnButtonClickListener interface, calls a method in the host activity named onRecipeStepSelected
    public interface OnButtonClickListener {
        void onNextButtonClicked(Bakery.StepsList clickedRecipeStep);

        void onPreviousButtonClicked(Bakery.StepsList clickedRecipeStep);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_STEP_DESC, mCurrentStepDescription);
        outState.putString(STATE_STEP_URL, mCurrentStepVideoUrl);
        outState.putLong(STATE_PLAYBACK_POSITION, mPlaybackPosition);
        outState.putSerializable(KEY_CURRENT_STEP, mCurrentRecipeStep);
    }
}
