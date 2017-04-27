package no.hit.jon.pietabell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;

public class PieAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Pie> mPies;
    private LayoutInflater mInflater;
    public PieAdapter(Context c, ArrayList<Pie> pies) {
        mContext = c;
        mPies = pies;
        mInflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE);

    }
    public int getCount() {
        return  mPies.size();
    }
    public Object getItem(int position) {
        return mPies.get(position);
    }
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.pie_view_item, null);
            viewHolder = new ViewHolder();
            viewHolder.textViewName = (TextView)
                    convertView.findViewById(R.id.textViewName);
            viewHolder.textViewDescription = (TextView)
                    convertView.findViewById(R.id.textViewDescription);
            viewHolder.textViewPrice = (TextView)
                    convertView.findViewById(R.id.textViewPrice);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Pie currentPie = mPies.get(position);
        viewHolder.textViewName.setText(currentPie.mName);
        viewHolder.textViewDescription.setText(currentPie.mDescription);
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String price = formatter.format(currentPie.mPrice);
        viewHolder.textViewPrice.setText(price);
        return convertView;
    }

    private static class ViewHolder {
        public TextView textViewName;
        public TextView textViewDescription;
        public TextView textViewPrice;
    }
}