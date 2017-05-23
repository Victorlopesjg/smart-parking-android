package br.ufrn.gcmsmartparking.business;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.iid.FirebaseInstanceId;

import br.ufrn.gcmsmartparking.R;
import br.ufrn.gcmsmartparking.model.User;

/**
 * Created by Dannylo Johnathan on 21/05/2017.
 */

public class PreferencesUserTools {

    private static SharedPreferences sharedPreferences;
    private static final String PREFS_PRIVATE = "PREFS_PRIVATE";

    public static boolean isPreference(Context context) {
        return (PreferencesUserTools.getPreferencias(context.getString(R.string.key_preference_user), context) != null
                && !PreferencesUserTools.getPreferencias(context.getString(R.string.key_preference_user), context).isEmpty());
    }

    public static boolean isTokenPreference(Context context){
        return (PreferencesUserTools.getPreferencias(context.getString(R.string.key_preference_token), context) != null);
    }

    // Limpar preferencias do smartphone
    public static void cleanPreferences(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE);
        settings.edit().clear().apply();
    }

    // Limpar uma preferencia do smartphone
    public static void cleanOnePreference(String key, Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE);
        settings.edit().remove(key).commit();
    }

    // Salva as preferencias string(value) na memoria local
    public static void setPreferencias(String value, String key, boolean clear, Context context) {
        if (clear) {
            SharedPreferences settings = context.getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE);
            settings.edit().remove(key).commit();
        }

        sharedPreferences = context.getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsPrivateEditor = sharedPreferences.edit();
        prefsPrivateEditor.putString(key, value + getPreferencias(key, context));

        prefsPrivateEditor.commit();
    }


    // Salva um User nas preferencias da memoria local.
    public static void setPreferencias(User obj, String key, boolean clear, Context context) {
        if (clear) {
            SharedPreferences settings = context.getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE);
            settings.edit().remove(key).commit();
        }

        sharedPreferences = context.getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsPrivateEditor = sharedPreferences.edit();

        prefsPrivateEditor.putString(key, obj.getLogin());

        prefsPrivateEditor.commit();
    }

    // retorna o Usuário na memoria local.
    public static String getPreferencias(String key, Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE);
        String retorno = sharedPreferences.getString(key, "");
        sharedPreferences = null;

        if (retorno == null || retorno.equalsIgnoreCase(""))
            return null;
        else
            return retorno;
    }

    // Não utilizado!

    /*public static Object[] getPreferenciasArray(String key, Context context) {
        sharedPreferences = context.getSharedPreferences(PREFS_PRIVATE, Context.MODE_PRIVATE);
        Object[] retorno = sharedPreferences.getString(key, "").split(",");
        sharedPreferences = null;
        return retorno;
    }
    */
}
