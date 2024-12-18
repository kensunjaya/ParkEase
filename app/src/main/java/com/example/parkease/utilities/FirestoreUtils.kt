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
fun <T : Any> fetchCollection(
    collectionName: String,
    type: Class<T>,
    onSuccess: (List<T?>) -> Unit,
    onFailure: (Exception) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    db.collection(collectionName).get()
        .addOnSuccessListener { result ->
            val documents = result.documents.map { document ->
                document.toObject(type)
            }
            onSuccess(documents)
        }
        .addOnFailureListener { exception ->
            onFailure(exception)
        }
}


fun <T : Any> createDocumentWithFields(
    collectionName: String,
    documentName: String,
    fields: T,
    onSuccess: (Boolean) -> Unit,
    onFailure: (Exception) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    db.collection(collectionName).document(documentName).set(fields)
        .addOnSuccessListener {
            onSuccess(true)
        }
        .addOnFailureListener { exception ->
            onFailure(exception)
        }
}

fun <T> editDocumentField(
    collectionName: String,
    documentName: String,
    field: String,
    value: T,
    onSuccess: (Boolean) -> Unit,
    onFailure: (Exception) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    db.collection(collectionName).document(documentName).update(field, value)
        .addOnSuccessListener {
            onSuccess(true)
        }
        .addOnFailureListener { exception ->
            onFailure(exception)
        }
}