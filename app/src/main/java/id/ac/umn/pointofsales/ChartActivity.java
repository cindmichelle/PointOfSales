package id.ac.umn.pointofsales;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


import javax.annotation.Nullable;

import cn.pedant.SweetAlert.SweetAlertDialog;



public class ChartActivity extends AppCompatActivity {


    ArrayList<String> orderIds = new ArrayList<String>();
    ArrayList<Record> orderDetail = new ArrayList<Record>();
    ArrayList<Record> lvData = new ArrayList<>();
    ArrayList<Product> productTotalQty = new ArrayList<>();
    ArrayList<Product> ord = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Set set;
    CollectionReference ordersFs = db.collection("orders");
    SweetAlertDialog pDialog;

    AnyChartView anyChartView;
    Record record;
    private RecordsAdapter recordsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));


        Date startDates = new Date(getIntent().getLongExtra("startDates", -1));
        Date endDates = new Date(getIntent().getLongExtra("endDates", -1));




        ordersFs.whereLessThanOrEqualTo("date", endDates).whereGreaterThan("date", startDates).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        String orderId;
                        orderId = document.getId();
                        orderIds.add(orderId);
                    }
                    if(orderIds.size() == 0){
                        makeChart();
                    }

                }

                for (int i = 0; i < orderIds.size(); i++) {
                    final int z = orderIds.size();


                    db.collection("orders").document(orderIds.get(i)).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot document, @Nullable FirebaseFirestoreException e) {
                            ArrayList<HashMap<String, Object>> order = (ArrayList<HashMap<String, Object>>) document.get("orderDetails");
                            ord.clear();
                            int total = 0;
                            for (int j = 0; j < order.size(); j++) {

                                String name = order.get(j).get("name").toString();
                                String qtys = order.get(j).get("qty").toString();
                                int qty = Integer.parseInt(qtys);
                                String prices = order.get(j).get("price").toString();
                                int price = Integer.parseInt(prices);
                                String imageUrl = order.get(j).get("imageUrl").toString();
                                Product orderData = new Product(name, imageUrl, price, qty);


                                Log.d(this.getClass().toString(), "sales1 iterasij : " + j);

                                ord.add(orderData);

                                int size;
                                if (productTotalQty.size() == 0) {
                                    productTotalQty.add(orderData);
                                } else {
                                    size = productTotalQty.size();
                                    for (int k = 0; k < size; k++) {
                                        if (orderData.getName().equals(productTotalQty.get(k).getName())) {
                                            Log.d(this.getClass().toString(), "sales1 masuk jugafak");

                                            int totalQty;


                                            totalQty = orderData.getQty() + productTotalQty.get(k).getQty();
                                            productTotalQty.get(k).setQty(totalQty);
                                            Log.d(this.getClass().toString(), "sales1 masuk qty k " + k + " : " + productTotalQty.get(k).getQty());
                                            break;
                                        } else if ((k == (size - 1)) && !(orderData.getName().equals(productTotalQty.get(k).getName()))) {
                                            Log.d(this.getClass().toString(), "sales1 nyemplung uy size : " + size);

                                            productTotalQty.add(orderData);
                                            break;
                                        }
                                    }
                                }
                                Log.d(this.getClass().toString(), "sales1 productTotalQty size : " + productTotalQty.size());
                                for (int i = 0; i < productTotalQty.size(); i++) {
                                    Log.d(this.getClass().toString(), "sales1 productTotalQty name" + i + " : " + productTotalQty.get(i).getName());
                                    Log.d(this.getClass().toString(), "sales1 productTotalQty qty" + i + " : " + productTotalQty.get(i).getQty());
                                }
                            }

                            for(int i = 0; i < ord.size(); i++){
                                total = total + ord.get(i).getQty() * ord.get(i).getPrice();
                            }
//                            Date date, int total, String id
                             record = new Record(document.getDate("date"), total, document.getId());
                            total = 0;
                            orderDetail.add(record);
                            Log.d(this.getClass().toString(), "sales1 orderDetail ID : " + orderDetail.get(0).getId());
                            Log.d(this.getClass().toString(), "sales1 orderDetail size : " + orderDetail.size());
                            makeChart();
                        }
                    });


                }


            }
        });
    }

    void makeChart() {
        recordsAdapter = new RecordsAdapter(ChartActivity.this, R.layout.record  ,orderDetail);
        final ListView recordsView = findViewById(R.id.records_view);
        recordsView.setAdapter(recordsAdapter);
        recordsView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Record lvDatas;
                lvDatas = (Record) recordsView.getItemAtPosition(position);
                Intent intent = new Intent (ChartActivity.this, DetailsReportActivity.class);
                intent.putExtra("lvID", lvDatas.getId());
                intent.putExtra("lvDate", lvDatas.getDate().toString());
                startActivity(intent);
//                Toast.makeText(this,"You selected : " + item,Toast.LENGTH_SHORT).show();
            }
        });
        if(productTotalQty.size() == 0){
            Log.d(this.getClass().toString(), "kosong gan");

            new  SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Oops...")
                    .setContentText("Data tidak ditemukan!")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            finish();
                        }
                    })
                    .show();
        }
        else{
            List<DataEntry> data = new ArrayList<>();
            Cartesian cartesian = AnyChart.column();
            data.clear();

            for (int i = 0; i < productTotalQty.size(); i++) {
                data.add(new ValueDataEntry(productTotalQty.get(i).getName(), productTotalQty.get(i).getQty()));
            }

            set = Set.instantiate();
            set.data(data);


            Column column = cartesian.column(data);

            column.fill("function() {" +
                    "            if (this.index % 3 == 0)" +
                    "                return '#ffcb2c';" +
                    "            else if (this.index % 3 == 1)" +
                    "                return '#f5820b';" +
                    "            else if (this.index % 3 == 2)" +
                    "                return '#fea614';" +
                    "        }");

            column.tooltip()
                    .titleFormat("{%X}")
                    .position(Position.CENTER_BOTTOM)
                    .anchor(Anchor.CENTER_BOTTOM)
                    .offsetX(0d)
                    .offsetY(5d)
                    .format("{%Value}{groupsSeparator: }")
                    .fontColor("#FFFFFF");

            cartesian.animation(true);
            cartesian.title("Menu Ordered");

            cartesian.yScale().minimum(0d);

            cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

            cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
            cartesian.interactivity().hoverMode(HoverMode.BY_X);

            cartesian.xAxis(0).title("Menu");
            cartesian.yAxis(0).title("Quantity");

            anyChartView.setChart(cartesian);
//            btnChart.setVisibility(View.VISIBLE);
        }

    }

