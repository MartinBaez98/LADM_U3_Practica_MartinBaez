package mx.edu.ittepic.ladm_u3_practica_martinbaez

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var baseRemota =FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        agregar.setOnClickListener {
            agregarPedido()
        }

        consultar.setOnClickListener {
            var v = Intent(this, Main2Activity::class.java)
            startActivity(v)
        }

    }

    private fun agregarPedido() {
        var datos = hashMapOf(
            "nombre" to nombre.text.toString(),
            "domicilio" to domicilio.text.toString(),
            "celular" to celular.text.toString(),
            "pedido" to hashMapOf(
                "descripcion" to descripcion.text.toString(),
                "precio" to precio.text.toString().toFloat(),
                "cantidad" to cantidad.text.toString().toInt(),
                "entregado" to entregado.isChecked
            )
        )

        baseRemota.collection("restaurante").add(datos)
            .addOnSuccessListener {
                Toast.makeText(this,"Capturado correctamente", Toast.LENGTH_LONG).show()
                limpiarCampos()
            }
            .addOnFailureListener {
                Toast.makeText(this,"No se completó el registro", Toast.LENGTH_LONG).show()
            }
    }

    private fun limpiarCampos(){
        nombre.setText("")
        domicilio.setText("")
        celular.setText("")
        descripcion.setText("")
        precio.setText("")
        cantidad.setText("")
        entregado.isChecked = false
    }

}
