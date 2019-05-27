package id.ac.umn.pointofsales;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.WriteResult;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.ContentValues.TAG;

public class CashSelectionPopup extends DialogFragment implements Serializable{

    Button confirmBtn;
    Button cancelBtn;
    TextView shouldPay;
    TextView change;
    EditText paidEditText;

    private ArrayList<Product> products = new ArrayList<>();
    IntentFilter intentFilter;
    SweetAlertDialog pDialog;
    boolean sukses;
    boolean complete = false;
    int changes;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View popUpDialog = inflater.inflate(R.layout.popup_cash_selection, null);

        builder.setView(popUpDialog);

        complete = false;

        confirmBtn = popUpDialog.findViewById(R.id.confirm_cash_selection_btn);
        cancelBtn = popUpDialog.findViewById(R.id.cancel_cash_selection_btn);
        paidEditText = popUpDialog.findViewById(R.id.paidEditText);
        shouldPay = popUpDialog.findViewById(R.id.shouldPayText);
        change = popUpDialog.findViewById(R.id.changeText);


        Bundle args = getArguments();
        final int totalPayment = args.getInt("totalPayment");
        shouldPay.setText(String.format(Locale.US, "Rp %,d.00", totalPayment));
        changes = 0;
        changes = 0 - totalPayment;
        shouldPay.setText(String.format(Locale.US, "Rp %,d.00", totalPayment));

        paidEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(paidEditText.getText().toString().matches("")){
                    changes = -totalPayment;
                }
                else{
                    int paids = Integer.parseInt(paidEditText.getText().toString());
                    changes = paids - totalPayment;
                }
                change.setText(String.format(Locale.US, "Rp %,d.00", changes));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        intentFilter = new IntentFilter();
        intentFilter.addAction("konfirmasi_data");
        getActivity().registerReceiver(uploadDataCompleteReceiver, intentFilter);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(confirmBtn.getContext(),"hello", Toast.LENGTH_SHORT).show();
                dismiss();
                pDialog = new SweetAlertDialog(confirmBtn.getContext(), SweetAlertDialog.PROGRESS_TYPE);
                pDialog.setTitleText("\n\nPembayaran Diproses");
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#FFBA00"));
                pDialog.getProgressHelper().setCircleRadius(196);
                pDialog.setCancelable(false);
                pDialog.show();

                UploadDataTask uploadDataTask = new UploadDataTask(products, getContext());
                uploadDataTask.execute();

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                DialogFragment paymentFragment = new PaymentSelectionPopup();
                paymentFragment.show(getFragmentManager(), "BACK TO PAYMENT SELECTION");
            }
        });

        builder.setCancelable(false);

        return builder.create();
    }


    class UploadDataTask extends AsyncTask<Product, String, String>{
        ArrayList<Product> product;
        Context context;
        public UploadDataTask(ArrayList<Product> products, Context context) {
            this.product = products;
            this.context = context;
        }

        @Override
        protected String doInBackground(Product... products) {
            uploadDataBroadcast();
            return null;
        }

        protected void onPostExecute(String result){
            pDialog.setTitleText("Success!")
                    .setContentText("Transaksi berhasil!").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                Intent intent = new Intent();

            intent.setAction("move_activity");
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            Log.d(this.getClass().toString(),"isi context : " + context);
            context.sendBroadcast(intent);
                }
            })
                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);



        }
    }
    public void uploadDataBroadcast(){
        Intent intent = new Intent();
        intent.setAction("upload_data");
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        getContext().sendBroadcast(intent);
    }

    private BroadcastReceiver uploadDataCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            context.unregisterReceiver(uploadDataCompleteReceiver);
            Bundle args = intent.getBundleExtra("BUNDLE");
            ArrayList<Product> orders = (ArrayList<Product>)  args.getSerializable("orderList");

            Calendar calendar = Calendar.getInstance();
            Date dateAndTime = calendar.getTime();


            Map<String, Object> docData = new HashMap<>();
            docData.put("orderDetails", orders);
            docData.put("date", dateAndTime);
            Log.d(this.getClass().toString(),"masuk broadcast transaksi");
            db.collection("orders").add(docData);
        }
    };
}
