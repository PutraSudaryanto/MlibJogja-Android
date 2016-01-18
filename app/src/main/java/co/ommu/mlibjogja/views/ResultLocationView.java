package co.ommu.mlibjogja.views;

import java.util.ArrayList;

import co.ommu.mlibjogja.R;
import co.ommu.mlibjogja.models.ResultLocationModel;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ResultLocationView extends BaseAdapter {

	LayoutInflater inflater;
	ArrayList<ResultLocationModel> array;

	public ResultLocationView(ArrayList<ResultLocationModel> array, Context context) {
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
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View vi = arg1;
		Holder holder;
		if (vi == null) {
			holder = new Holder();
			vi = inflater.inflate(R.layout.view_result_location, null);
			holder.tvPerpus = (TextView) vi.findViewById(R.id.tvPerpus);
			holder.tvAddress = (TextView) vi.findViewById(R.id.tvAddress);
			holder.tvEks = (TextView) vi.findViewById(R.id.tvExemplar);
			holder.linContain = (LinearLayout) vi.findViewById(R.id.linContainer);
			vi.setTag(holder);
		} else {
			holder = (Holder) vi.getTag();
		}
		holder.tvPerpus.setText(array.get(position).name);
		holder.tvAddress.setText(array.get(position).address);
		holder.tvEks.setText(array.get(position).book);
		if ((position % 2) == 0) {
			holder.linContain.setBackgroundColor(Color.parseColor("#f8f8f8"));
		} else {
			holder.linContain.setBackgroundColor(Color.parseColor("#ffffff"));
		}
		return vi;
	}

	static class Holder {
		TextView tvPerpus;
		TextView tvAddress;
		TextView tvEks;
		LinearLayout linContain;

	}

}
