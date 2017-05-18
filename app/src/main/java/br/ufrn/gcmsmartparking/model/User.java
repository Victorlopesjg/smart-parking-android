package br.ufrn.gcmsmartparking.model;

import br.ufrn.gcmsmartparking.annotation.POST;
import br.ufrn.gcmsmartparking.annotation.PUT;

/**
 * Created by Victor Oliveira on 18/05/17.
 * Email: victorlopesjg@gmail.com
 */

@POST("user/create")
@PUT("user/update")
public class User {
    private String login;
    private String senha;
    private String token;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
