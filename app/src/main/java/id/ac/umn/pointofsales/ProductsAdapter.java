package id.ac.umn.pointofsales;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;


public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductHolder> {

    private Context context;
    private ArrayList<Product> products;
    private FragmentCommunication mCommunicator;

    Product product;

    public interface FragmentCommunication{
        void onButtonClicked(String id, String name, int price, int plusMinus);
    }

    public ProductsAdapter(Context context, ArrayList<Product> products, FragmentCommunication communication) {
        this.context = context;
        this.products = products;
        mCommunicator=communication;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_row, parent, false);
        return new ProductHolder(view, mCommunicator);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        product = products.get(position);
        holder.setDetails(product);
    }

    @Override
    public int getItemCount() {
        return products!= null ? products.size() : 0;
    }

    public class ProductHolder extends RecyclerView.ViewHolder{
        private TextView txtName, txtPrice, txtQty;
        private Button btnPlus, btnMinus;
        FragmentCommunication mCommunication;

        public ProductHolder(View itemView, FragmentCommunication Communicator) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtQty = itemView.findViewById(R.id.txtQty);
            btnPlus = itemView.findViewById(R.id.plusQty);
            btnMinus = itemView.findViewById(R.id.minusQty);

            //listener = (FragmentListener) context;
            mCommunication = Communicator;

            btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    products.get(position).plusQty();
                    txtQty.setText(String.format(Locale.US, "%,d", products.get(position).getQty()));
                    Log.d(this.getClass().toString(),"Qty: " + products.get(position).getQty());
                    mCommunication.onButtonClicked(products.get(position).getId(), products.get(position).getName(), products.get(position).getPrice(), 1);
                }
            });

            btnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(products.get(position).getQty() > 0){
                        products.get(position).minusQty();
                        txtQty.setText(String.format(Locale.US, "%,d", products.get(position).getQty()));
                    }
                    mCommunication.onButtonClicked(products.get(position).getId(), products.get(position).getName(), products.get(position).getPrice(), 0);
                }
            });
        }

        public void setDetails(Product product){
            txtName.setText(product.getName());
            Log.d(this.getClass().toString(),"ID barang: " + product.getId());
            txtPrice.setText(String.format(Locale.US, "Rp %,d.00", product.getPrice()));
        }
    }


}
