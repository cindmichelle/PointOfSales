package id.ac.umn.pointofsales;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements FragmentMenu.FragmentListener {

    IntentFilter intentFilter;



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

        intentFilter = new IntentFilter();
        intentFilter.addAction("move_activity");

        registerReceiver(moveActivity, intentFilter);
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



    private BroadcastReceiver moveActivity = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            unregisterReceiver(moveActivity);
            Intent i = new Intent(MainActivity.this, MainMenuActivity.class);
            finish();
            startActivity(i);
        }
    };
}
