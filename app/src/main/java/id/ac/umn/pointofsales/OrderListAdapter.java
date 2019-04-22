package id.ac.umn.pointofsales;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderListHolder>{
    private List<Product> orderLists;
    private Context context;
    Product orderList;

    public OrderListAdapter(Context context, ArrayList<Product> orderLists) {
        this.context = context;
        this.orderLists = orderLists;
    }

    @NonNull
    @Override
    public OrderListAdapter.OrderListHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.order_list_row, parent, false);
        return new OrderListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListHolder OrderListHolder, int position) {
        orderList = orderLists.get(position);
        OrderListHolder.setDetails(orderList);
    }

    @Override
    public int getItemCount() {
        return (orderLists != null) ? orderLists.size() : 0;
    }

    public class OrderListHolder extends RecyclerView.ViewHolder{
        private TextView txtNama, txtHarga, txtQty, txtTotal;

        public OrderListHolder(View itemView) {
            super(itemView);

            txtNama = itemView.findViewById(R.id.menu_name);
            txtHarga = itemView.findViewById(R.id.menu_price);
            txtQty = itemView.findViewById(R.id.order_qty);
            txtTotal = itemView.findViewById(R.id.menu_order_total);
        }

        public void setDetails(Product orderList){
            txtNama.setText(orderList.getName());
            txtHarga.setText(String.format(Locale.US, "Rp %,d.00", orderList.getPrice()));
            txtQty.setText(String.format(Locale.US, "%,d x ", orderList.getQty()));
            int total = orderList.getQty() * orderList.getPrice();
            txtTotal.setText(String.format(Locale.US, "Rp %,d.00", total));
        }
    }


}
