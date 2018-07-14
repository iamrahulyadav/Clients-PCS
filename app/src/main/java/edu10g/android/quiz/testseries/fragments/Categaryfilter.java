package edu10g.android.quiz.testseries.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu10g.android.quiz.testseries.R;

/**
 * Created by Vikram on 12/30/2017.
 */

public class Categaryfilter extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.categarylayout,
                container, false);

        return view;
    }
}
