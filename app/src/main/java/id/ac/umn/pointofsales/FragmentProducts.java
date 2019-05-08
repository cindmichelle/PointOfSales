package id.ac.umn.pointofsales;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class FragmentProducts extends Fragment implements Serializable {

    private RecyclerView recyclerViewProducts;
    private ArrayList<Product> products = new ArrayList<>();
    private String menu;
    ArrayList<Product> orders = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    BroadcastReceiver receiver;
    IntentFilter intentFilter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        recyclerViewProducts = view.findViewById(R.id.products_list);

        if(getArguments()!= null){
            onChangeMenu(getArguments().getString("menu"));
        }
        Log.d(TAG, "onCreateView Fragment Product");

        recyclerViewProducts.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate Fragment Product");
//        getBeveragesCollections();

        intentFilter = new IntentFilter();
        intentFilter.addAction("upload_data");

        getActivity().registerReceiver(mintaDataReceiver, intentFilter);
    }

    public void onChangeMenu(String text){
        this.menu = text;
        Log.d(TAG, "clicked 3, menu : " + text);
        getBeveragesCollections();

    }

    private void getBeveragesCollections(){

        if(products.size() > 0) {
            products.clear();
        }

        Query collectionReference = db.collection("menus").whereEqualTo("category", menu);

        collectionReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "getBVC Fragment Product");

                            for(DocumentSnapshot document : task.getResult()){
                                Product product = document.toObject(Product.class);
                                product.setId(document.getId());
                                product.setImageUrl(document.getString("image_url"));
                                Log.d(TAG,"DER  " + product.getImageUrl());
                                products.add(product);
                            }

                            ProductsAdapter productsAdapter = new ProductsAdapter(getContext(), products, communication);
                            recyclerViewProducts.setAdapter(productsAdapter);

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, e.getMessage());
                    }
                });
    }

    ProductsAdapter.FragmentCommunication communication = new ProductsAdapter.FragmentCommunication(){

        @Override
        public void onButtonClicked(String id, String name, int price,  int plusMinus) {
            Bundle bundle = new Bundle();

            Product order;
            order = new Product(name, "", price);
            order.setId(id);

            if(orders.size() > 0){
                for(int i = 0; i < orders.size(); i++){
                    if(orders.get(i).getId() == order.getId()){
                        if(orders.get(i).getQty() > 0 && plusMinus == 1){
                            orders.get(i).plusQty();
                        }
                        else if(orders.get(i).getQty() > 0 && plusMinus == 0) {
                            orders.get(i).minusQty();
                            if(orders.get(i).getQty() == 0){
                                orders.remove(i);
                            }
                        }
                        break;
                    }
                    else if(i == (orders.size() - 1) && orders.get(i).getId() != order.getId() && plusMinus == 1){
                        order.plusQty();
                        orders.add(order);
                        break;
                    }
                }
            }
            else{
                if(plusMinus == 1){
                    order.plusQty();
                    orders.add(order);
                }
            }
            Log.d(this.getClass().toString(),"Size : " + orders.size());

            bundle.putSerializable("DATA", orders);

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            FragmentOrderList fragmentOrderList = new FragmentOrderList();
            fragmentOrderList.setArguments(bundle);

            fragmentTransaction.replace(R.id.main_activity_fragment_order_list, fragmentOrderList);
            fragmentTransaction.commit();
        }
    };

    private BroadcastReceiver mintaDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(this.getClass().toString(),"context dalam receiver" + context);
            context.unregisterReceiver(mintaDataReceiver);
            Intent i = new Intent();
            Bundle args = new Bundle();
            args.putSerializable("orderList", orders);
            i.putExtra("BUNDLE", args);
            i.setAction("konfirmasi_data");
            i.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            context.sendBroadcast(i);
            Log.d(this.getClass().toString(),"Masuk ke broadcast product5");
        }
    };
}
