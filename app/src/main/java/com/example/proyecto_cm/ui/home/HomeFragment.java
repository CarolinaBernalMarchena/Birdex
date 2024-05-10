package com.example.proyecto_cm.ui.home;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.proyecto_cm.R;
import com.example.proyecto_cm.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

        // Inflar el diseño del fragmento
        FragmentHomeBinding binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Obtener referencia al TextView dentro del diseño del fragmento
        FirebaseDatabase database = FirebaseDatabase.getInstance("urlDataBase");
        DatabaseReference myRef = database.getReference("Pajaros");


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Este método se llama cada vez que hay un cambio en los datos
                int indice = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Iterar sobre los hijos de la referencia

                    String nombre = snapshot.child("nombre").getValue(String.class);
                    String url = snapshot.child("url").getValue(String.class);
                    String habitat = snapshot.child("habitat").getValue(String.class);
                    String descripcion = snapshot.child("descripcion").getValue(String.class);

                    //creamos la imagen
                    LinearLayout linearLayout = root.findViewById(R.id.linearLayout);

                    // Crear un nuevo ImageView
                    ImageView imageView = new ImageView(getContext());
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,  // Ancho
                            ViewGroup.LayoutParams.WRAP_CONTENT // Alto
                    ));
                    imageView.setImageResource(R.drawable.bird); // Establecer la imagen
                    imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE); // Escala de la imagen
                    imageView.setAdjustViewBounds(true); // Ajustar límites de visualización

                    // Agregar el ImageView al LinearLayout existente
                    linearLayout.addView(imageView);


                    Glide.with(requireContext())
                            .load(url)
                            .apply(new RequestOptions().placeholder(R.drawable.bird)) // Establece una imagen de carga mientras se carga la imagen
                            .into(imageView);

                    // Crear un nuevo TextView
                    TextView textView = new TextView(getContext());
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,  // Ancho
                            LinearLayout.LayoutParams.WRAP_CONTENT // Alto
                    );
                    layoutParams.setMargins(0,10,0,50);
                    textView.setLayoutParams(layoutParams);
                    textView.setText(nombre); // Establecer el texto
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16); // Tamaño del texto en sp
                    textView.setTextColor(Color.BLACK); // Color del texto
                    textView.setGravity(Gravity.CENTER); // Alineación del texto
                    Typeface typeface = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD ); //fuente del texto
                    textView.setTypeface(typeface);
                    linearLayout.addView(textView);


                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) { // Ir a otro fragment al hacer clic en la imagen
                            // Crear el Bundle con los parámetros
                            Bundle bundle = new Bundle();
                            bundle.putString("nombre", nombre);
                            bundle.putString("habitat", habitat);
                            bundle.putString("descripcion", descripcion);
                            bundle.putString("url", url);

                            NavController navController = Navigation.findNavController(v);
                            navController.navigate(R.id.action_fragmentHome_to_fragmentDetails, bundle);
                        }
                    });

                    indice++;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}