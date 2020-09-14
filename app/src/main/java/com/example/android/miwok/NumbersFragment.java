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
public class NumbersFragment extends Fragment {
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
        View rootView = inflater.inflate(R.layout.word_list,container,false);

        maudioManager=(AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<word> numbers = new ArrayList<word>();
        numbers.add(new word("One", "lutti", R.drawable.number_one, R.raw.number_one));
        numbers.add(new word("Two", "otiiko", R.drawable.number_two, R.raw.number_two));
        numbers.add(new word("Three", "tolookosu", R.drawable.number_three, R.raw.number_three));
        numbers.add(new word("Four", "oyyisa", R.drawable.number_four, R.raw.number_four));
        numbers.add(new word("Five", "massokka", R.drawable.number_five, R.raw.number_five));
        numbers.add(new word("Six", "temmokka", R.drawable.number_six, R.raw.number_six));
        numbers.add(new word("Seven", "kenekaku", R.drawable.number_seven, R.raw.number_seven));
        numbers.add(new word("Eight", "kawinta", R.drawable.number_eight, R.raw.number_eight));
        numbers.add(new word("Nine", "wo'e", R.drawable.number_nine, R.raw.number_nine));
        numbers.add(new word("Ten", "na'aacha", R.drawable.number_ten, R.raw.number_ten));
        wordAdapter itemAdapter = new wordAdapter(getActivity(), numbers, R.color.category_numbers);
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
