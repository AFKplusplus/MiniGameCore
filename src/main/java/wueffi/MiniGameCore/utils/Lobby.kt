package ore.MiniGameCore.utils;

// Bukkit imports
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

// MiniGameCore imports
import ore.MiniGameCore.managers.LobbyManager;
import ore.MiniGameCore.managers.ScoreboardManager;

// Java imports
import java.io.File;
import java.util.*;

public class Lobby {

    // Initialize variables for the lobby information.
    private val lobbyID: String
    private val gameName: String
    private val maxPlayers: Int
    private val players: HashSet<UUID> = HashSet()
    private val owner: Player
    private val worldFolder: File
    private val readyPlayers: HashSet<Player> = HashSet()
    private var lobbyState: String
    private val teamList: ArrayList<Team> = ArrayList()
    private var teamCounter: Int = 0

    // Initialization function for the Lobby class
    public fun Lobby(lobbyID: String, gameName: String, maxPlayers: Int, owner: Player, worldFolder: File) {
        this.lobbyID = lobbyID
        this.gameName = gameName
        this.maxPlayers = maxPlayers
        this.owner = owner
        this.worldFolder = worldFolder
        this.players.add(owner.getUniqueId()) // Add the host into the player list
        this.lobbyState = LobbyState
    }

    // Adding players into the lobby
    public fun addPlayer(player: Player): Boolean {

        // If the player is already in the lobby, return
        if (players.contains(player.getUniqueId())) return false

        // If the lobby is already full, return
        if (players.size() >= maxPlayers) return false

        // Otherwise set the player's status to WAITING and add them into the lobby
        ScoreboardManager.setPlayerStatus(player, "WAITING")
        return players.add(player.getUniqueId())

    }

    // Removing players from the lobby
    public fun removePlayer(player: Player): Boolean {

        // Get the lobby that the player is in
        var lobby: Lobby = LobbyManager.getLobbyByPlayer(player)

        // If the player is the only person left in the lobby, reset the lobby
        if (lobby.getPlayers().size() == 1 && players.contains(player.getUniqueId())) {
            LobbyHandler.LobbyReset(lobby)
        }

        // Remove the waiting status from the player and remove them from the lobby
        ScoreboardManager.removePlayerStatus(player)
        return players.remove(player.getUniqueId())

    }

    // Readies a player
    public fun ready(player: Player): Boolean {
        return readyPlayers.add(player)
    }

    // Unreadies a player
    public fun unready(player: Player): Boolean {
        return readyPlayers.remove(player)
    }

    // Gets the current lobby state
    public fun getLobbyState(): String {
        return lobbyState
    }

    // Set the lobby state
    public fun setLobbyState(state: String) {
        this.lobbyState = state
    }

    // Checks if the lobby is full
    public fun isFull(): Boolean {
        return players.size() >= maxPlayers
    }

    // Checks if the lobby contains a specific player
    public fun containsPlayer(player: Player): Boolean {
        if (player == null) return false
        return players.contains(player.getUniqueId())
    }

    // Get all the players in a lobby
    public fun getPlayers(): HashSet<Player> {

        // Create an array for the result
        var result: HashSet<Player> = HashSet()

        // For every player in the lobby, add them into the result
        for (uuid: UUID in players) {
            var player: Player = Bukkit.getPlayer(uuid)
            if (player != null) result.add(player)
        }
        return result

    }

    // Get the team list
    public fun getTeamList(): ArrayList<Team> {
        return teamList
    }

    // Add a team in the game
    public fun addTeam(): Boolean {
        var id: Int = teamCounter
        teamCounter = id + 1

        var result: Team = new Team(String.valueOf(id))
        if (result == null) return false

        return teamList.add(result)
    }

    // Get a team by the ID
    public fun getTeam(id: Int): Team {
        if (teamList.size() >= id) return teamList.get(id)
        else return null
    }

    // Get the players that are ready
    public fun getReadyPlayers(): HashSet<Player> {
        return readyPlayers
    }

    // Get the lobby ID
    public fun getLobbyID(): String {
        return lobbyID
    }

    // Get the game name
    public fun getGameName(): String {
        return gameName
    }

    // Get the host of the lobby
    public fun getOwner(): Player {
        return owner
    }

    // Get the maximum amount of players allowed in the lobby
    public fun getMaxPlayers(): Int {
        return maxPlayers
    }

    // Get the world folder of the world
    public fun getWorldFolder(): File {
        return worldFolder
    }

    // Get the team a player is in
    public fun getTeamByPlayer(player: Player): Team {

        // For every team in the team list, if the player is in the team return the team
        for (team: Team in teamList) {
            if (team.containsPlayer(player)) return team
        }
        return null
    }
}