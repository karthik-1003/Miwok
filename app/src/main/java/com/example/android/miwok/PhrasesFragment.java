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
public class PhrasesFragment extends Fragment {
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
        View rootView = inflater.inflate(R.layout.word_list,container,false);

        maudioManager=(AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<word> numbers = new ArrayList<word>();
        numbers.add(new word("Where are you going?", "minto wuksus"
                , R.raw.phrase_where_are_you_going));
        numbers.add(new word("What is your name?", "tinnә oyaase'nә",
                R.raw.phrase_what_is_your_name));
        numbers.add(new word("My name is...", "oyaaset...",
                R.raw.phrase_my_name_is));
        numbers.add(new word("How are you feeling?", "michәksәs?",
                R.raw.phrase_how_are_you_feeling));
        numbers.add(new word("I’m feeling good.", "kuchi achit",
                R.raw.phrase_im_feeling_good));
        numbers.add(new word("Are you coming?", "әәnәs'aa?",
                R.raw.phrase_are_you_coming));
        numbers.add(new word("Yes, I’m coming.", "hәә’ әәnәm",
                R.raw.phrase_yes_im_coming));
        numbers.add(new word("I’m coming.", "әәnәm",
                R.raw.phrase_im_coming));
        numbers.add(new word("Let’s go.", "yoowutis",
                R.raw.phrase_lets_go));
        numbers.add(new word("Come here.", "әnni'nem'",
                R.raw.phrase_come_here));
        wordAdapter itemAdapter = new wordAdapter(getActivity(), numbers,
                R.color.category_phrases);
        ListView listItemView = (ListView)rootView.findViewById(R.id.list);
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
