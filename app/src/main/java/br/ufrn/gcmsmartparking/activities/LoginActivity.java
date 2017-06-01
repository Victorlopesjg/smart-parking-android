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

import com.google.firebase.iid.FirebaseInstanceId;

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

        if (PreferencesUserTools.isPreference(getApplicationContext())) {
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

    private class LoginTask extends AsyncTask<String, Boolean, Boolean> {

        private WebService webService;

        private WebService getInstanceWebService() {
            if (webService == null) {
                this.webService = new WebService();
            }
            return webService;
        }

        public void sendTokenRegistrationServer(User user, final String token) {
            Log.e("TAG_LOGIN_ACTIVITY", "sendRegistrationToServer: " + token);

            try {
                user.setToken(token);
                getInstanceWebService().put(user, getApplicationContext());
                Log.i("TAG_REGISTRATION", "TOKEN SUBMITED.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            User user = new User();
            user.setLogin(params[0]);
            user.setPassword(params[1]);
            try {
                boolean authorized = this.getInstanceWebService().auth(user, getApplicationContext());
                if (authorized) {
                    PreferencesUserTools.setPreferencias(user, getString(R.string.key_preference_user), false, getApplicationContext());
                    if(PreferencesUserTools.isTokenPreference(getApplicationContext())){
                        String newToken = PreferencesUserTools.getPreferencias(getString(R.string.key_preference_token), getApplicationContext());
                        this.sendTokenRegistrationServer(user, newToken);
                    }
                }
                return authorized;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            progressBar.setVisibility(View.GONE);

            if (result) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            } else {
                Toast.makeText(getApplicationContext(), "Usuário não encontrado", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
