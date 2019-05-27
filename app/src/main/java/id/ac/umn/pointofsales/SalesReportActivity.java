package id.ac.umn.pointofsales;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;


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

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SalesReportActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    ArrayList<Product> orderDetail = new ArrayList<Product>();
    ArrayList<Product> ord = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Set set;

    TextView selectStartDate;
    TextView selectEndDate;
    Button btnReport;
    DatePickerDialog startDatePickerDialog;
    private CardView weeklyBtn;
    private CardView monthlyBtn;
    private LinearLayout weeklyBorder;
    private LinearLayout monthlyBorder;
    private LinearLayout weeklyPickerStart;
    private LinearLayout weeklyPickerEnd;
    private LinearLayout monthlyPicker;
    private Spinner spinner;

    Date startDates;
    Date endDates;
    int selected = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_report);

        selectStartDate = findViewById(R.id.select_start_date);
        selectEndDate = findViewById(R.id.select_end_date);
        btnReport = findViewById(R.id.btn_report);
        weeklyBtn = findViewById(R.id.weekly_btn);
        weeklyBorder = findViewById(R.id.weekly_border);
        monthlyBtn = findViewById(R.id.monthly_btn);
        monthlyBorder = findViewById(R.id.monthly_border);
        weeklyPickerStart = findViewById(R.id.weekly_picker_start);
        weeklyPickerEnd = findViewById(R.id.weekly_picker_end);
        monthlyPicker = findViewById(R.id.monthly_picker);

        spinner = findViewById(R.id.select_month);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.month, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        selected = 1;
        if(selected == 1){
            monthlyPicker.setVisibility(View.GONE);
            weeklyBorder.setBackgroundResource(R.drawable.card_border);
            monthlyBorder.setBackgroundResource(R.drawable.salesreport_clicked_border);
        }


        selectStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerStartDialog();
            }
        });

        weeklyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selected == 0){
                    selected = 1;
                    weeklyBorder.setBackgroundResource(R.drawable.card_border);
                    monthlyBorder.setBackgroundResource(R.drawable.salesreport_clicked_border);
                    monthlyPicker.setVisibility(View.GONE);
                    weeklyPickerStart.setVisibility(View.VISIBLE);
                    weeklyPickerEnd.setVisibility(View.VISIBLE);
                }
            }
        });

        monthlyBorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selected == 1){
                    selected = 0;
                    monthlyBorder.setBackgroundResource(R.drawable.card_border);
                    weeklyBorder.setBackgroundResource(R.drawable.salesreport_clicked_border);
                    weeklyPickerStart.setVisibility(View.GONE);
                    weeklyPickerEnd.setVisibility(View.GONE);
                    monthlyPicker.setVisibility(View.VISIBLE);
                }
            }
        });

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selected == 1){
                    Intent intent = new Intent(SalesReportActivity.this, ChartActivity.class);
                    intent.putExtra("startDates", startDates.getTime());
                    intent.putExtra("endDates", endDates.getTime());
                    startActivity(intent);
                }
                else if(selected == 0){
                    Intent intent = new Intent(SalesReportActivity.this, ChartActivity.class);
                    intent.putExtra("startDates", startDates.getTime());
                    intent.putExtra("endDates", endDates.getTime());
                    startActivity(intent);
                }

            }
        });

    }

    private void showDatePickerStartDialog(){
        startDatePickerDialog = new DatePickerDialog(this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        startDatePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        startDatePickerDialog.show();
    }



    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String startDate = dayOfMonth + " - " + (month+1) + " - " + year;
        String endDate = "";

        Calendar start = Calendar.getInstance();
        start.set(Calendar.YEAR, year);
        start.set(Calendar.MONTH, month);
        start.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        startDates = start.getTime();

        Calendar end = Calendar.getInstance();

        endDates = start.getTime();

        Calendar todays = Calendar.getInstance();
        int todaysMonth = todays.get(Calendar.MONTH);
        int todaysDate = todays.get(Calendar.DAY_OF_MONTH);
        int todaysYear = todays.get(Calendar.YEAR);
        if((month <= todaysMonth) && ((dayOfMonth + 6) <= todaysDate) && year <= todaysYear) {
            endDate = (dayOfMonth + 6) + " - " + (month+1) + " - " + year;
            end.set(Calendar.YEAR, year);
            end.set(Calendar.MONTH, month);
            end.set(Calendar.DAY_OF_MONTH, dayOfMonth + 6);
        }
        else if(((dayOfMonth + 6) >= 31)){
            int newDays = 0;
            int newMonths = 0;
            int newYears = year;
            end.set(Calendar.YEAR, year);
            if((month % 2 == 0) && ((month + 1) != 2) ){
                newDays = dayOfMonth + 6 - 31;
                newMonths = month+ 1;
            }
            else if((month % 2 == 1) && ((month + 1) != 2) ){
                newDays = dayOfMonth + 6 - 30;
                newMonths = month+ 1;
            }
            else if((month+1) == 2){
                newDays = dayOfMonth + 6 - 28;
                newMonths = month+ 1;
            }
            else if((month+1) == 12){
                newDays = dayOfMonth + 6 - 31;
                newMonths = 1;
                newYears = year+ 1;
                end.set(Calendar.YEAR, newYears);
            }

            end.set(Calendar.MONTH, newMonths);
            end.set(Calendar.DAY_OF_MONTH, newDays);
            endDate = newDays + " - " + (newMonths+1) + " - " + newYears;

        }
        else{
            endDate = todaysDate + " - " + (todaysMonth+1) + " - " + todaysYear;
            end.set(Calendar.YEAR, todaysYear);
            end.set(Calendar.MONTH, todaysMonth);
            end.set(Calendar.DAY_OF_MONTH, todaysDate);
        }
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);
        startDates = start.getTime();
        endDates = end.getTime();
        selectStartDate.setText(startDate);
        selectEndDate.setText(endDate);



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
        Calendar start = Calendar.getInstance();
        start.set(Calendar.DAY_OF_MONTH, 1);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.YEAR, 2019);
        start.set(Calendar.MONTH, pos);
        startDates = start.getTime();
        Calendar end = Calendar.getInstance();
        end.set(Calendar.DAY_OF_MONTH, 1);
        end.set(Calendar.HOUR_OF_DAY, 0);
        end.set(Calendar.MINUTE, 0);
        end.set(Calendar.SECOND, 0);
        end.set(Calendar.YEAR, 2019);
        end.set(Calendar.MONTH, (pos + 1));
        endDates = end.getTime();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {


    }
}
