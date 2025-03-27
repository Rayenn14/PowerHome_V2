package com.example.powerhome_v2;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import java.util.ArrayList;
import java.util.List;

public class CalendarActivity extends BaseActivity {

    private List<ImageButton> dayButtons = new ArrayList<>();
    private int selectedDayId = R.id.jour1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityContent(R.layout.activity_calendar);

        setupDayButtons();
        setInitialSelection();
    }

    private void setupDayButtons() {

        dayButtons.add(findViewById(R.id.jour1));
        dayButtons.add(findViewById(R.id.jour2));
        dayButtons.add(findViewById(R.id.jour3));
        dayButtons.add(findViewById(R.id.jour4));
        dayButtons.add(findViewById(R.id.jour5));
        dayButtons.add(findViewById(R.id.jour6));
        dayButtons.add(findViewById(R.id.jour7));


        for (ImageButton button : dayButtons) {
            button.setOnClickListener(this::handleDaySelection);
        }
    }

    private void setInitialSelection() {

        findViewById(selectedDayId).setBackgroundResource(R.drawable.btn1);
    }

    private void handleDaySelection(View view) {

        for (ImageButton button : dayButtons) {
            button.setBackgroundResource(R.drawable.btn2);
        }


        view.setBackgroundResource(R.drawable.btn1);
        selectedDayId = view.getId();
    }
}