package com.example.imdbviewer.util

//Not the correct way, but not important for now
fun String.mapToYear():String{
    if (isNullOrBlank()) return ""
    return if (length>4){
        substring(0,4)
    }else{
        this
    }
}

