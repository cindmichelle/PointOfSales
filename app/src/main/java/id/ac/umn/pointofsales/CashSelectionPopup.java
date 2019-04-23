package id.ac.umn.pointofsales;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CashSelectionPopup extends DialogFragment {

    Button confirmBtn;
    Button cancelBtn;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View popUpDialog = inflater.inflate(R.layout.popup_cash_selection, null);

        builder.setView(popUpDialog);

        confirmBtn = popUpDialog.findViewById(R.id.confirm_cash_selection_btn);
        cancelBtn = popUpDialog.findViewById(R.id.cancel_cash_selection_btn);


        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(confirmBtn.getContext(),"hello", Toast.LENGTH_SHORT).show();
                dismiss();
                SweetAlertDialog pDialog = new SweetAlertDialog(confirmBtn.getContext(), SweetAlertDialog.PROGRESS_TYPE);
                pDialog.setTitleText("\n\nPembayaran Diproses");
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#FFBA00"));
                pDialog.getProgressHelper().setCircleRadius(196);
                pDialog.setCancelable(false);
                pDialog.show();
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
}
