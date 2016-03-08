package ch.yannick.display.technical;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ch.yannick.context.R;

/**
 * Created by Yannick on 10.03.2015.
 */
public class EnumAdapter<T extends AdapterUsable> extends ArrayAdapter<T> {

    private int layout;
    public EnumAdapter(Context context, List<T> objects, int layout) {
        super(context, layout, objects);
        this.layout=layout;
    }

    public EnumAdapter(Context context, List<T> objects) {
        super(context, R.layout.row_dialog, objects);
        layout = R.layout.row_dialog;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(layout, parent, false);
        }
        TextView text = (TextView) convertView.findViewById(android.R.id.text1);
        text.setText(getItem(position).getStringId());
        return convertView;
    }
}
