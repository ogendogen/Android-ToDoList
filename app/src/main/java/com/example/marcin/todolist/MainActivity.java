package com.example.marcin.todolist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
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
    private TaskList mytasks = new TaskList();

    View.OnClickListener globalTextViewListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //String resourceName = view.getResources().getResourceName(view.getId());
            int taskID = view.getId();
            taskID -= 500;

            Intent i = new Intent(getApplicationContext(), TaskActivity.class);
            i.putExtra("isEdit", true);
            i.putExtra("indexToEdit", taskID);
            startActivity(i);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        scrollView = (ScrollView)findViewById(R.id.scroll);
        scrollView.removeAllViews();

        try
        {
            //TaskFileHandler.deleteFile(getApplicationContext());
            mytasks = TaskFileHandler.readAllTasks(getApplicationContext());
        }
        catch(Exception e)
        {
            Toast.makeText(this, "Błąd: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        try
        {
            int offset = 500;
            for (int i=0; i<mytasks.length(); i++)
            {
                TextView txt = new TextView(this);
                txt.setText(mytasks.get(i).getName());
                txt.setId(500 + i);
                txt.setLayoutParams(params);
                txt.setHeight(100);

                txt.setOnClickListener(globalTextViewListener);

                layout.addView(txt);
                layout.setId(R.id.mainLayout);
            }
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
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
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.Theme_AppCompat_Dialog_Alert);
            builder.setTitle("Po czym chcesz sortować ?");
            String[] items = {"Po dacie", "Po priorytecie", "Alfabetycznie", "Niealfabetycznie"};
            builder.setItems(items, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    try
                    {
                        switch(which)
                        {
                            case 0:
                            {
                                mytasks.sortTasks(TaskList.SortCriteria.DueDate);
                                reOrderTasks(mytasks);
                                break;
                            }
                            case 1:
                            {
                                mytasks.sortTasks(TaskList.SortCriteria.Priority);
                                reOrderTasks(mytasks);
                                break;
                            }
                            case 2:
                            {
                                mytasks.sortTasks(TaskList.SortCriteria.TaskNameAsc);
                                reOrderTasks(mytasks);
                                break;
                            }
                            case 3:
                            {
                                mytasks.sortTasks(TaskList.SortCriteria.TaskNameDesc);
                                reOrderTasks(mytasks);
                                break;
                            }
                        }
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Błąd: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
            builder.create();
            builder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void reOrderTasks(TaskList tasks)
    {
        LinearLayout layout = (LinearLayout)findViewById(R.id.mainLayout);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.VERTICAL);

        for (int i=0; i<layout.getChildCount(); i++)
        {
            TextView txt = (TextView)layout.getChildAt(i);
            txt.setOnClickListener(null);
        }

        layout.removeAllViews();

        for (int i=0; i<tasks.length(); i++)
        {
            TextView txt = new TextView(this);
            txt.setText(mytasks.get(i).getName());
            txt.setId(500 + i);
            txt.setLayoutParams(params);
            txt.setHeight(100);

            txt.setOnClickListener(globalTextViewListener);

            layout.addView(txt);
        }
    }
}
