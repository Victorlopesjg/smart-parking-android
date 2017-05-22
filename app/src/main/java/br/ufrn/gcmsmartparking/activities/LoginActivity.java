package br.ufrn.gcmsmartparking.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import br.ufrn.gcmsmartparking.R;
import br.ufrn.gcmsmartparking.business.PreferencesUserTools;
import br.ufrn.gcmsmartparking.business.WebService;
import br.ufrn.gcmsmartparking.model.User;

public class LoginActivity extends AppCompatActivity {

    private EditText login;
    private EditText password;
    private Button loginAction;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(PreferencesUserTools.getPreferencias(getResources().getString(R.string.key_preference_user), getApplicationContext()) != null
                && !PreferencesUserTools.getPreferencias(getResources().getString(R.string.key_preference_user), getApplicationContext()).equalsIgnoreCase("")){
            startActivity(new Intent(this, MainActivity.class));
        }

        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);
        loginAction = (Button) findViewById(R.id.loginButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);

        loginAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new LoginTask().execute(login.getText().toString(),
                        password.getText().toString());
            }
        });
    }

    private class LoginTask extends AsyncTask<String, String, Void> {

        private WebService webService;

        public WebService getInstanceWebService(){
            if(webService == null){
                this.webService = new WebService();
            }
            return webService;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... params) {
            User user = new User();
            user.setLogin(params[0]);
            user.setPassword(params[1]);
            Log.i("LOGIN", user.getLogin() + " " + user.getPassword());
            try {
                User userResponse = this.getInstanceWebService().auth(user, getApplicationContext());
                if(userResponse != null) {
                    PreferencesUserTools.setPreferencias(userResponse, getResources().getString(R.string.key_preference_user), false, getApplicationContext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(PreferencesUserTools.getPreferencias(getResources().getString(R.string.key_preference_user), getApplicationContext()) != null &&
                    PreferencesUserTools.getPreferencias(getResources().getString(R.string.key_preference_user), getApplicationContext()).equals(login.getText().toString())){
                progressBar.setVisibility(View.GONE);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Usuário ou senha inválidos", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
