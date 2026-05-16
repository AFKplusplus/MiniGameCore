package ore.MiniGameCore.api

// Bukkit imports
import org.bukkit.Location
import org.bukkit.entity.Player

// MiniGameCore imports
import ore.MiniGameCore.managers.GameManager
import ore.MiniGameCore.managers.LobbyManager
import ore.MiniGameCore.utils.Lobby
import ore.MiniGameCore.utils.Team
import ore.MiniGameCore.utils.Winner

// Import java
import java.util.UUID

public class MiniGameCoreAPI {

    // Used to turn all variables and functions static
    companion object {

        // Lobby manager variable
        private val lobbyManager: LobbyManager = LobbyManager.getInstance()

        // Get lobby manager
        public fun getLobbyManager(): LobbyManager {
            return lobbyManager
        }

        // Make a player win
        public fun winPlayer(lobby: Lobby, player: Player) {
            GameManager.endGame(lobby, Winner.PlayerWinner(player))
        }

        // Make a team win
        public fun winTeam(lobby: Lobby, team: Team) {
            GameManager.endGame(lobby, Winner.TeamWinner(player))
        }

        // Kills a player
        public fun playerDeath(playerID: UUID) {
            GameManager.playerDeath(playerID)
        }

        // Revives a player
        public fun playerAlive(playerID: UUID) {
            GameManager.playerAlive(playerID)
        }

        // Get the respawn location of a player
        public fun getRespawnLocation(playerID: UUID): Location {
            return GameManager.getRespawnPoint(playerID)
        }

    }

}