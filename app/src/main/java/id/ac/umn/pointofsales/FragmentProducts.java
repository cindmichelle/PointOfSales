package id.ac.umn.pointofsales;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.ArrayList;


public class FragmentProducts extends Fragment implements Serializable {

    private RecyclerView recyclerViewProducts;
    private ArrayList<Product> products = new ArrayList<>();
    ArrayList<Product> orders = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        recyclerViewProducts = view.findViewById(R.id.products_list);
        ProductsAdapter productsAdapter = new ProductsAdapter(getContext(), products, communication);
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
        product = new Product("1","Cappucino", 43000);
        products.add(product);

        product = new Product("2","Americano", 45000);
        products.add(product);


        product = new Product("3","Caramel Macchiato", 56000);
        products.add(product);

        product = new Product("4","Breakfast Tea", 28000);
        products.add(product);
    }

    ProductsAdapter.FragmentCommunication communication = new ProductsAdapter.FragmentCommunication(){

        @Override
        public void onButtonClicked(String id, String name, int price, int plusMinus) {
            Bundle bundle = new Bundle();

            Product order;
            order = new Product(id, name, price);

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
}
