package br.ufrn.gcmsmartparking.model;

import br.ufrn.gcmsmartparking.annotation.AUTH;
import br.ufrn.gcmsmartparking.annotation.POST;
import br.ufrn.gcmsmartparking.annotation.PUT;

/**
 * Created by Victor Oliveira on 18/05/17.
 * Email: victorlopesjg@gmail.com
 */

@POST("user/")
@PUT("user/update")
@AUTH("user/auth")
public class User {
    private String login;
    private String password;
    private String token;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
