package com.jcodecraeer.demo;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.BaseViewHolder;
import com.jcodecraeer.xrecyclerview.DeleteRecyclerView;
import com.jcodecraeer.xrecyclerview.OnItemDelClickListener;
import com.jcodecraeer.xrecyclerview.R;

import java.util.ArrayList;


/**
 * Created by prj on 2016/10/25.
 */
public class DeleteRecyclerActivity extends AppCompatActivity {

    private DeleteRecyclerView recyclerView;
    //private RemoveRecyclerview recyclerView;
    private ArrayList<String> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delrecycler);

        recyclerView = (DeleteRecyclerView) findViewById(R.id.id_item_remove_recyclerview);

        mList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mList.add(i + "");
        }

        final MyAdapter adapter = new MyAdapter(this, mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setOnItemClickListener(new OnItemDelClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(DeleteRecyclerActivity.this, "** " + mList.get(position) + " **", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteClick(int position) {
                adapter.removeItem(position);
            }
        });
    }

    public class MyAdapter extends RecyclerView.Adapter {
        private Context mContext;
        private LayoutInflater mInflater;
        private ArrayList<String> mList;

        public MyAdapter(Context context, ArrayList<String> list) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
            mList = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new BaseViewHolder(mInflater.inflate(R.layout.del_recyclerview_item_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            BaseViewHolder viewHolder = (BaseViewHolder) holder;
            viewHolder.setText(R.id.item_content,mList.get(position));
        }

        @Override
        public int getItemCount() {
            return mList != null ? mList.size() : 0;
        }

        public void removeItem(int position) {
            mList.remove(position);
            notifyItemRemoved(position);
        }
    }
}