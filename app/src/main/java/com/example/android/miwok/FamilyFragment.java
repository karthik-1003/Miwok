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
public class FamilyFragment extends Fragment {
    private MediaPlayer player;
    private AudioManager maudioManager;

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
    private MediaPlayer.OnCompletionListener mcompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            releasePlayer();
        }
    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.word_list,container,false);


        maudioManager=(AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<word> numbers = new ArrayList<word>();
        numbers.add(new word("father", "әpә", R.drawable.family_father,
                R.raw.family_father));
        numbers.add(new word("mother", "әṭa", R.drawable.family_mother,
                R.raw.family_mother));
        numbers.add(new word("son", "angsi", R.drawable.family_son,
                R.raw.family_son));
        numbers.add(new word("daughter", "tune", R.drawable.family_daughter,
                R.raw.family_daughter));
        numbers.add(new word("older brother", "taachi", R.drawable.family_older_brother,
                R.raw.family_older_brother));
        numbers.add(new word("younger brother", "chalitti", R.drawable.family_younger_brother,
                R.raw.family_younger_brother));
        numbers.add(new word("older sister", "teṭe", R.drawable.family_older_sister,
                R.raw.family_older_sister));
        numbers.add(new word("younger sister", "kolliti", R.drawable.family_younger_sister,
                R.raw.family_younger_sister));
        numbers.add(new word("grandmother", "ama", R.drawable.family_grandmother,
                R.raw.family_grandmother));
        numbers.add(new word("grandfather", "paapa", R.drawable.family_grandfather,
                R.raw.family_grandfather));

        wordAdapter itemAdapter = new wordAdapter(getActivity(), numbers, R.color.category_family);
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
