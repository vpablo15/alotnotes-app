package com.hedge.alotnotes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.hedge.alotnotes.R;
import com.hedge.alotnotes.models.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class NoteAdapter extends BaseAdapter {

    private Context context;
    private List<Note> list;
    private int layout;

    public NoteAdapter(Context context, List<Note> list, int layout) {
        this.context = context;
        this.list = list;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Note getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layout, null);
            vh = new ViewHolder();
            vh.description = (TextView) convertView.findViewById(R.id.textViewNoteDescription);
            vh.date = (TextView) convertView.findViewById(R.id.textViewNoteDate);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder)convertView.getTag();
        }

        Note note = list.get(position);

        vh.description.setText(note.getDescription());
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String finalDate = df.format(note.getCreatedAt());
        vh.date.setText(finalDate);


        return convertView;
    }

    public class ViewHolder {
        TextView description;
        TextView date;
    }
}
