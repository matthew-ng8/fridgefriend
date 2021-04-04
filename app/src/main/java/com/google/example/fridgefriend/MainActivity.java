
package com.google.example.fridgefriend;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "";
    TextView textLog, logT;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setContentView(R.layout.home_page);
        TextView logT = (TextView) findViewById(R.id.textLog);
        logT.setVisibility(View.INVISIBLE);

        Button login = (Button) findViewById(R.id.loginButton);
        findViewById(R.id.loginButton).setOnClickListener(accListner);
        //buttons
        findViewById(R.id.homePage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,HomePage.class);

                startActivity(intent);
            }
        });

    }

    private View.OnClickListener accListner = new View.OnClickListener() {
        public void onClick(View v) {
            AccountManager accMan = AccountManager.get(getApplicationContext());
            Intent googlePicker = AccountManager.newChooseAccountIntent(null, null,
                    new String[] {"com.google"}, true, null, null, null, null);
            startActivityForResult(googlePicker, 1);
            login.setVisibility(Button.INVISIBLE);

        }
    };

    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            Bundle options = new Bundle();
            AccountManager accountManager = AccountManager.get(this);
            Account[] accounts = accountManager.getAccounts();
            for(Account a : accounts){
                Log.d(TAG, "type--" + a.type + "---name---" + a.name);
                accountManager.invalidateAuthToken(a.type, null);
                accountManager.getAuthToken(
                        a,
                        "Manager your tasks",
                        options,
                        this,
                        new OnTokenAcquired(),
                        new Handler(new OnError()));

            }
        }
    }

    private class Handler extends android.os.Handler{
        public Handler(OnError onError){

        }

    }



    private class OnTokenAcquired implements AccountManagerCallback<Bundle> {


        public class networkThread implements Runnable
        {
            AccountManagerFuture<Bundle> result;
            public networkThread(AccountManagerFuture<Bundle> result)
            {
                this.result = result;
            }

            public void run(){
                try {
                    Intent launch = (Intent) result.getResult().get((AccountManager.KEY_INTENT));
                    if(launch != null){
                        startActivityForResult(launch, 0);
                        return;
                    }
                    Bundle bundle = null;
                    bundle = result.getResult();

                    String token = bundle.getString(AccountManager.KEY_AUTHTOKEN);

                    String apiKey = "AIzaSyBZyXazssSe3L-GrYJ9f29lfkEKiSd3ZFY";
                    String clientID = "716402394253-elcpt5ku3srbavkbpgaho4oae2esuv7l.apps.googleusercontent.com";
                    String clientSecret = "";
                    URL url = new URL("https://www.googleapis.com/tasks/v1/users/@me/lists?key=" + apiKey);
                    URLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.addRequestProperty("client_id", clientID);
                    conn.addRequestProperty("client_secret", clientSecret);
                    conn.setRequestProperty("Authorization", "OAuth " + token);
                    logT.setVisibility(View.VISIBLE);


                } catch (AuthenticatorException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (OperationCanceledException e) {
                    e.printStackTrace();
                }
            }
        }
        @Override
        public void run(AccountManagerFuture<Bundle> result) {
            Thread t = new Thread(new networkThread(result));
            t.start();
        }
    }

    private class OnError {
    }

}
