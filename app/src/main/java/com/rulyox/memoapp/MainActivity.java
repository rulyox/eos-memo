package com.rulyox.memoapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rulyox.memoapp.adapter.MemoAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Memo> memoList = new ArrayList<>();
    private MemoAdapter memoAdapter = new MemoAdapter(this, memoList);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUI();
        loadMemo();

    }

    private void setUI() {

        // tool bar
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.main_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(1, -1);

            }
        });

        // recycler view
        RecyclerView recyclerView = findViewById(R.id.main_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // recycler view adapter
        recyclerView.setAdapter(memoAdapter);
        memoAdapter.notifyDataSetChanged();

    }

    private void loadMemo() {

        memoList.clear();

        // network thread
        new Thread() {
            @Override
            public void run() {

                try {

                    String getMemo = Requests.getMemo();

                    if(getMemo == null) return;

                    // parse json array
                    JSONArray jsonArray = new JSONArray(getMemo);

                    for(int i = 0; i < jsonArray.length(); i++) {

                        // parse json object
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        final int id = jsonObject.getInt("id");
                        final String title = jsonObject.getString("title");
                        final String text = jsonObject.getString("text");
                        final String date = jsonObject.getString("date");

                        // add to list
                        Memo newMemo = new Memo(id, title, text, date);
                        memoList.add(newMemo);

                    }

                    // ui thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // update recycler view
                            memoAdapter.notifyDataSetChanged();

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();

    }

    private void showDialog(int action, final int id) {

        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog, null);

        TextView nameTextView = view.findViewById(R.id.dialog_name);
        final EditText titleEditText = view.findViewById(R.id.dialog_title);
        final EditText textEditText = view.findViewById(R.id.dialog_text);
        Button okButton = view.findViewById(R.id.dialog_ok);
        Button cancelButton = view.findViewById(R.id.dialog_cancel);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(view);

        final AlertDialog dialog = builder.create();

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if(action == 1) { // add

            nameTextView.setText("Add Memo");

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String title = titleEditText.getText().toString();
                    String text = textEditText.getText().toString();
                    String date = new SimpleDateFormat("yyyy. MM. dd. HH:mm", Locale.getDefault()).format(new Date());

                    addMemo(title, text, date);

                    dialog.dismiss();

                }
            });

        } else if(action == 2) { // edit

            nameTextView.setText("Edit Memo");

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String title = titleEditText.getText().toString();
                    String text = textEditText.getText().toString();
                    String date = new SimpleDateFormat("yyyy. MM. dd. HH:mm", Locale.getDefault()).format(new Date());

                    editMemo(id, title, text, date);

                    dialog.dismiss();

                }
            });

        }

        dialog.show();

    }

    private void addMemo(final String title, final String text, final String date) {

        new Thread() {
            @Override
            public void run() {

                Requests.addMemo(title, text, date);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        loadMemo();

                    }
                });

            }
        }.start();

    }

    public void clickedMemo(int action, int id) {

        if(action == 0) deleteMemo(id);
        else if(action == 1) showDialog(2, id);

    }

    private void deleteMemo(final int id) {

        new Thread() {
            @Override
            public void run() {

                Requests.deleteMemo(id);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        loadMemo();

                    }
                });

            }
        }.start();

    }

    private void editMemo(final int id, final String title, final String text, final String date) {

        new Thread() {
            @Override
            public void run() {

                Requests.editMemo(id, title, text, date);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        loadMemo();

                    }
                });

            }
        }.start();

    }

}
