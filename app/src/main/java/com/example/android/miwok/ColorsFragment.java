package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ColorsFragment extends Fragment {

    private MediaPlayer player;
    private AudioManager maudioManager;

    private MediaPlayer.OnCompletionListener mcompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releasePlayer();
        }
    };

    AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {
                    if(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                            focusChange== AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){
                        player.pause();
                        player.seekTo(0);
                    }else if(focusChange==AudioManager.AUDIOFOCUS_GAIN){
                        player.start();
                    }else if(focusChange==AudioManager.AUDIOFOCUS_LOSS){
                        releasePlayer();
                    }
                }
            };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.word_list,container,false);

        maudioManager=(AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<word> numbers = new ArrayList<word>();
        numbers.add(new word("red", "weṭeṭṭi", R.drawable.color_red,
                R.raw.color_red));
        numbers.add(new word("green", "chokokki", R.drawable.color_green,
                R.raw.color_green));
        numbers.add(new word("brown", "ṭakaakki", R.drawable.color_brown,
                R.raw.color_brown));
        numbers.add(new word("gray", "ṭopoppi", R.drawable.color_gray,
                R.raw.color_gray));
        numbers.add(new word("black", "kululli", R.drawable.color_black,
                R.raw.color_black));
        numbers.add(new word("white", "kelelli", R.drawable.color_white,
                R.raw.color_white));
        numbers.add(new word("dusty yellow", "ṭopiisә", R.drawable.color_dusty_yellow,
                R.raw.color_dusty_yellow));
        numbers.add(new word("mustard yellow", "chiwiiṭә", R.drawable.color_mustard_yellow,
                R.raw.color_mustard_yellow));


        wordAdapter itemAdapter = new wordAdapter(getActivity(), numbers, R.color.category_colors);
        ListView listItemView = (ListView) rootView.findViewById(R.id.list);
        listItemView.setAdapter(itemAdapter);

        listItemView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                word word = numbers.get(position);
                releasePlayer();

                int result = maudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    player = MediaPlayer.create(getActivity(), word.getMaudio());
                    player.start();
                    player.setOnCompletionListener(mcompletionListener);
                }
            }
        });
        return rootView;
    }


    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    private void releasePlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (player != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            player.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            player = null;
            maudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}
