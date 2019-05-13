package id.ac.umn.pointofsales;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


import javax.annotation.Nullable;

import cn.pedant.SweetAlert.SweetAlertDialog;



public class ChartActivity extends AppCompatActivity {


    ArrayList<String> orderIds = new ArrayList<String>();
    ArrayList<Product> orderDetail = new ArrayList<Product>();
    ArrayList<Product> productTotalQty = new ArrayList<>();
    ArrayList<Product> ord = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Set set;
    CollectionReference ordersFs = db.collection("orders");

    AnyChartView anyChartView;
    int test = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        Intent intent = getIntent();
        Date startDates = new Date(getIntent().getLongExtra("startDates", - 1));
        Date endDates = new Date(getIntent().getLongExtra("endDates", - 1));


        ordersFs.whereLessThanOrEqualTo("date",endDates).whereGreaterThan("date", startDates).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    int i = 0;
                    for(DocumentSnapshot document : task.getResult()){
                        String orderId;
                        orderId = document.getId();
                        orderIds.add(orderId);
//                        Log.d(this.getClass().toString(),"sales1 product Id : "  + orderIds.get(i));
                        i++;
                    }
                }
//                Log.d(this.getClass().toString(),"sales1 product size : "  + orderIds.size());


                for(int i = 0; i < orderIds.size(); i++){
                    final int z = orderIds.size();
                    db.collection("orders").document(orderIds.get(i)).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot document, @Nullable FirebaseFirestoreException e) {
                            ArrayList<HashMap<String, Object>> order = (ArrayList<HashMap<String, Object>>) document.get("orderDetails");


//                            Log.d(this.getClass().toString(),"sales1 order size: "  + order.get(0).get("name"));


                            for(int j = 0; j < order.size(); j++){

                                String name = order.get(j).get("name").toString();
                                String qtys = order.get(j).get("qty").toString();
                                int qty = Integer.parseInt(qtys);
                                String prices = order.get(j).get("price").toString();
                                int price = Integer.parseInt(prices);
                                String imageUrl = order.get(j).get("imageUrl").toString();
                                Product orderData = new Product(name, imageUrl, price, qty);

                                //orderDetail.add(orderData);

                                Log.d(this.getClass().toString(),"sales1 iterasij : "  + j);

                                Log.d(this.getClass().toString(),"sales1 orderData name : "  + orderData.getName());


                                int size;
                                if(productTotalQty.size() == 0){
                                    productTotalQty.add(orderData);
                                }
                                else{
                                    size = productTotalQty.size();
                                    for(int k = 0; k < size; k++){
                                        if(orderData.getName().equals(productTotalQty.get(k).getName())){
                                            Log.d(this.getClass().toString(),"sales1 masuk jugafak");

                                            int totalQty;


                                            totalQty = orderData.getQty() + productTotalQty.get(k).getQty();
                                            productTotalQty.get(k).setQty(totalQty);
                                            Log.d(this.getClass().toString(),"sales1 masuk qty k " + k + " : "   + productTotalQty.get(k).getQty());
                                            break;
                                        }
                                        else if((k == (size - 1)) && !(orderData.getName().equals(productTotalQty.get(k).getName()))){
                                            Log.d(this.getClass().toString(),"sales1 nyemplung uy size : " + size);

                                            productTotalQty.add(orderData);
                                            break;
                                        }
                                    }
                                }
                                Log.d(this.getClass().toString(),"sales1 productTotalQty size : "  + productTotalQty.size());
                                for(int i = 0; i < productTotalQty.size(); i++){
                                    Log.d(this.getClass().toString(),"sales1 productTotalQty name" + i + " : "  + productTotalQty.get(i).getName());
                                    Log.d(this.getClass().toString(),"sales1 productTotalQty qty" + i + " : "  + productTotalQty.get(i).getQty());
                                }


                                makeChart();
                            }
//                            Log.d(this.getClass().toString(),"sales1 size test: "  + test);

                        }
                    });


