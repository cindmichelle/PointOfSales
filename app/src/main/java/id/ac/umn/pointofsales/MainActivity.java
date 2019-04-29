package id.ac.umn.pointofsales;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements FragmentMenu.FragmentListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment fragmentMenu = new FragmentMenu();
        fragmentTransaction.replace(R.id.main_activity_fragment_menus, fragmentMenu);

        Fragment fragmentProducts = new FragmentProducts();
        fragmentTransaction.replace(R.id.main_activity_fragment_products, fragmentProducts);

        Fragment fragmentOrderList = new FragmentOrderList();
        fragmentTransaction.replace(R.id.main_activity_fragment_order_list, fragmentOrderList);

        fragmentTransaction.commit();
    }

    @Override
    public void onButtonClicked(String text) {

        FragmentProducts fragmentProducts = (FragmentProducts) getSupportFragmentManager()
                .findFragmentById(R.id.main_activity_fragment_products);

        if(fragmentProducts != null){
            fragmentProducts.onChangeMenu(text);
        }
        else {
            fragmentProducts = new FragmentProducts();

            Bundle arguments = new Bundle();

            arguments.putString("menu", text);

            fragmentProducts.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_activity_fragment_products, fragmentProducts)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
