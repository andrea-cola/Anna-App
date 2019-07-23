package com.cola.vita;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class DBInterface extends AsyncTask {

    private final int offset = 1600;

    @Override
    protected Object doInBackground(Object[] objects) {

        int op = (int)objects[0];
        VitaDatabase db = (VitaDatabase) objects[1];

        if(op == 0) { // read
            List<Timing> list = db.timingDAO().getAll();

            View view = (View) objects[2];
            ((TextView)view.findViewById(R.id.best_time)).setText(bestTime(list));
            ((TextView)view.findViewById(R.id.quiz_number)).setText(String.valueOf(list.size() + offset));
            ((TextView)view.findViewById(R.id.quiz_skipped)).setText(countSkipped(list));

            ProgressBar progressBar = view.findViewById(R.id.progressBar);
            progressBar.setProgress( (list.size()+offset) / 300000 * 100 + 2);
        } else { // write
            db.timingDAO().insert((Timing) objects[2]);
        }

        return null;
    }

    private String countSkipped(List<Timing> list){
        return String.valueOf(list.parallelStream()
                .filter(t -> t.skipped)
                .collect(toList()).size());
    }

    private String bestTime(List<Timing> list){
        if(list.size() == 0)
            return "--:--";
        list.sort((p1, p2) -> Long.compare(p1.time, p2.time));
        return secToTime(list.get(0).time);
    }

    private String secToTime(long t){
        return (t / 60 == 0 ? "00" : t / 60) + ":" + (t%60 < 10 ? "0" + t%60 : t%60);
    }

}
