package com.example.proyecto_cm;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.proyecto_cm.databinding.FragmentHomeBinding;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashSet;
import java.util.Set;

public class DetailsFragment extends Fragment {

    private DetailsViewModel mViewModel;

    public static DetailsFragment newInstance() {
        return new DetailsFragment();
    }

    public String nombreDetalles;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        // Recibe los parámetros
        Bundle bundle = getArguments();

        if (bundle != null) {
            Log.d("myTag", bundle.toString());

            String nombre = bundle.getString("nombre");
            nombreDetalles=nombre;
            String habitat = bundle.getString("habitat");
            String descripcion = bundle.getString("descripcion");
            String url = bundle.getString("url");

            // Usa los parámetros como necesites
            TextView nombreTextView = view.findViewById(R.id.nombre);
            TextView habitatTextView = view.findViewById(R.id.habitat);
            TextView descripcionTextView = view.findViewById(R.id.descripcion);
            ImageView urlImageView = view.findViewById(R.id.url);


            nombreTextView.setText(nombre);
            habitatTextView.setText("Hábitat: " + habitat);
            descripcionTextView.setText(descripcion);

            Glide.with(requireContext())
                    .load(url)
                    .apply(new RequestOptions().placeholder(R.drawable.bird).dontTransform()) // Establece una imagen de carga mientras se carga la imagen
                    .into(urlImageView);
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        //checkbox pájaro visto
        CheckBox checkBox = view.findViewById(R.id.checkBox);

        // comprobamos en localstorage si el pájaro ha sido o no marcado como visto
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        Set<String> pajarosVistos = sharedPref.getStringSet("pajaros_vistos", new HashSet<>());
        // si no hay lista de pájaros entonces el pájaro no está marcado como visto
        if (pajarosVistos.size()==0){
            Log.d("[checkbox] inicializamos vacío", pajarosVistos.toString());
            checkBox.setChecked(false);
            // miramos si en la lista de pajaros está este pajaro
        } else if (pajarosVistos.contains(nombreDetalles)) {
            checkBox.setChecked(true);  //si está el pájaro lo marcamos como visto
            Log.d("[checkbox] inicializamos visto", pajarosVistos.toString());
        }else {
            checkBox.setChecked(false);
            Log.d("[checkbox] inicializamos no visto", pajarosVistos.toString());
        }

        checkBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Set<String> pajarosVistos2 = sharedPref.getStringSet("pajaros_vistos", new HashSet<>());
                Log.d("check", String.valueOf(checkBox.isChecked()));
                if(checkBox.isChecked()){
                    if (pajarosVistos2.size()== 0) { // mirar si hay una lista de pajaros
                        //Si no la hay, crear una lista con este pajaro y meterlo en local storage
                        Set<String> nuevaListaPajaros = new HashSet<>();
                        nuevaListaPajaros.add(nombreDetalles);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putStringSet("pajaros_vistos", nuevaListaPajaros);
                        editor.apply();
                        Log.d("[checkbox] no existia lista o lista vacia", nuevaListaPajaros.toString());

                    } else if (!pajarosVistos2.contains(nombreDetalles)) { //si sí la hay añadir este pajaro a la lista y meter la lista en localstorage
                        Set<String> copiaListaPajaros = new HashSet<>(pajarosVistos2);
                        copiaListaPajaros.add(nombreDetalles);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putStringSet("pajaros_vistos", copiaListaPajaros);
                        editor.apply();
                        Log.d("[checkbox] existe lista y añadimos el pajaro", copiaListaPajaros.toString());
                    }
                }else{
                    Set<String> quitamosPajaro = new HashSet<>(pajarosVistos2);
                    quitamosPajaro.remove(nombreDetalles);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putStringSet("pajaros_vistos", quitamosPajaro);
                    editor.apply();
                    Log.d("[checkbox] quitamos pájaro", quitamosPajaro.toString());

                }
            }
        });

        //Notas del pájaro
        // coger valor de nombreDetalles+"_notas" y meterlo en el inputtext de la nota
        String notaLocal = sharedPref.getString(nombreDetalles+"_notas", "sin notas");
        setTextoNota(notaLocal, view);

        //botón para añadir las notas, modifica el valor en local
        TextView textoNotas = view.findViewById(R.id.nota);
        Button button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPref.edit();
                String newNota = textoNotas.getText().toString();
                editor.putString(nombreDetalles+"_notas",newNota );
                editor.apply();
            }
        });


        return view;
    }

    //cambiar el texto de la nota
    public void setTextoNota(String textoNotaNuevo, View view){
        TextView textoNotas = view.findViewById(R.id.nota);
        textoNotas.setText(textoNotaNuevo);
    }




}