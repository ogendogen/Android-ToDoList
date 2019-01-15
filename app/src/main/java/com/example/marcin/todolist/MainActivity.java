package com.example.marcin.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private ScrollView scrollView;
    private TaskList tasks = new TaskList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        scrollView = (ScrollView)findViewById(R.id.scroll);
        scrollView.removeAllViews();

        int taskID = getIntent().getIntExtra("taskID", -2);
        if (taskID == -1) // dodawanie
        {
            Intent i = getIntent();
            String s_TaskName = i.getStringExtra("taskName");
            String s_TaskDesc = i.getStringExtra("taskDesc");
            int i_priority = i.getIntExtra("priority", 2);
            Date dueDate = new Date();
            dueDate.setTime(i.getLongExtra("dueDateTime", -1));

            try
            {
                tasks.addTask(s_TaskName, s_TaskDesc, i_priority, dueDate);
            }
            catch(Exception e)
            {
                Toast.makeText(getApplicationContext(), "Błąd: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        else if (taskID > -1) // edycja
        {

        }

        ArrayList<Task> mytasks = tasks.getTasks();
        //(LinearLayout)scrollView.getChildAt(0); // this line crashes app


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        for (int i=0; i<mytasks.size(); i++)
        {
            TextView txt = new TextView(this);
            txt.setText(mytasks.get(i).getName());
            txt.setId(i);
            txt.setLayoutParams(params);

            layout.addView(txt);
        }
        scrollView.addView(layout);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TaskActivity.class);
                intent.putExtra("isEdit", false);
                startActivity(intent);
                //Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
