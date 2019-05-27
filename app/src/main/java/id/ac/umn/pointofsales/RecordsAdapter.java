package id.ac.umn.pointofsales;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecordsAdapter extends ArrayAdapter<Record>{
    private Context recordContext;
    private  int rowLayout;

    int arr;

    public RecordsAdapter(@NonNull Context context, int resource, @NonNull List<Record> objects) {
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

        TextView textId = v.findViewById(R.id.record_id);
        TextView textDate = v.findViewById(R.id.record_date);
        TextView textTotal = v.findViewById(R.id.record_total);

        Record record = getItem(position);

        if(record != null){
            textId.setText(record.getId());
            textDate.setText(record.getDate().toString());
            textTotal.setText(String.format(Locale.US, "Rp %,d.00", record.getTotal()));
        }

        return v;
    }
}
