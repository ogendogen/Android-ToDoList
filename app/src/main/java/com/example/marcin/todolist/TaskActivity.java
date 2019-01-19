package com.example.marcin.todolist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class TaskActivity extends AppCompatActivity {

    private EditText taskName, taskDesc, taskDate, taskTime;
    private SeekBar priority;
    private Button btn;
    private Boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        isEdit = getIntent().getBooleanExtra("isEdit", false);

        taskName = (EditText)findViewById(R.id.editText);
        taskDesc = (EditText)findViewById(R.id.editText3);
        taskDate = (EditText)findViewById(R.id.editText4);
        taskTime = (EditText)findViewById(R.id.editText5);

        priority = (SeekBar)findViewById(R.id.seekBar);
        priority.setMax(4);
        priority.setProgress(2);
        priority.incrementProgressBy(1);

        btn = (Button)findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    String s_TaskName = taskName.getText().toString();
                    String s_taskDesc = taskDesc.getText().toString();

                    String s_dueDate = taskDate.getText().toString();
                    String s_dueTime = taskTime.getText().toString();
                    String s_dueDateTime = s_dueDate + " " + s_dueTime;

                    SimpleDateFormat pattern = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.US);
                    Date dueDateTime = pattern.parse(s_dueDateTime);

                    int i_priority = priority.getProgress();

                    if (s_TaskName.isEmpty() || s_taskDesc.isEmpty() || s_dueDate.isEmpty()) throw new Exception("Jedno z pól jest puste!");
                    if (i_priority < 0 || i_priority > 4) throw new Exception("Niepoprawny priorytet!");
                    if (s_TaskName.length() > 20) throw new Exception("Za długa nazwa! (max. 20 znaków)");
                    if (s_taskDesc.length() > 128) throw new Exception("Za długi opis! (max. 128 znaków)");
                    if (dueDateTime.before(new Date())) throw new Exception("Wybrany czas już minął!");

                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    /*i.putExtra("taskID", -1); // -1 -> dodawanie
                    i.putExtra("taskName", s_TaskName);
                    i.putExtra("taskDesc", s_taskDesc);
                    i.putExtra("priority", i_priority);
                    i.putExtra("dueDateTime", dueDateTime.getTime());*/
                    TaskFileHandler.writeTask(getApplicationContext(), new Task(s_TaskName, s_taskDesc, i_priority, dueDateTime));
                    startActivity(i);

                    Toast.makeText(getApplicationContext(), "Zadanie dodane", Toast.LENGTH_LONG).show();
                }
                catch(ParseException e)
                {
                    Toast.makeText(getApplicationContext(), "Niepoprawny format daty/czasu!", Toast.LENGTH_LONG).show();
                }
                catch(Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Błąd: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
