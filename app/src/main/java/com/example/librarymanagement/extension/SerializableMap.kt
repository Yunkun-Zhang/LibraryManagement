package com.example.librarymanagement.extension
import  java.io.Serializable


class SerializableMap: Serializable {
    val map: HashMap<Int, MutableList<Int>> = hashMapOf()
}