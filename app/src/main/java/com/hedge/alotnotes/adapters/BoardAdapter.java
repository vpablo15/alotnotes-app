package com.hedge.alotnotes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.hedge.alotnotes.R;
import com.hedge.alotnotes.models.Board;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class BoardAdapter extends BaseAdapter {

    private Context context;
    private List<Board> list;
    private int layout;

    public BoardAdapter(Context context, List<Board> boards, int layout) {
        this.context = context;
        this.list = boards;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Board getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh;

        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(layout,null);
            vh = new ViewHolder();
            vh.title = (TextView) convertView.findViewById(R.id.textViewBoardTitle);
            vh.notes = (TextView)convertView.findViewById(R.id.textViewBoardNotes);

            convertView.setTag(vh);

        }else{
            vh = (ViewHolder)convertView.getTag();
        }

        Board board = list.get(position);

        vh.title.setText(board.getTitle());
        int totalNotes = board.getNotes().size();
        String textForNotes = (totalNotes == 1) ? totalNotes + " Note" : totalNotes + " Notes";
        vh.notes.setText(textForNotes);

        return convertView;
    }

    public class ViewHolder{
        TextView title;
        TextView notes;
    }

}
