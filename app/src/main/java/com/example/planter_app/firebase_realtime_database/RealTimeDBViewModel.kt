package com.example.planter_app.firebase_realtime_database

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File

class RealTimeDBViewModel : ViewModel() {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    private val user = FirebaseAuth.getInstance().currentUser

    fun savePlantData(
        uri: String,
        disease: String,
        treatment: String,
        date: String,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val fileUri = Uri.parse(uri)
        val file = File(fileUri.path!!)

        if (file.exists()) {
            val imageUri = Uri.fromFile(file)

            if (user != null) {
                val userId = user.uid
                viewModelScope.launch(Dispatchers.IO) {
                    val storageReference = storage.reference
                    val imageRef = storageReference.child("images/${imageUri.lastPathSegment}")
                    val uploadTask = imageRef.putFile(imageUri)

                    uploadTask.addOnSuccessListener {
                        imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                            val contactsRef =
                                database.reference.child("users").child(userId).child("MyPlants")
                            val newContactRef = contactsRef.push()
                            val contact =
                                Plant(
                                    newContactRef.key!!,
                                    downloadUrl.toString(),
                                    disease,
                                    treatment,
                                    date
                                )

                            newContactRef.setValue(contact)
                            onSuccess("Plant saved successfully!")
                        }
                    }.addOnFailureListener {
                        onFailure(it)
                        Log.i("TAG", "savePlantData: Failed: $it ")
                    }
                }
            } else {
                Log.e("TAG", "No authenticated user found.")
                onFailure(Exception("No authenticated user found."))
            }
        } else {
            Log.e("TAG", "File does not exist at URI: $fileUri")
            onFailure(Exception("File does not exist at URI: $fileUri"))
        }
    }


    fun fetchPlantData(
        onSuccess: (List<Plant>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            try {
                if (user != null) {
                    val userId = user.uid
                    val myRef = database.reference.child("users").child(userId).child("MyPlants")
                    val dataSnapshot = myRef.get().await()
                    val plants = dataSnapshot.children.map { child ->
                        val key = child.key ?: ""
                        val imageUrl = child.child("image").getValue(String::class.java) ?: ""
                        val disease = child.child("disease").getValue(String::class.java) ?: ""
                        val treatment = child.child("treatment").getValue(String::class.java) ?: ""
                        val date = child.child("date").getValue(String::class.java) ?: ""
                        Plant(key, imageUrl, disease, treatment,date)
                    }
                    onSuccess(plants)
                } else {
                    onFailure(Exception("No authenticated user found."))
                }
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }


    fun deletePlantData(key: String, onSuccess: (String) -> Unit, onFailure: (String) -> Unit){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (user != null) {
                    val userId = user.uid
                    val plantRef = database.reference.child("users").child(userId).child("MyPlants").child(key)
                    plantRef.removeValue().await()
                    onSuccess("Plant Deleted Successfully")
                    Log.i("TAG", "deletePlantData: Plant Deleted Successfully")
                } else {
                    onFailure("User not authenticated")
                }
            } catch (e: Exception) {
                onFailure("Failed to delete: error -> $e")
                Log.i("TAG", "Failed to delete: key passed : $key,   error -> $e")

            }
        }
    }
}