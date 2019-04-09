package id.ac.umn.pointofsales;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class FragmentProducts extends Fragment {

    private RecyclerView recyclerViewProducts;
    private ArrayList<Product> products = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        recyclerViewProducts = view.findViewById(R.id.products_list);
        ProductsAdapter productsAdapter = new ProductsAdapter(getContext(), products);
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerViewProducts.setAdapter(productsAdapter);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createListData();
    }

    private void createListData(){
        Product product;
        product = new Product("Cappucino", 43000);
        products.add(product);

        product = new Product("Americano", 45000);
        products.add(product);


        product = new Product("Caramel Macchiato", 56000);
        products.add(product);

        product = new Product("Breakfast Tea", 28000);
        products.add(product);

    }
}
