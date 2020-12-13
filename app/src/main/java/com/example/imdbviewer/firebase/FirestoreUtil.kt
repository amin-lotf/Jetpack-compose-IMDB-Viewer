package com.example.imdbviewer.firebase

import android.net.Uri
import android.util.Log
import com.example.imdbviewer.firebase.FirebaseAuthUtil.userId
import com.example.imdbviewer.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FirestoreUtil {
    private val TAG="aminjoon"
    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }


    private val curUserDocRef: DocumentReference
        get() = firestore.document("users/$userId")



    suspend fun initCurUserIfFirstTime(onComplete:(Throwable?)->Unit){
        try {
            val documentSnapshot=curUserDocRef.get().await()
            if(!documentSnapshot.exists()){
                val newUser=User(name = "user ${userId.subSequence(0,4)}",profilePicturePath = null)
                curUserDocRef.set(newUser).await()
            }
            onComplete(null)
        }catch (t:Throwable){
            onComplete(t)
        }
    }

     suspend fun updateCurUser(name:String="",profilePicturePath:Any?=null,onComplete:(Throwable?)->Unit){
        val userFileMap= mutableMapOf<String,Any>()

        if (name.trim().isNotBlank()){
            userFileMap["name"]=name
        }

        profilePicturePath?.let {
            userFileMap["profilePicturePath"]=profilePicturePath
        }
        try {
            curUserDocRef.update(userFileMap).await()
            onComplete(null)
        }catch (t:Throwable){
            onComplete(t)
        }
    }


    suspend fun getCurrentUser(onComplete: (User?,Throwable?) -> Unit){
        try {
            val user = curUserDocRef.get().await().toObject(User::class.java)
            user?.profilePicturePath?.let {
                FirebaseStorageUtil.getPhotoPath(it.toString()){link,throwable->
                    link?.let {
                        Log.d(TAG, "getCurrentUser: $it")
                        onComplete(user.copy(profilePicturePath = it),null)

                    }
                    throwable?.printStackTrace()

                }
            }


        }catch (t:Throwable){
            onComplete(null,t)
        }
    }

}