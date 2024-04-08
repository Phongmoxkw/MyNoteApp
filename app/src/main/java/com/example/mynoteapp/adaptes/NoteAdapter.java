package com.example.mynoteapp.adaptes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynoteapp.R;
import com.example.mynoteapp.objects.Note;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder>{
    private ArrayList<Note> arrayList;
    private Context context;
    private ClickListeners clickListeners;

    public NoteAdapter(Context context, ArrayList<Note> arrayList, ClickListeners clickListeners) {
        this.arrayList = arrayList;
        this.context = context;
        this.clickListeners = clickListeners;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_note,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Note note = arrayList.get(position);
        holder.title.setText(note.get_title());
        holder.content.setText(note.get_content());
    }


    @Override
    public int getItemCount() {
        if(arrayList!=null && arrayList.size()>0)
            return arrayList.size();
        else
            return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView title, content;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {
            clickListeners.onItemClick(getAdapterPosition(),v);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListeners.onItemLongClick(getAdapterPosition(),v);
            return true;
        }
    }
    public interface ClickListeners{
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }
}
