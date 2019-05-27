package id.ac.umn.pointofsales;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class DetailsAdapter extends ArrayAdapter<Product> {
    private Context recordContext;
    private  int rowLayout;


    public DetailsAdapter(@NonNull Context context, int resource, @NonNull List<Product> objects) {
        super(context, resource, objects);
        this.rowLayout = resource;
        this.recordContext = context;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup viewGroup) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(recordContext);
            v = inflater.inflate(rowLayout, null);
        }

        TextView textName = v.findViewById(R.id.record_name);
        TextView textPrice = v.findViewById(R.id.record_price);
        TextView textQty = v.findViewById(R.id.record_qty);

        Product record = getItem(position);

        if(record != null){
            textName.setText(record.getName());
            textPrice.setText(String.format(Locale.US, "Rp %,d.00", record.getPrice()));
            textQty.setText(String.format(Locale.US, "%,d.00", record.getQty()));
        }

        return v;
    }
}