//                    db.collection("orders").document(orderIds.get(i)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentSnapshot> tasks) {
//
//                            if(tasks.isSuccessful()){
//                                DocumentSnapshot document = tasks.getResult();
//
//
//
//                                test++;
//                            }
//
//                            //setTotalQty();
//                            Log.d(this.getClass().toString(),"sales1 orderDetail sizea : "  + orderDetail.size());
//
//                            makeChart();
//                        }
//
//                    });
//                    if(test == (orderIds.size()-1)){
//                        Log.d(this.getClass().toString(),"Baru selesai sizea gana");
//
//                        //makeChart();
//                    }
                }

            }
        });




    }

    void makeChart(){
        List<DataEntry> data = new ArrayList<>();
        Cartesian cartesian = AnyChart.column();
        data.clear();

//        Log.d(this.getClass().toString(),"sales1 size gan1 : "  + productTotalQty.size());

        for(int i = 0; i < productTotalQty.size(); i++){
//            Log.d(this.getClass().toString(),"sales1 size gan : "  + productTotalQty.size());
            data.add(new ValueDataEntry(productTotalQty.get(i).getName(), productTotalQty.get(i).getQty()));
        }

        set = Set.instantiate();
        set.data(data);



        Column column = cartesian.column(data);

        column.normal().fill("#ffc526", 1);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{groupsSeparator: }")
                .fontColor("#FFFFFF");

        cartesian.animation(true);
        cartesian.title("Top 10 Ordered Menu");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Menu");
        cartesian.yAxis(0).title("Quantity");

        anyChartView.setChart(cartesian);
    }

    void setTotalQty(){
//        Log.d(this.getClass().toString(),"sales1 size gan  order: "  + orderDetail.size());
//
//        for(int i = 0; i < orderDetail.size(); i++){
//            if(productTotalQty.size() == 0){
//                productTotalQty.add(orderDetail.get(i));
//            }
//            else{
//                for(int j = 0; j < productTotalQty.size(); j++){
//                    if(orderDetail.get(i).getName().equals(productTotalQty.get(j).getName())){
//                        int totalQty;
//                        totalQty = orderDetail.get(i).getQty() + productTotalQty.get(j).getQty();
//                        productTotalQty.get(j).setQty(totalQty);
//                    }
//                    else if((j == (productTotalQty.size() - 1)) && !orderDetail.get(i).getName().equals(productTotalQty.get(j).getName())){
//                        productTotalQty.add(orderDetail.get(i));
//                    }
//                }
//            }
//        }


        Log.d(this.getClass().toString(),"sales1 size gan ditotal: "  + productTotalQty.size());

    }

    class CreateChart extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }


        protected void onPostExecute(Void result){

            Log.d(this.getClass().toString(),"sales1 orderDetail sizea : "  + orderDetail.size());

            Cartesian cartesian = AnyChart.column();

            List<DataEntry> data = new ArrayList<>();
            Log.d(this.getClass().toString(),"sales1 size gan : "  + productTotalQty.size());

            for(int i = 0; i < productTotalQty.size(); i++){
                Log.d(this.getClass().toString(),"sales1 size gan : "  + productTotalQty.size());
                data.add(new ValueDataEntry(productTotalQty.get(i).getName(), productTotalQty.get(i).getQty()));
            }

            set = Set.instantiate();
            set.data(data);



            Column column = cartesian.column(data);

            column.normal().fill("#ffc526", 1);

            column.tooltip()
                    .titleFormat("{%X}")
                    .position(Position.CENTER_BOTTOM)
                    .anchor(Anchor.CENTER_BOTTOM)
                    .offsetX(0d)
                    .offsetY(5d)
                    .format("{%Value}{groupsSeparator: }")
                    .fontColor("#000000");

            cartesian.animation(true);
            cartesian.title("Top 10 Ordered Menu");

            cartesian.yScale().minimum(0d);

            cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

            cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
            cartesian.interactivity().hoverMode(HoverMode.BY_X);

            cartesian.xAxis(0).title("Menu");
            cartesian.yAxis(0).title("Quantity");

            anyChartView.setChart(cartesian);

        }


    }



    void getTop10(){
        CollectionReference orders = db.collection("orders");

        orders.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    int i = 0;
                    for(DocumentSnapshot document : task.getResult()){
                        String orderId;
                        orderId = document.getId();
                        orderIds.add(orderId);
                        Log.d(this.getClass().toString(),"sales1 product Id : "  + orderIds.get(i));
                        i++;
                    }
                }
                Log.d(this.getClass().toString(),"sales1 product size : "  + orderIds.size());

                Calendar calendar = Calendar.getInstance();
                String dateAndTime = calendar.getTime().toString();
                Log.d(this.getClass().toString(),"sales1 date : "  + dateAndTime);

                for(int i = 0; i < orderIds.size(); i++){
                    db.collection("orders").document(orderIds.get(i)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> tasks) {
                            Log.d(this.getClass().toString(),"sales1 masuk gan");

                            if(tasks.isSuccessful()){
                                DocumentSnapshot document = tasks.getResult();
                                //Product product = document.toObject(Product.class);

                                ArrayList<HashMap<String, Object>> order = (ArrayList<HashMap<String, Object>>) document.get("orderDetails");
                                //ArrayList<Product> order = (ArrayList<Product>) document.get("orderDetails");


                                Log.d(this.getClass().toString(),"sales1 order size: "  + order.get(0).get("name"));



                                //Log.d(this.getClass().toString(),"sales1 orderData: "  + orderData.getName());

                                for(int j = 0; j < order.size(); j++){

                                    String name = order.get(j).get("name").toString();
                                    String qtys = order.get(j).get("qty").toString();
                                    int qty = Integer.parseInt(qtys);
                                    String prices = order.get(j).get("price").toString();
                                    int price = Integer.parseInt(prices);
                                    String imageUrl = order.get(j).get("imageUrl").toString();
                                    Product orderData = new Product(name, imageUrl, price, qty);

                                    orderDetail.add(orderData);

                                    Log.d(this.getClass().toString(),"sales1 orderDetail id : "  + orderDetail.get(j).getName());

//                                    int qty  = order.get(j).get("qty");

//                                    orderDetail.add(order.get(j));
//                                    ord)

                                }
//                                Log.d(this.getClass().toString(),"sales1 orderDetail id : "  + orderDetail.get(0));

                            }
                        }
                    });
                }
            }
        });
        orders.document().getId();
        Log.d(this.getClass().toString(),"collection id: " + orders.document().getId());
    }
}
