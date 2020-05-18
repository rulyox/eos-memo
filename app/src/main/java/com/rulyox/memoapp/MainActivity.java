package com.rulyox.memoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.rulyox.memoapp.adapter.MemoAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Memo> memoList = new ArrayList<>();
    private MemoAdapter memoAdapter = new MemoAdapter(memoList);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUI();

        test();

    }

    private void setUI() {

        // tool bar
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        // recycler view
        RecyclerView recyclerView = findViewById(R.id.main_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        // recycler view adapter
        recyclerView.setAdapter(memoAdapter);
        memoAdapter.notifyDataSetChanged();

    }

    private void addMemo(int id, String title, String text, String date) {

        // add to list
        Memo newMemo = new Memo(id, title, text, date);
        memoList.add(newMemo);

        // update recycler view
        memoAdapter.notifyDataSetChanged();

    }

    private void test() {

        addMemo(1, "첫번째 메모", "안녕", "2020. 05. 17.");
        addMemo(2, "두번째 메모", "메모앱입니다.", "2020. 05. 17.");
        addMemo(3, "세번째 메모", "333", "2020. 05. 17.");

    }

}
