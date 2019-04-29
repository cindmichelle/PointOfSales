package id.ac.umn.pointofsales;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment fragmentProducts = new FragmentProducts();
        fragmentTransaction.replace(R.id.main_activity_fragment_products, fragmentProducts);

        Fragment fragmentOrderList = new FragmentOrderList();
        fragmentTransaction.replace(R.id.main_activity_fragment_order_list, fragmentOrderList);

        fragmentTransaction.commit();
    }
}
