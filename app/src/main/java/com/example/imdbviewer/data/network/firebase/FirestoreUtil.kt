package com.example.imdbviewer.data.network.firebase

import android.util.Log
import com.example.imdbviewer.data.cache.models.ListItemEntity
import com.example.imdbviewer.data.network.firebase.FirebaseAuthUtil.userId
import com.example.imdbviewer.data.network.firebase.model.FavoriteItemDto
import com.example.imdbviewer.domain_models.TmdbListItem
import com.example.imdbviewer.domain_models.User
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

object FirestoreUtil {
    private val TAG = "aminjoon"
    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }


     val curUserDocRef: DocumentReference
        get() = firestore.document("users/$userId")

    private val favoritesCollectionRef:CollectionReference
    get() = firestore.collection("favorites").document("users").collection(userId)



    suspend fun insertIntoFavorites(tmdbListItem: FavoriteItemDto){
        favoritesCollectionRef.document(tmdbListItem.id.toString())
            .set(
                tmdbListItem.apply {
                    timeSynced=System.currentTimeMillis()
                                   },
                SetOptions.merge()
            )
            .await()
    }


    suspend fun removeFromFavorites(itemId:Int){
        val documentSnapshot= favoritesCollectionRef.document(itemId.toString()).get().await()
        if (documentSnapshot.exists()){
            favoritesCollectionRef.document(itemId.toString()).delete().await()
        }
    }

    suspend fun getSyncedFavorites(): List<FavoriteItemDto> {
        val querySnapshot=favoritesCollectionRef.get().await()
        return querySnapshot.map { it.toObject(FavoriteItemDto::class.java) }
    }



    suspend fun initCurUserIfFirstTime() {
        val documentSnapshot = curUserDocRef.get().await()
        if (!documentSnapshot.exists()) {
            val newUser = User(name = "user ${userId.subSequence(0, 4)}", profilePicturePath = null)
            curUserDocRef.set(newUser).await()
        }
    }

    suspend fun updateCurUser(name: String = "", profilePicturePath: Any? = null) {
        val userFileMap = mutableMapOf<String, Any>()

        if (name.trim().isNotBlank()) {
            userFileMap["name"] = name
        }
        profilePicturePath?.let {
            userFileMap["profilePicturePath"] = profilePicturePath
        }
        curUserDocRef.update(userFileMap).await()

    }


}