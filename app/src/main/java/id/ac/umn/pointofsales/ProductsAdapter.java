package id.ac.umn.pointofsales;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductHolder> {

    private Context context;
    private ArrayList<Product> products;

    public ProductsAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_row, parent, false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        Product product = products.get(position);
        holder.setDetails(product);
    }

    @Override
    public int getItemCount() {
        return products!= null ? products.size() : 0;
    }

    public class ProductHolder extends RecyclerView.ViewHolder{
        private TextView txtName, txtPrice;

        public ProductHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
        }

        public void setDetails(Product product){
            txtName.setText(product.getName());
            txtPrice.setText(String.format(Locale.US, "Rp %,d.00", product.getPrice()));
        }
    }
}
