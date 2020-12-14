package com.example.imdbviewer.data.network.firebase

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.core.net.toFile
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import java.util.*

object FirebaseStorageUtil {
    private val TAG = "aminjoon"
    private val storageInstance by lazy {
        FirebaseStorage.getInstance()
    }

    private val curUserRef: StorageReference
        get() = storageInstance.reference.child(FirebaseAuthUtil.userId)


    suspend fun uploadProfilePhoto(
        imageUri: Uri,
        onComplete: (imagePath: String?, throwable: Throwable?) -> Unit
    ) {
        val ref = curUserRef.child("profilePictures/${UUID.randomUUID()}")

        try {
            ref.putFile(imageUri).await()
            onComplete(ref.path, null)
        } catch (t: Throwable) {
            onComplete(null, t)
        }
    }


    suspend fun getPhotoPath(path: String?):Uri? {
        if (path == null) return null
        return storageInstance.getReference(path).downloadUrl.await()
        //val link= storageInstance.getReferenceFromUrl(path).downloadUrl.await()
    }
}