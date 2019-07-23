package com.cola.vita;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Timer extends Fragment {

    private String[] materie = {"Biologia", "Fisica", "Chimica", "Logica", "Matematica"};

    private TextView minutes, seconds, steps;
    private Button reset, start, fenomena;
    private CountDownTimer c;
    private int s, pass, orginalPass;
    private MediaPlayer mp;
    private DBInterface dbInterface;
    private long remainingTime = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.timer_layout, container, false);
        minutes = view.findViewById(R.id.minutes);
        seconds = view.findViewById(R.id.seconds);
        steps = view.findViewById(R.id.steps);
        reset = view.findViewById(R.id.reset);
        start = view.findViewById(R.id.start);
        fenomena = view.findViewById(R.id.fenomena);

        dbInterface = new DBInterface();

        Spinner spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, materie);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                c.cancel();
                if(materie[i].equalsIgnoreCase("Biologia")){
                    s = 60;
                } else {
                    s = 90;
                }
                c = new CountDownTimer(s * 1000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        minutes.setText(getMinutes(millisUntilFinished));
                        seconds.setText(getSeconds(millisUntilFinished));
                        remainingTime = s -  millisUntilFinished / 1000;
                    }

                    public void onFinish() {
                        mp.start();
                        update(false);
                    }

                };
                reset();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                s = 90;
            }
        });

        mp = MediaPlayer.create(getContext(), R.raw.unconvinced);

        pass = 30;
        s = 90;

        orginalPass = pass;

        c = new CountDownTimer(s * 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                minutes.setText(getMinutes(millisUntilFinished));
                seconds.setText(getSeconds(millisUntilFinished));
            }

            public void onFinish() {
                mp.start();
                update(false);
            }

        };

        reset();

        start.setOnClickListener(view1 -> c.start());

        fenomena.setOnClickListener(view12 -> update(true));

        reset.setOnClickListener(view13 -> reset());

        return view;
    }

    private void reset(){
        c.cancel();
        pass = orginalPass;
        steps.setText(orginalPass + " steps remaining");
        minutes.setText(getMinutes(s * 1000));
        seconds.setText(getSeconds(s * 1000));
    }

    private String getMinutes(long m){
        String tmp = String.valueOf(m / 1000 / 60);
        if(tmp.length() < 2)
            tmp = "0" + tmp;
        return tmp;
    }

    private String getSeconds(long m){
        long diff = m/1000 - ((m / 1000 / 60) * 60);
        String tmp = String.valueOf(diff);
        if(tmp.length() < 2)
            tmp = "0" + tmp;
        return tmp;
    }

    private void update(boolean flag) {
        Timing timing = new Timing();
        timing.id_set = 0;
        timing.time = remainingTime;
        timing.skipped = flag;
        new DBInterface().execute(1, ((MainActivity)getActivity()).getDb(), timing);

        c.cancel();
        c.start();
        if (pass > 0){
            pass--;
            steps.setText(pass + " steps remaining");
        } else{
            reset();
        }
    }
}
