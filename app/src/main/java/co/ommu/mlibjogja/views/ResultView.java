package co.ommu.mlibjogja.views;

import java.util.ArrayList;

import co.ommu.mlibjogja.R;
import co.ommu.mlibjogja.models.ResultModel;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ResultView extends BaseAdapter {

    LayoutInflater inflater;
    ArrayList<ResultModel> array;

    public ResultView(ArrayList<ResultModel> array, Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.array = array;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return array.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int position, View view, ViewGroup arg2) {
        // TODO Auto-generated method stub
        View vi = view;
        Holder holder;
        if (vi == null) {
            holder = new Holder();
            vi = inflater.inflate(R.layout.view_result, null);
            holder.tvTitle = (TextView) vi.findViewById(R.id.tvTitle);
            holder.tvAuthor = (TextView) vi.findViewById(R.id.tvAuthor);
            holder.tvPublisher = (TextView) vi.findViewById(R.id.tvPublisher);
            holder.tvLocation = (TextView) vi.findViewById(R.id.tv_total_position);
            holder.linBg = (LinearLayout) vi.findViewById(R.id.linBg);
            vi.setTag(holder);
        } else {
            holder = (Holder) vi.getTag();
        }

        holder.tvTitle.setText(array.get(position).title);
        holder.tvAuthor.setText(array.get(position).author);
        holder.tvPublisher.setText(array.get(position).publisher);
        holder.tvLocation.setText(array.get(position).location);

        if ((position % 2) == 0) {
            holder.linBg.setBackgroundColor(Color.parseColor("#f8f8f8"));
        } else {
            holder.linBg.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        return vi;
    }

    static class Holder {
        TextView tvTitle;
        TextView tvAuthor;
        TextView tvPublisher;
        TextView tvLocation;
        LinearLayout linBg;
    }

}
