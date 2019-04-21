package id.ac.umn.pointofsales;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.ArrayList;

public class FragmentOrderList extends Fragment implements Serializable{

    private RecyclerView recyclerViewOrderList;
    private OrderListAdapter orderListAdapter;
    private ArrayList<Product> orderLists = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);

        recyclerViewOrderList = view.findViewById(R.id.detailOrder_list);
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
