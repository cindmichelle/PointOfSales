package id.ac.umn.pointofsales;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Nullable;

public class DetailsReportActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference ordersFs = db.collection("orders");
    ArrayList<Product> orderDetail = new ArrayList<>();
    private DetailsAdapter recordsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_report);
        String id = getIntent().getStringExtra("lvID");
        String date = getIntent().getStringExtra("lvDate");

        TextView textId = findViewById(R.id.detailOrder_id);
        TextView textDate = findViewById(R.id.detailOrder_date);

        textId.setText(id);
        textDate.setText(date);
        db.collection("orders").document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                ArrayList<HashMap<String, Object>> order = (ArrayList<HashMap<String, Object>>) documentSnapshot.get("orderDetails");

                for (int j = 0; j < order.size(); j++) {

                    String name = order.get(j).get("name").toString();
                    String qtys = order.get(j).get("qty").toString();
                    int qty = Integer.parseInt(qtys);
                    String prices = order.get(j).get("price").toString();
                    int price = Integer.parseInt(prices);

                    Product orderData = new Product(name, "", price, qty);

                    orderDetail.add(orderData);

                    Log.d(this.getClass().toString(), "sales1 iterasij : " + j);

                }
                recordsAdapter = new DetailsAdapter(DetailsReportActivity.this, R.layout.details_order  ,orderDetail);
                final ListView recordsView = findViewById(R.id.detailOrder_view);
                recordsView.setAdapter(recordsAdapter);
            }
        });
    }
}