//    class CreateChart extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            return null;
//        }
//
//
//        protected void onPostExecute(Void result) {
//
//            Log.d(this.getClass().toString(), "sales1 orderDetail sizea : " + orderDetail.size());
//
//            Cartesian cartesian = AnyChart.column();
//
//            List<DataEntry> data = new ArrayList<>();
//            Log.d(this.getClass().toString(), "sales1 size gan : " + productTotalQty.size());
//
//            for (int i = 0; i < productTotalQty.size(); i++) {
//                Log.d(this.getClass().toString(), "sales1 size gan : " + productTotalQty.size());
//                data.add(new ValueDataEntry(productTotalQty.get(i).getName(), productTotalQty.get(i).getQty()));
//            }
//
//            set = Set.instantiate();
//            set.data(data);
//
//
//            Column column = cartesian.column(data);
//
//            column.normal().fill("#ffc526", 1);
//
//            column.tooltip()
//                    .titleFormat("{%X}")
//                    .position(Position.CENTER_BOTTOM)
//                    .anchor(Anchor.CENTER_BOTTOM)
//                    .offsetX(0d)
//                    .offsetY(5d)
//                    .format("{%Value}{groupsSeparator: }")
//                    .fontColor("#000000");
//
//            cartesian.animation(true);
//            cartesian.title("Top 10 Ordered Menu");
//
//            cartesian.yScale().minimum(0d);
//
//            cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");
//
//            cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
//            cartesian.interactivity().hoverMode(HoverMode.BY_X);
//
//            cartesian.xAxis(0).title("Menu");
//            cartesian.yAxis(0).title("Quantity");
//
//            anyChartView.setChart(cartesian);
//
//        }
//
//
//    }

}
