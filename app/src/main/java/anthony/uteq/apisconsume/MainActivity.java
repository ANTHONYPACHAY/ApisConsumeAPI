package anthony.uteq.apisconsume;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.*;
import com.google.gson.JsonArray;

import org.json.JSONArray;

import anthony.uteq.apisconsume.models.IJournalApis;
import anthony.uteq.apisconsume.utiles.MyLogs;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Spinner cmdprovider;

    private Retrofit retrofit;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Se crea un spinner para filtrar el web service
        cmdprovider = (Spinner)findViewById(R.id.idprovider);
        //se configura y establece el adaptador al control
        ArrayAdapter<CharSequence> adaptador = ArrayAdapter.createFromResource(this, R.array.provop_a1, android.R.layout.simple_spinner_dropdown_item);
        cmdprovider.setAdapter(adaptador);

        //?j_id=2
        retrofit = new Retrofit.Builder()
                .baseUrl("https://revistas.uteq.edu.ec")
                //base url, es la url que no cambia al no depender de parámetros
                .addConverterFactory(GsonConverterFactory.create())
                //indicamos que el json lo convertirá utilizando la libreria GSON
                .build();//se termina la instancia

        //getData();
    }
    //List<Revista> allRev;
    private void getDataRetrofit(String param){
        //instancia a la interfaz que manejara las respuestas a las peticiones
        IJournalApis servicio =  retrofit.create(IJournalApis.class);
        //llamada al evento que ejecuta el web service
        Call <JsonArray> list2= servicio.getdataR(param);
        //funcion asincrona, en sus eventos se expecifica lo que debe hacer una vez se fulmina la búsqueda
        //el web service retorna un JsonArray
        list2.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                Log.e("mi_Retrofit", "count: " + response);
                //Obtener el cuerpo de la respuesta dentro de un JsonArray
                JsonArray jarr =response.body();
                Log.e("mi_Retrofit", "count: " + jarr.toString());
                //Se envia la información al activity destinado para visualización de resultados.
                sendData("Retrofit",param,jarr.toString());
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("mi_Retrofit", "onFailure: " + t.getMessage());
                Toast toast= Toast.makeText(MainActivity.this, "Error en Retrofit", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }

    private void getDataVolley(String param){
        queue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(
                Request.Method.GET,
                "https://revistas.uteq.edu.ec/ws/issues.php?j_id="+param,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        sendData("Volley",param,response.toString());
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast toast= Toast.makeText(MainActivity.this, "Error en Volley", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }
        );
        queue.add(request);
    }

    public void eventSearch(View view){
        Log.d("mi_Retrofit: ","Estoy aqui");
        //obtener los parámetros para realizar
        EditText j_id = (EditText) findViewById(R.id.txtidjournal);
        Spinner spinner = (Spinner) findViewById(R.id.idprovider);
        //obtener el texto de spinner (combo)
        String selected = spinner.getSelectedItem().toString();
        Log.d("mi_Retrofit: ","Seleccionado" + selected);
        //validar logitud para no buscar por gusto
        if(j_id.getText().toString().length() > 0){
            //validar los Consumirdores de la API
            if(selected.equals("Retrofit")){
                //llamada a api con Retrofit
                getDataRetrofit(j_id.getText().toString());
            }else{
                //llamada a api con volley
                getDataVolley(j_id.getText().toString());
            }
        }
        else{
            //toast
            Toast.makeText(this, "Ingrese un ID de artículo.", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendData(String provider, String journal, String message){
        //Creamos el Intent
        Log.d("estado: ","entro al evento");
        Intent intent = new Intent(MainActivity.this, Results.class);

        Log.d("estado: ","encontro el boton");
        //Creamos la información a pasar entre actividades - Pares Key-Value
        Bundle b = new Bundle();

        b.putString("provider", provider);
        b.putString("journal", journal);
        b.putString("response", message);
        //se agregan los parámetros
        intent.putExtras(b);
        Log.d("estado: ","llegue al final de este evento");
        //iniciamos la nuva actividad
        startActivity(intent);
    }
}