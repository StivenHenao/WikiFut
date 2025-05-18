package com.wikifut.app.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.wikifut.app.model.FavoriteTeam
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FavoritesRepository  @Inject constructor(

) {
    private val userId: String? get() = auth.currentUser?.uid
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun agregarAFavoritos(equipoFavorito: FavoriteTeam) {
        val uid = userId ?: return
        db.collection("favoritos")
            .document(uid)
            .collection("equipos")
            .document(equipoFavorito.team.id.toString())
            .set(equipoFavorito) // Guarda el objeto completo
            .await()
    }

    suspend fun obtenerFavoritos(): List<FavoriteTeam> {
        val uid = userId ?: return emptyList()
        val snapshot = db.collection("favoritos")
            .document(uid)
            .collection("equipos")
            .get()
            .await()

        return snapshot.toObjects(FavoriteTeam::class.java)
    }

    suspend fun eliminarFavorito(teamId: Int) {
        val uid = userId ?: return
        db.collection("favoritos")
            .document(uid)
            .collection("equipos")
            .document(teamId.toString())
            .delete()
            .await()
    }
}
