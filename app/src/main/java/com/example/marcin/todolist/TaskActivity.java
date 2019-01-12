package com.example.marcin.todolist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.Date;

public class TaskActivity extends AppCompatActivity {

    EditText taskName, taskDesc, taskDate, taskTime;
    SeekBar priority;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        taskName = (EditText)findViewById(R.id.editText);
        taskDesc = (EditText)findViewById(R.id.editText3);
        taskDate = (EditText)findViewById(R.id.editText4);
        taskTime = (EditText)findViewById(R.id.editText5);

        priority = (SeekBar)findViewById(R.id.seekBar);
        priority.setMax(5);
        priority.setProgress(3);
        priority.incrementProgressBy(1);

        btn = (Button)findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    String s_TaskName = taskName.getText().toString();
                    String s_taskDesc = taskDesc.getText().toString();
                    String dueDate = taskDate.getText().toString();
                    String dueTime = taskTime.getText().toString();
                    int i_priority = priority.getProgress();
                }
                catch(Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Błąd: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
