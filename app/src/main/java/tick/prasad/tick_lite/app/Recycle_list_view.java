package tick.prasad.tick_lite.app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import tick.prasad.tick_lite.R;

public class Recycle_list_view extends BaseAdapter {

    private Context context;
    private List<MoreDetailsGS> list;

    public Recycle_list_view() {
    }

    public Recycle_list_view(Context context, List<MoreDetailsGS> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = View.inflate(context, R.layout.activity_recycle_list_view, null);

        TextView tvPlace, tvPrice;
        tvPlace = v.findViewById(R.id.textPlace);
        tvPrice = v.findViewById(R.id.textPrice);

        tvPlace.setText(list.get(position).getPlace());
        tvPrice.setText(list.get(position).getPrice());
        v.setId(position);


        return v;
    }
}
