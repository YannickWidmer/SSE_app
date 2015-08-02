package ch.yannick.technical;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ch.yannick.context.R;

/**
 * Created by Yannick on 10.05.2015.
 */
public class AdapterColored<T>  extends ArrayAdapter<ColoredHolder<T>> {

    private int layout;
    private static String LOG = "Colorde Adapter";
    private static class ViewHolder {
        private TextView itemView;
    }

    public AdapterColored(Context context, List<ColoredHolder<T>> objects, int layout) {
        super(context, layout, objects);
        this.layout=layout;
    }

    public AdapterColored(Context context, List<ColoredHolder<T>> objects) {
        super(context, R.layout.row_dialog, objects);
        layout = R.layout.row_dialog;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(layout, parent, false);
        }
        TextView text = (TextView) convertView.findViewById(android.R.id.text1);
        ColoredHolder<T> item = getItem(position);
        if(item.hasText())
            text.setText(item.getString());
        else
            text.setText(item.getStringId());
        text.setTextColor(getContext().getResources().getColor(item.getColor()));
        return convertView;
    }
}
