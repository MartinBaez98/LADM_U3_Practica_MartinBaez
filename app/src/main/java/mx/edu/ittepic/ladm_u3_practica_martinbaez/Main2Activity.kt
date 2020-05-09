package mx.edu.ittepic.ladm_u3_practica_martinbaez

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.consulta.*

class Main2Activity : AppCompatActivity() {

    var baseDatos = FirebaseFirestore.getInstance()
    var dataLista = ArrayList<String>()
    var listaID = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.consulta)

        buscar.setOnClickListener {
            buscarRegistro(consulta.text.toString(), clave.selectedItemPosition)
        }

        regresar.setOnClickListener {
            finish()
        }

        baseDatos.collection("restaurante")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if(firebaseFirestoreException != null){
                    //Si es diferente de null, hay un error
                    Toast.makeText(this, "ERROR: No se puede acceder a consulta", Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }
                dataLista.clear()
                listaID.clear()
                for( document in querySnapshot!! ){
                    var cadena = document.getString("nombre")+("\n")+
                            document.getString("pedido.descripcion")+("\n")+
                            document.getDouble("pedido.precio")+("\n")+
                            document.getDouble("pedido.cantidad")+("\n")
                    dataLista.add(cadena)
                    listaID.add(document.id)
                }
                if(dataLista.size == 0){
                    dataLista.add("No hay datos")
                }
                var adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataLista)
                lista.adapter = adapter
            }

        lista.setOnItemClickListener { parent, view, position, id ->
            if(listaID.size == 0){
                return@setOnItemClickListener
            }
            AlertaEliminar(position)
        }

    }

    private fun AlertaEliminar(position: Int){
        AlertDialog.Builder(this).setTitle("ATENCIÓN")
            .setMessage("¿Qué desea hacer con el registro? \n ${dataLista[position]}")
            .setPositiveButton("Eliminar"){d,w ->
                eliminar(listaID[position])
            }
            .setNegativeButton("Actualizar"){d,w ->
                llamarVentanaActualizar(listaID[position])
            }
            .setNeutralButton("Cancelar"){dialog, wich ->

            }.show()
    }

    private fun llamarVentanaActualizar(idActualizar: String) {
        baseDatos.collection("restaurante").document(idActualizar)
            .get()
            .addOnSuccessListener {
                var a = Intent(this, Main3Activity::class.java)
                a.putExtra("id", idActualizar)
                a.putExtra("nombre", it.getString("nombre"))
                a.putExtra("domicilio", it.getString("domicilio"))
                a.putExtra("celular", it.getString("celular"))
                a.putExtra("descripcion", it.getString("pedido.descripcion"))
                a.putExtra("precio", it.getDouble("pedido.precio"))
                a.putExtra("cantidad", it.getDouble("pedido.cantidad"))
                a.putExtra("entregado", it.getBoolean("pedido.entregado"))

                startActivity(a)
            }
            .addOnFailureListener {
                Toast.makeText(this,"ERROR: No hay conexión de RED",Toast.LENGTH_LONG).show()
            }
    }

    private fun eliminar(idEliminar: String) {
        baseDatos.collection("restaurante").document(idEliminar).delete()
            .addOnSuccessListener {
                Toast.makeText(this,"Se eliminó con éxito",Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(this,"No se pudo eliminar",Toast.LENGTH_LONG).show()
            }
    }

    private fun buscarRegistro(valor: String, clave: Int) {
        when(clave) {
            0 -> {
                consultaNombre(valor)
            }
            1 -> {
                consultaDesc(valor)
            }
        }
    }

    private fun consultaDesc(valor: String) {
        baseDatos.collection("restaurante").whereEqualTo("pedido.descripcion", valor)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if(firebaseFirestoreException != null){
                    resultado.setText("ERROR: No hay conexión")
                    return@addSnapshotListener
                }

                var res = ""
                for (document in querySnapshot!!){
                    res += "Nombre: "+document.getString("nombre")+
                            "\nDomicilio: "+document.getString("domicilio")+
                            "\nCelular: "+document.getString("celular")+
                            "\nDescripción: "+document.getString("pedido.descripcion")+
                            "\nPrecio: "+document.getDouble("pedido.precio")+
                            "\nCantidad: "+document.getDouble("pedido.cantidad")+
                            "\nEstado: "+document.getBoolean("pedido.entregado")+"\n"
                }

                resultado.setText(res)
            }
    }

    private fun consultaNombre(valor: String) {
        baseDatos.collection("restaurante").whereEqualTo("nombre", valor)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if(firebaseFirestoreException != null){
                    resultado.setText("ERROR: No hay conexión")
                    return@addSnapshotListener
                }

                var res = ""
                for (document in querySnapshot!!){
                    res += "Nombre: "+document.getString("nombre")+
                            "\nDomicilio: "+document.getString("domicilio")+
                            "\nCelular: "+document.getString("celular")+
                            "\nDescripción: "+document.getString("pedido.descripcion")+
                            "\nPrecio: "+document.getDouble("pedido.precio")+
                            "\nCantidad: "+document.getDouble("pedido.cantidad")+
                            "\nEstado: "+document.getBoolean("pedido.entregado")
                }

                resultado.setText(res)
            }
    }
}
