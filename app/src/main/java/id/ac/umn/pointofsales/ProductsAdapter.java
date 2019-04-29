package id.ac.umn.pointofsales;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import static android.content.ContentValues.TAG;


public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductHolder>{

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
        private ImageView imageProduct;
        private Button btnPlus, btnMinus;
        FragmentCommunication mCommunication;

        public ProductHolder(View itemView, FragmentCommunication Communicator) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtQty = itemView.findViewById(R.id.txtQty);
            btnPlus = itemView.findViewById(R.id.plusQty);
            btnMinus = itemView.findViewById(R.id.minusQty);
            imageProduct = itemView.findViewById(R.id.imageProduct);



            mCommunication = Communicator;

            btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    products.get(position).plusQty();
                    txtQty.setText(String.format(Locale.US, "%,d", products.get(position).getQty()));
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

        private class DownLoadImageTask extends AsyncTask<String,Void, Bitmap> {
            ImageView imageView;

            public DownLoadImageTask(ImageView imageView){
                this.imageView = imageView;
            }

            /*
                doInBackground(Params... params)
                    Override this method to perform a computation on a background thread.
             */
            protected Bitmap doInBackground(String...urls){
                String urlOfImage = urls[0];
                Bitmap logo = null;
                try{
                    InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                    logo = BitmapFactory.decodeStream(is);
                }catch(Exception e){ // Catch the download exception
                    Log.d(TAG, "error dude : " + e.getMessage());
                }
                return logo;
            }

            /*
                onPostExecute(Result result)
                    Runs on the UI thread after doInBackground(Params...).
             */
            protected void onPostExecute(Bitmap result){
                imageView.setImageBitmap(result);
            }
        }

        public void setDetails(Product product){
            new DownLoadImageTask(imageProduct).execute(product.getImageUrl());

            Log.d(TAG, "setDetails ProductAdapter" + " imgUrl : " + product.getImageUrl() );

            Log.d(TAG, "DocumentSnapshot data: " + product.getName() + "  DARRRR");
            txtName.setText(product.getName());
            Log.d(this.getClass().toString(),"ID barang: " + product.getId());
            txtPrice.setText(String.format(Locale.US, "Rp %,d.00", product.getPrice()));
        }
    }


}
