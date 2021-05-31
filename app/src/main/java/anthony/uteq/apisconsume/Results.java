package anthony.uteq.apisconsume;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

public class Results extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);



        TextView txtproveedor = (TextView)findViewById(R.id.txtresultprovider);
        TextView textsearch = (TextView)findViewById(R.id.txttextsearch);
        TextView results = (TextView)findViewById(R.id.txtresponse);
        //asignar evento de movimiento
        results.setMovementMethod(new ScrollingMovementMethod());

        //Creamos la información a pasar entre actividades - Pares Key-Value
        Bundle bundle = this.getIntent().getExtras();
        //obtenermos lo valores pasados por el otro activity y los asignamos
        txtproveedor.setText("Estos datos fueron proporcionador por :" + bundle.getString("provider"));
        textsearch.setText("Resultados para búsqeda de publicaciones ["+ bundle.getString("journal")+ "]");
        results.setText(bundle.getString("response"));
        //Resultados de búsqueda

    }
    public void back(View view){
        onBackPressed();
    }
}