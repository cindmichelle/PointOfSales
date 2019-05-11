package id.ac.umn.pointofsales;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class SalesReportActivity extends AppCompatActivity {

    ArrayList<String> orderIds = new ArrayList<String>();
    ArrayList<Product> orderDetail = new ArrayList<Product>();
    List<Product> productTotalQty = new ArrayList<>();
    ArrayList<Product> ord = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Set set;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_report);

        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        Cartesian cartesian = AnyChart.column();

        getTop10();


        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Rouge", 180540));
        data.add(new ValueDataEntry("Foundation", 94190));
        data.add(new ValueDataEntry("Mascara", 102610));
        data.add(new ValueDataEntry("Lip gloss", 110430));
        data.add(new ValueDataEntry("Lipstick", 128000));
        data.add(new ValueDataEntry("Nail polish", 143760));
        data.add(new ValueDataEntry("Eyebrow pencil", 170670));
        data.add(new ValueDataEntry("Eyeliner", 213210));
        data.add(new ValueDataEntry("Eyeshadows", 249980));

        set = Set.instantiate();
        set.data(data);


        List<DataEntry> data2 = new ArrayList<>();
        data2.add(new ValueDataEntry("Rouge", 180540));
        data2.add(new ValueDataEntry("Foundation", 180540));
        data2.add(new ValueDataEntry("Mascara", 102610));
        data2.add(new ValueDataEntry("Lip gloss", 110430));
        set.data(data2);

        Column column = cartesian.column(data);

        column.normal().fill("#ffc526", 1);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("${%Value}{groupsSeparator: }")
                .fontColor("#FFFFFF");

        cartesian.animation(true);
        cartesian.title("Top 10 Cosmetic Products by Revenue");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("${%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Product");
        cartesian.yAxis(0).title("Revenue");

        anyChartView.setChart(cartesian);
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
