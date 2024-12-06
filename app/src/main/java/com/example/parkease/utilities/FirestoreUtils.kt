package com.example.parkease.utilities

import com.google.firebase.firestore.FirebaseFirestore

// Fetch data fields
// This is a generic function
fun <T : Any> fetchDocument(
    collectionName: String,
    documentId: String,
    type: Class<T>,
    onSuccess: (T?) -> Unit,
    onFailure: (Exception) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    db.collection(collectionName).document(documentId).get()
        .addOnSuccessListener { document ->
            if (document.exists()) {
                val fields = document.toObject(type)
                onSuccess(fields)
            } else {
                onFailure(Exception("Document does not exist"))
            }
        }
        .addOnFailureListener { exception ->
            onFailure(exception)
        }
}

// Fetch all documents in a collection
fun fetchCollection(
    collectionName: String,
    onSuccess: (List<Map<String, Any>>) -> Unit,
    onFailure: (Exception) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    db.collection(collectionName).get()
        .addOnSuccessListener { result ->
            val documents = result.map { it.data }
            onSuccess(documents)
        }
        .addOnFailureListener { exception ->
            onFailure(exception)
        }
}


fun <T : Any> createDocumentWithFields(
    collectionName: String,
    documentName: String,
    fields: T, // Fields to store in the document
    onSuccess: (Boolean) -> Unit, // Callback for success, returning true
    onFailure: (Exception) -> Unit // Callback for failure
) {
    val db = FirebaseFirestore.getInstance()
    db.collection(collectionName).document(documentName).set(fields)
        .addOnSuccessListener {
            onSuccess(true) // Document created successfully
        }
        .addOnFailureListener { exception ->
            onFailure(exception) // Handle failure
        }
}