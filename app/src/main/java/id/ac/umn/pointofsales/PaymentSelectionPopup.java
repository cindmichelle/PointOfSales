package id.ac.umn.pointofsales;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class PaymentSelectionPopup extends DialogFragment {

    CardView cashBtn;
    CardView debitBtn;
    CardView creditBtn;
    Button confirmBtn;
    Button cancelBtn;

    LinearLayout cashBtnBorder;
    LinearLayout debitBtnBorder;
    LinearLayout creditBtnBorder;

    private int paymentMethod = 0;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View popUpDialog = inflater.inflate(R.layout.popup_payment_selection, null);

        builder.setView(popUpDialog);





        cashBtn = popUpDialog.findViewById(R.id.cash_btn);
        creditBtn = popUpDialog.findViewById(R.id.credit_btn);
        debitBtn = popUpDialog.findViewById(R.id.debit_btn);
        confirmBtn = popUpDialog.findViewById(R.id.confirm_payment_selection_btn);
        cancelBtn = popUpDialog.findViewById(R.id.cancel_payment_selection_btn);

        cashBtnBorder = (LinearLayout) popUpDialog.findViewById(R.id.cashBorder);
        debitBtnBorder = (LinearLayout) popUpDialog.findViewById(R.id.debitBorder);
        creditBtnBorder = (LinearLayout) popUpDialog.findViewById(R.id.creditBorder);


        cashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentMethod = 0;
                cashBtnBorder.setBackgroundResource(R.drawable.card_clicked_border);
                debitBtnBorder.setBackgroundResource(R.drawable.card_border);
                creditBtnBorder.setBackgroundResource(R.drawable.card_border);
            }
        });

        debitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentMethod = 1;
                cashBtnBorder.setBackgroundResource(R.drawable.card_border);
                debitBtnBorder.setBackgroundResource(R.drawable.card_clicked_border);
                creditBtnBorder.setBackgroundResource(R.drawable.card_border);
            }
        });

        creditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentMethod = 2;
                cashBtnBorder.setBackgroundResource(R.drawable.card_border);
                debitBtnBorder.setBackgroundResource(R.drawable.card_border);
                creditBtnBorder.setBackgroundResource(R.drawable.card_clicked_border);
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(paymentMethod == 0){
                    dismiss();
                    DialogFragment cashSelectionPopup = new CashSelectionPopup();
                    Bundle mArgs = getArguments();
                    int totalPayment = mArgs.getInt("totalPayment");
                    mArgs.putInt("totalPayment", totalPayment);
                    cashSelectionPopup.setArguments(mArgs);
                    cashSelectionPopup.show(getFragmentManager(), "CASH SELECTION");
                }
                else {
                    Toast.makeText(confirmBtn.getContext(),"Still on Progress", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        builder.setCancelable(false);

        return builder.create();
    }
}
