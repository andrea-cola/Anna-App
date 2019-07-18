package com.cola.vita;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class Timer extends Fragment {

    private String[] materie = {"Biologia", "Fisica", "Chimica", "Logica", "Matematica"};

    private TextView minutes, seconds, steps;
    private Button reset, start, fenomena;
    private CountDownTimer c;
    private int s, pass, orginalPass;
    private MediaPlayer mp;

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
                    }

                    public void onFinish() {
                        mp.start();
                        update();
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
                update();
            }

        };

        reset();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.start();
            }
        });

        fenomena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });

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

    private void update() {
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
