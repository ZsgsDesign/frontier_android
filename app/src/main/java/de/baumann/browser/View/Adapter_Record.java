package xyz.johnzhang.frontier.View;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import xyz.johnzhang.frontier.Database.Record;
import xyz.johnzhang.frontier.Ninja.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class Adapter_Record extends ArrayAdapter<Record> {
    private final Context context;
    private final int layoutResId;
    private final List<Record> list;

    public Adapter_Record(Context context, List<Record> list) {
        super(context, R.layout.list_item, list);
        this.context = context;
        this.layoutResId = R.layout.list_item;
        this.list = list;
    }

    private static class Holder {
        TextView title;
        TextView time;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Holder holder;
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(layoutResId, parent, false);
            holder = new Holder();
            holder.title = view.findViewById(R.id.record_item_title);
            holder.time = view.findViewById(R.id.record_item_time);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        Record record = list.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd", Locale.getDefault());holder.title.setText(record.getTitle());
        holder.time.setText(sdf.format(record.getTime()));

        return view;
    }
}