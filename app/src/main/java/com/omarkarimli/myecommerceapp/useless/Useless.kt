package com.omarkarimli.myecommerceapp.useless

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.omarkarimli.myecommerceapp.R

class Useless(val context: Context) {
    private fun writeProductsToFirestore() {
        val firestore = FirebaseFirestore.getInstance()

        // Step 1: Read the JSON file from the raw resource
        val inputStream = context.resources.openRawResource(R.raw.products)
        val jsonString = inputStream.bufferedReader().use { it.readText() }

        // Step 2: Parse the JSON string into a list of maps or objects
        val gson = Gson()
        val productListType = object : TypeToken<List<Map<String, Any>>>() {}.type
        val products: List<Map<String, Any>> = gson.fromJson(jsonString, productListType)

        // Step 3: Add each product to Firestore
        for (product in products) {
            firestore.collection("products")
                .add(product)
                .addOnSuccessListener {
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                    Log.d("111", "Success")
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show()
                    Log.d("111", it.message.toString())
                }
        }
    }

    private fun writeCategoriesToFirestore() {
        val firestore = FirebaseFirestore.getInstance()

        // Step 1: Read the JSON file from the raw resource
        val inputStream = context.resources.openRawResource(R.raw.categories)
        val jsonString = inputStream.bufferedReader().use { it.readText() }

        // Step 2: Parse the JSON string into a list of maps or objects
        val gson = Gson()
        val categoriesListType = object : TypeToken<List<Map<String, Any>>>() {}.type
        val categories: List<Map<String, Any>> = gson.fromJson(jsonString, categoriesListType)

        // Step 3: Add each category to Firestore
        for (category in categories) {
            firestore.collection("categories")
                .add(category)
                .addOnSuccessListener {
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                    Log.d("111", "Success")
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show()
                    Log.d("111", it.message.toString())
                }
        }
    }
}