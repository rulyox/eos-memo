package com.rulyox.memoapp;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rulyox.memoapp.adapter.MemoAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Memo> memoList = new ArrayList<>();
    private MemoAdapter memoAdapter = new MemoAdapter(memoList);

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

                addMemo();

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

    private void add(int id, String title, String text, String date) {

        // add to list
        Memo newMemo = new Memo(id, title, text, date);
        memoList.add(newMemo);

        // update recycler view
        memoAdapter.notifyDataSetChanged();

    }

    private void refresh() {

        memoList.clear();

        loadMemo();

    }

    private void loadMemo() {

        // network thread
        new Thread() {
            @Override
            public void run() {

                try {

                    String getMemo = null;

                    // get memo from server
                    while(getMemo == null) { // prevent get fail

                        getMemo = Requests.getMemo();

                    }

                    // parse json array
                    JSONArray jsonArray = new JSONArray(getMemo);

                    for(int i = 0; i < jsonArray.length(); i++) {

                        // parse json object
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        final int id = jsonObject.getInt("id");
                        final String title = jsonObject.getString("title");
                        final String text = jsonObject.getString("text");
                        final String date = jsonObject.getString("date");

                        // ui thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                add(id, title, text, date);

                            }
                        });

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }.start();

    }

    private void addMemo() {

        new Thread() {
            @Override
            public void run() {

                Requests.addMemo("자동 추가", (memoList.size() + 1) + "", "2020");

                // ui thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        refresh();

                    }
                });

            }
        }.start();

    }

}
