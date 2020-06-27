package com.rulyox.memoapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rulyox.memoapp.MainActivity;
import com.rulyox.memoapp.Memo;
import com.rulyox.memoapp.R;

import java.util.ArrayList;

public class MemoAdapter extends RecyclerView.Adapter<MemoViewHolder> {

    private Context context;
    private ArrayList<Memo> memoList;

    public MemoAdapter(Context context, ArrayList<Memo> memoList) {
        this.context = context;
        this.memoList = memoList;
    }

    @NonNull @Override
    public MemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new MemoViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MemoViewHolder holder, int position) {

        final Memo memo = memoList.get(position);

        holder.title.setText(memo.getTitle());
        holder.text.setText(memo.getText());
        holder.date.setText(memo.getDate());

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final CharSequence[] items = {"Delete", "Edit"};

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        MainActivity mainActivity = (MainActivity) context;
                        mainActivity.clickedMemo(which, memo.getId());

                    }
                });

                alertDialogBuilder.create().show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return memoList.size();
    }

}
