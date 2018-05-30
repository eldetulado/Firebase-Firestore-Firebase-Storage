package oso.example.com.fsfirebase

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_joker.view.*
import oso.example.com.fsfirebase.R.id.rv_list_joke

class MainActivity : AppCompatActivity() {

    private val listJoker:ArrayList<People> = ArrayList()
    private val adapter = PeopleAdapter(listJoker,this,R.layout.item_joker)
    private lateinit var db:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_list_joke.layoutManager = GridLayoutManager(this,2)
        rv_list_joke.adapter = adapter

        db = FirebaseFirestore.getInstance()

        fab_button.setOnClickListener {
//            showDialog()
            val i = Intent(this, StorageActivity::class.java)
            startActivity(i)
            finish()
        }

        loadAllData(db)

//        deleteUser(db)
    }

    private fun showDialog() {
        val build = AlertDialog.Builder(this)
        val alertDialog = build.create()
        val inf = layoutInflater
        val view = inf.inflate(R.layout.dialog_joker,null,false)
        val contentJoker = view.user_content_joker_dialog
        val name = view.user_name_dialog
        val btnSend = view.button_send
        btnSend.setOnClickListener {
            if (!contentJoker.text.toString().trim().isEmpty()
                    && !name.text.toString().trim().isEmpty()){
                insertData(db,name.text.toString().trim(),contentJoker.text.toString().trim())
                alertDialog.dismiss()
            }
        }

        alertDialog.setView(view)
        alertDialog.show()
    }

    private fun insertData(db: FirebaseFirestore, name: String, content: String) {
        val temp = People(name,content)
        db.collection("chistes").document(name).set(temp)
                .addOnSuccessListener {
                    Toast.makeText(this,"Insertado exitosamente",Toast.LENGTH_SHORT).show()
                    loadAllData(db)
                }.addOnFailureListener {
                    Toast.makeText(this,"Sucedio un error",Toast.LENGTH_SHORT).show()
                }

    }

    override fun onStart() {
        super.onStart()
        listenData(db)
    }

    private fun deleteUser(db: FirebaseFirestore) {
        db.collection("chistes").document("maria")
                .delete().addOnSuccessListener {
                    Toast.makeText(this, "eliminado", Toast.LENGTH_SHORT).show()
                    adapter.notifyDataSetChanged()
                }.addOnFailureListener {
                    Toast.makeText(this, "error al eliminar", Toast.LENGTH_SHORT).show()
                }
    }

    private fun listenData(db: FirebaseFirestore) {
        db.collection("chistes").addSnapshotListener {
            querySnapshot, firebaseFirestoreException ->

            if (firebaseFirestoreException != null) {
                Toast.makeText(this, "error al escuchar", Toast.LENGTH_SHORT).show()
            }

            listJoker.clear()
            querySnapshot?.forEach {
                val name = it.getString("name")
                val content = it.getString("content")
                if (name != null && content != null)
                    listJoker.add(People(name, content))
            }
            adapter.notifyDataSetChanged()
        }
    }

    private fun loadAllData(db: FirebaseFirestore) {
        db.collection("chistes")
                .get()
                .addOnCompleteListener {
            if (it.isSuccessful) {
                listJoker.clear()
                for (document in it.result) {
                    Log.e("datos", document.id + " => " + document.data)
                    val name = document.getString("name")
                    val content = document.getString("content")
                    if (name != null && content != null)
                        listJoker.add(People(name, content))
                }
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this, "error de codigo", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
