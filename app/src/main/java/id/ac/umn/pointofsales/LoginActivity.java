package id.ac.umn.pointofsales;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();

        Button btn_submit_login = (Button) findViewById(R.id.btn_submit_login);
        btn_submit_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compareLogin();
            }
        });
    }

    private void compareLogin() {
        EditText et_username = (EditText) findViewById(R.id.et_username);
        EditText et_password = (EditText) findViewById(R.id.et_password);

        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        String username = sharedPref.getString("username", "");
        String password = sharedPref.getString("password", "");

        if(username.equals(et_username.getText().toString()) && password.equals(et_password.getText().toString())){
            //Toast.makeText(this, "Valid!!!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(LoginActivity.this, MainMenuActivity.class);
            startActivity(i);
        }
        else{
            Toast.makeText(this, "Invalid username/password", Toast.LENGTH_SHORT).show();
        }
    }

    private void initialize() {
        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username", "poemcafe.mobile.chs@gmail.com");
        editor.putString("password", "adminpos");
        editor.apply();

        Toast.makeText(this, "SharedPref saved!", Toast.LENGTH_SHORT).show();
    }
}
