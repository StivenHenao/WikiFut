package com.wikifut.app.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.wikifut.app.model.FavoriteTeam
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.wikifut.app.model.League
import com.wikifut.app.model.LigaResponse
import com.wikifut.app.model.Player

class FavoritesRepository(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) {
    private val userId: String? get() = auth.currentUser?.uid

    // ====== EQUIPOS ======
    suspend fun agregarEquipoAFavoritos(equipoFavorito: FavoriteTeam) {
        val uid = userId ?: return
        db.collection("favoritos")
            .document(uid)
            .collection("equipos")
            .document(equipoFavorito.team.id.toString())
            .set(equipoFavorito)
            .await()
    }

    suspend fun obtenerEquiposFavoritos(): List<FavoriteTeam> {
        val uid = userId ?: return emptyList()
        val snapshot = db.collection("favoritos")
            .document(uid)
            .collection("equipos")
            .get()
            .await()
        return snapshot.toObjects(FavoriteTeam::class.java)
    }

    suspend fun eliminarEquipoFavorito(teamId: Int) {
        val uid = userId ?: return
        db.collection("favoritos")
            .document(uid)
            .collection("equipos")
            .document(teamId.toString())
            .delete()
            .await()
    }

    // ====== JUGADORES ======
    suspend fun agregarJugadorAFavoritos(jugador: Player) {
        val uid = userId ?: return
        db.collection("favoritos")
            .document(uid)
            .collection("jugadores")
            .document(jugador.id.toString())
            .set(jugador)
            .await()
    }

    suspend fun obtenerJugadoresFavoritos(): List<Player> {
        val uid = userId ?: return emptyList()
        val snapshot = db.collection("favoritos")
            .document(uid)
            .collection("jugadores")
            .get()
            .await()
        return snapshot.toObjects(Player::class.java)
    }

    suspend fun eliminarJugadorFavorito(playerId: Int) {
        val uid = userId ?: return
        db.collection("favoritos")
            .document(uid)
            .collection("jugadores")
            .document(playerId.toString())
            .delete()
            .await()
    }

    // ====== LIGAS ======
    suspend fun agregarLigaAFavoritos(liga: League) {
        val uid = userId ?: return
        db.collection("favoritos")
            .document(uid)
            .collection("ligas")
            .document(liga.id.toString())
            .set(liga)
            .await()
    }

    suspend fun obtenerLigasFavoritas(): List<League> {
        val uid = userId ?: return emptyList()
        val snapshot = db.collection("favoritos")
            .document(uid)
            .collection("ligas")
            .get()
            .await()
        return snapshot.toObjects(League::class.java)
    }

    suspend fun eliminarLigaFavorita(leagueId: Int) {
        val uid = userId ?: return
        db.collection("favoritos")
            .document(uid)
            .collection("ligas")
            .document(leagueId.toString())
            .delete()
            .await()
    }
}
