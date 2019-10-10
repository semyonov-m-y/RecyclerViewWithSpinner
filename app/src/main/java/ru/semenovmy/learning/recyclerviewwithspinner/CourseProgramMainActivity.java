package ru.semenovmy.learning.recyclerviewwithspinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import ru.semenovmy.learning.recyclerviewwithspinner.adapters.DisplayModeSpinnerAdapter;
import ru.semenovmy.learning.recyclerviewwithspinner.adapters.LectorSpinnerAdapter;
import ru.semenovmy.learning.recyclerviewwithspinner.adapters.DataAdapter;
import ru.semenovmy.learning.recyclerviewwithspinner.models.DisplayMode;
import ru.semenovmy.learning.recyclerviewwithspinner.models.Lecture;

public class CourseProgramMainActivity extends AppCompatActivity {

    private static final int POSITION_ALL = 0;

    private CourseProgramProvider mCourseProgramProvider = new CourseProgramProvider();
    private DataAdapter mDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewPreparing(savedInstanceState == null);
        initLectorsSpinner();
        initDisplayModeSpinner();
    }

    private void initLectorsSpinner() {
        Spinner spinner = findViewById(R.id.lectors_spinner);
        final List<String> spinnerItems = mCourseProgramProvider.providerLectors();
        Collections.sort(spinnerItems);
        spinnerItems.add(POSITION_ALL, getResources().getString(R.string.all));
        spinner.setAdapter(new LectorSpinnerAdapter(spinnerItems));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final List<Lecture> lectures = position == POSITION_ALL ?
                        mCourseProgramProvider.provideLectures() :
                        mCourseProgramProvider.filterBy(spinnerItems.get(position));
                mDataAdapter.setLectures(lectures);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initDisplayModeSpinner() {
        Spinner spinner = findViewById(R.id.display_mode_spinner);
        spinner.setAdapter(new DisplayModeSpinnerAdapter());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DisplayMode selectedDisplayMode = DisplayMode.values()[position];
                mDataAdapter.setDisplayMode(selectedDisplayMode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void recyclerViewPreparing(boolean isFirstCreate) {
        RecyclerView recyclerView = findViewById(R.id.learning_program_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mDataAdapter = new DataAdapter(getResources());
        mDataAdapter.setLectures(mCourseProgramProvider.provideLectures());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(mDataAdapter);
        if (isFirstCreate) {
            Lecture nextLecture = mCourseProgramProvider.getLectureNextTo(new Date());
            int positionOfNextLecture = mDataAdapter.getPositionOf(nextLecture);
            if (positionOfNextLecture != -1) {
                recyclerView.scrollToPosition(positionOfNextLecture);
            }
        }
    }
}
