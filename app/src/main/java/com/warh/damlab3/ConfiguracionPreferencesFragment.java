package com.warh.damlab3;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.warh.damlab3.dao.BuilderAPI;

public class ConfiguracionPreferencesFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_configuracion);

        EditTextPreference usuarioPref = (EditTextPreference) findPreference("usuario_sysacad");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        usuarioPref.setSummary(sharedPreferences.getString("usuario_sysacad", ""));

        final Preference prefUsuario = getPreferenceManager().findPreference(
                "usuario_sysacad");
        prefUsuario.setOnPreferenceChangeListener((preference, nuevoValor) -> {
            preference.setSummary(nuevoValor.toString());
            BuilderAPI.getInstancia().actualizarDatos();
            return true;
        });

        final Preference prefContrasena = getPreferenceManager().findPreference(
                "usuario_sysacad");
        prefContrasena.setOnPreferenceChangeListener((preference, nuevoValor) -> {
            preference.setSummary(nuevoValor.toString());
            BuilderAPI.getInstancia().actualizarDatos();
            return true;
        });
    }

}