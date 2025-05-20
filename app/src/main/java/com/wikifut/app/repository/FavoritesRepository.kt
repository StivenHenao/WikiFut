package com.wikifut.app.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.wikifut.app.model.FavoriteTeam
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FavoritesRepository(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) {
    private val userId: String? get() = auth.currentUser?.uid
    //private val auth = FirebaseAuth.getInstance()
    //private val db = FirebaseFirestore.getInstance()

    suspend fun agregarAFavoritos(equipoFavorito: FavoriteTeam) {
        val uid = userId ?: return
        val email = auth.currentUser?.email ?: return

        // Buscar el username del usuario actual desde la colección "users"
        val userDoc = db.collection("users")
            .document(email)
            .get()
            .await()

        val username = userDoc.getString("username") ?: "Desconocido"

        // Crear una copia del equipo con el userId y userName
        //val favoritoConUsuario = equipoFavorito.copy(
            //userId = uid,
            //userName = username
        //)

        db.collection("favoritos")
            .document(uid)
            .collection("equipos")
            .document(equipoFavorito.team.id.toString())
            .set(equipoFavorito)
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
