package mx.edu.ittepic.ladm_u3_practica_martinbaez

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.cantidad
import kotlinx.android.synthetic.main.activity_main.celular
import kotlinx.android.synthetic.main.activity_main.descripcion
import kotlinx.android.synthetic.main.activity_main.domicilio
import kotlinx.android.synthetic.main.activity_main.entregado
import kotlinx.android.synthetic.main.activity_main.nombre
import kotlinx.android.synthetic.main.activity_main.precio
import kotlinx.android.synthetic.main.activity_main3.*
import kotlinx.android.synthetic.main.consulta.*
import kotlinx.android.synthetic.main.consulta.regresar

class Main3Activity : AppCompatActivity() {

    var id = ""
    var baseDatos = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        var extras = intent.extras

        id = extras!!.getString("id").toString()
        nombre.setText(extras.getString("nombre"))
        domicilio.setText(extras.getString("domicilio"))
        celular.setText(extras.getString("celular"))
        descripcion.setText(extras.getString("descripcion"))
        cantidad.setText(extras.getDouble("cantidad").toString())
        precio.setText(extras.getDouble("precio").toString())
        entregado.isChecked = extras.getBoolean("entregado")

        actualizar.setOnClickListener {
            baseDatos.collection("restaurante").document(id)
                .update("nombre", nombre.text.toString(),
                    "domicilio", domicilio.text.toString(),
                    "celular", celular.text.toString(),
                    "pedido.descripcion", descripcion.text.toString(),
                    "pedido.precio", precio.text.toString().toFloat(),
                    "pedido.cantidad", cantidad.text.toString().toInt(),
                    "pedido.entregado", entregado.isChecked)
                .addOnSuccessListener {
                    Toast.makeText(this,"Se actualizó correctamente", Toast.LENGTH_LONG).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this,"ERROR: No se puede actualizar por falta de RED", Toast.LENGTH_LONG).show()
                }
        }

        regresar.setOnClickListener {
            finish()
        }
    }
}
