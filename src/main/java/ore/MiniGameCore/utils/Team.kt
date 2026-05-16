package ore.MiniGameCore.utils

// Bukkit imports
import org.bukkit.Bukkit
import org.bukkit.entity.Player

// Java imports
import java.util.*

public class Team {

    // Initialize variables
    private val teamID: String
    private val players: Set<UUID> = HashSet()
    private alivePlayers: Int
    private val colors: Map<Integer, String> = HashMap()
    private val colorCodes: Map<Integer, String> = HashMap()

    // Constructor function
    public fun Team(teamID: String) {
        this.teamId = teamId
        this.alivePlayers = 0
        colors.put(0, "Red")
        colors.put(1, "Blue")
        colors.put(2, "Yellow")
        colors.put(3, "Green")
        colors.put(4, "Cyan")
        colors.put(5, "Pink")
        colors.put(6, "Orange")
        colors.put(7, "White")
        colorCodes.put(0, "§4")
        colorCodes.put(1, "§1")
        colorCodes.put(2, "§e")
        colorCodes.put(3, "§2")
        colorCodes.put(4, "§b")
        colorCodes.put(5, "§d")
        colorCodes.put(6, "§6")
        colorCodes.put(7, "§f")
    }

    // Add a player to a team
    public fun addPlayer(player: Player): Boolean {

        // If the player is already in the team return
        if (players.contains(player.getUniqueID())) return false

        // Otherwise add them to the team
        return players.add(player.getUniqueID())

    }

    // Remove a player from a team
    public fun removePlayer(player: Player): Boolean {
        return players.remove(player.getUniqueID())
    }

    // Check whether a player is in a team
    public fun containsPlayer(player: Player): Boolean {
        return players.contains(player.getUniqueID())
    }

    // Get the players in a team
    public fun getPlayers(): Set<Player> {

        // The result
        var result: Set<Player> = HashSet()

        // Loop over every player in the team and add them to the result
        for (playerUUID: UUID in players) {

            // Get the player from the UUID
            var player: Player = Bukkit.getPlayer(playerUUID)

            // If the player is not null then add them
            if (player != null) result.add(player)

        }

        // Return the result
        return result

    }

    // Update the amount of alive players 
    public fun updateAlive() {
        alivePlayers = players.size()
    }

    // Decrement the amount of alive players
    public fun decreaseAlive() {
        alivePlayers--
        if (alivePlayers <= -1) alivePlayers = 0 // Make sure it doesn't overflow
    }

    // Get the amount of alive players currently in the team
    public fun getAlivePlayers() {
        return alivePlayers
    }

    // Get the color of the team
    public fun getColor(): String {
        return colors.get(teamID.toInt())
    }

    // Get the color code of the team
    public fun getColorCode(): String {
        return colorCodes.get(teamID.toInt())
    }

}