package id.ac.umn.pointofsales;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.Serializable;
import java.util.ArrayList;

public class FragmentOrderList extends Fragment implements Serializable{

    private RecyclerView recyclerViewOrderList;
    private OrderListAdapter orderListAdapter;
    private ArrayList<Product> orderLists = new ArrayList<>();
    Button payment_btn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);

        recyclerViewOrderList = view.findViewById(R.id.detailOrder_list);
        payment_btn = view.findViewById(R.id.payment_btn);
        orderListAdapter = new OrderListAdapter(getContext(), orderLists);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewOrderList.setLayoutManager(layoutManager);
        recyclerViewOrderList.setAdapter(orderListAdapter);

        if(getArguments() != null){
            createListData();

            recyclerViewOrderList = view.findViewById(R.id.detailOrder_list);
            orderListAdapter = new OrderListAdapter(getContext(), orderLists);
            layoutManager = new LinearLayoutManager(getActivity());
            recyclerViewOrderList.setLayoutManager(layoutManager);
            recyclerViewOrderList.setAdapter(orderListAdapter);
        }

        payment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment paymentFragment = new PaymentSelectionPopup();
                paymentFragment.show(getFragmentManager(), "PAYMENT SELECTION");
            }
        });
        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //createListData();
    }


    private void createListData(){
        ArrayList<Product> detailsOrder = (ArrayList<Product>) getArguments().getSerializable("DATA");

        for(int i = 0; i < detailsOrder.size();i++){
           orderLists.add(detailsOrder.get(i));
        }
    }
}
