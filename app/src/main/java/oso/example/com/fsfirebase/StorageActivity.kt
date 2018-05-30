package oso.example.com.fsfirebase

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_storage.*

class StorageActivity : AppCompatActivity() {

    private lateinit var direccion: Uri
    private lateinit var url: Uri
    private lateinit var name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storage)

        image_upload.setOnClickListener {
            val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            i.type = "image/*"
            startActivityForResult(i,1)
        }

        btn_upload.setOnClickListener { subirArchivo() }
    }

    private fun subirArchivo() {
        val instancia = FirebaseStorage.getInstance()
        val referencia = instancia.reference
        val img = referencia.child(name)

        img.putFile(direccion).addOnSuccessListener {
            Toast.makeText(this, "bien", Toast.LENGTH_SHORT).show()
            obtenerUrl(img)
        }.addOnFailureListener {
            Toast.makeText(this, "algo paso", Toast.LENGTH_SHORT).show()
        }
    }

    private fun obtenerUrl(img: StorageReference) {
        img.downloadUrl.continueWith {
            it.addOnCompleteListener { t ->
                Log.e("url", t.result.toString())
                url = t.result
//                Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/holaquehacexd-7aad2.appspot.com/o/FB_IMG_15272842773507970.jpg?alt=media&token=140da116-e811-4c15-8aa5-d50c4521c1d9").into(image_upload)
            }.addOnFailureListener {
                Log.e("url", it.message)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== Activity.RESULT_OK){
            if (data != null)
                direccion = data.data
            val ruta = direccion.lastPathSegment.toString()
            Log.e("ruta",ruta)
            name = direccion.lastPathSegment.toString().substring(ruta.lastIndexOf("/")+1)
            image_upload.setImageURI(direccion)
        }
    }
}
