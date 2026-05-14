package ore.MiniGameCore

// Bukkit imports
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

// MiniGameCore imports
import ore.MiniGameCore.commands.MiniGameCommand
import ore.MiniGameCore.commands.PartyCommand
import ore.MiniGameCore.commands.TeamChatCommand
import ore.MiniGameCore.managers.GameManager
import ore.MiniGameCore.managers.LobbyManager
import ore.MiniGameCore.managers.ScoreBoardManager
import ore.MiniGameCore.utils.*

// Java imports
import java.util.ArrayList
import java.util.List
import java.util.UUID
import java.util.stream.Stream

class MiniGameCore : JavaPlugin() {

    //// Initialize variables
    private val plugin: MiniGameCore = this
    private var availableGames: List<String>
    private var bannedPlayerrs: List<UUID>
    private var keepWorlds: Boolean
    private var lobbymanager: LobbyManager


    //// When the plugin is enabled (e.g. when the server starts up)
    override fun onEnable() {

        // Tell the console that the plugin is enabled and save the default config
        getLogger().info("MiniGameCore plugin enabled!")
        saveDefaultConfig()

        // Initialize variables for the available games, banned players, world keeping setting, and whether to enable the scoreboard
        availableGames = getConfig().getStringList("available-games")
        bannedPlayers = ArrayList()
        keepWorlds = getConfig().getBoolean("keep-worlds")
        var disableScoreboard: Boolean = getConfig().getBoolean("disable-scoreboard")

        // Loop over every UUID in the banned players list
        var bannedPlayersList = getConfig().getStringList("banned-players")
        for (UUIDString: String in bannedPlayersList) {
            try {

                // Add the UUID of the banned player into the banned players list
                bannedPlayers.add(UUID.fromString(UUIDString))

            } catch (e: IllegalArgumentException) {

                // If we get an error, tell the console
                getLogger().warning("Found an invalid UUID in the banned players list: $UUIDstring")

            }
        }

        //// Set this plugin's variables to the variables we just initialized
        this.avaiableGames = availableGames
        this.bannedPlayers = bannedPlayers
        this.keepWorlds = keepWorlds
        getLogger().info("Config finished loading!")

        // Setup the stats
        Stats.setup()
        getLogger().info("Stats loaded!")

        // Load commands and tab completers
        // /mg
        getCommand("mg").setExecutor(MiniGameCommand(this))
        getCommand("mg").setTabCompleter(MiniGameTabCompleter(this))

        // /party
        getCommand("party").setExecutor(PartyCommand(this))
        getCommand("party").setTabCompleter(PartyTabCompleter(this))

        // /p (alias for /party)
        getCommand("p").setExecutor(PartyCommand(this))
        getCommand("p").setTabCompleter(PartyTabCompleter(this))

        // /teamchat
        getCommand("teamchat").setExecutor(TeamChatCommand())

        // /tc (alias for /teamchat)
        getCommand("tc").setExecutor(TeamChatCommand())
        getLogger().info("Commands and tab completers finished registering!")

        // Clean up the worlds and lobbies
        getLogger().info("Starting cleanup task...")
        CleanUpWorlds.cleanUpWorlds(this)
        LobbyManager.cleanUpLobbies(this)

        // If the scoreboard is not disabled, start the scoreboard animation loop
        if (!disableScoreboard) ScoreBoardManager.startAnimationLoop()

        // Set the lobby handler plugin to this plugin and initialize the lobby manager
        LobbyHandler.setPlugin(this)
        lobbyManager = LobbyManager.getInstance()

        // Register the evnets of the game manager and player handler
        Bukkit.getPluginManager().registerEvents(GameManager(this), this)
        getServer().getPluginManager().registerEvents(PlayerHandler(this), this)

    }

    //// When the plugin is disabled (e.g. when the server is stopped)
    override fun onDisable() {

        // Close every open and closed lobby
        var openedAndClosedLobbies: List<Lobby> = Stream.concat(
            lobbyManager.getOpenLobbies().stream(), 
            lobbyManager.getClosedLobbies().stream()
            ).toList()
        
        // For every lobby in the open and closed lobbies list
        for (lobby: Lobby in openedAndClosedLobbies) {

            // Get the lobby id 
            var lobbyId: String = lobby.getLobbyId()

            // For every player in the lobby, reset the player
            for (player: Player in lobby.getPlayers()) {
                PlayerHandler.PlayerReset(player)
            }

            // Tell the console that the lobbies are being disabled
            getLogger().info("Disabling lobby $lobbyId")
            LobbyHandler.LobbyReset(lobby);
            getLogger().info("Lobby $lobbyId disabled!")
        }

        // Clear the frozen players and clean up the worlds
        GameManager.frozenPlayers.clear()
        getLogger().info("Starting cleanup task...")
        CleanUpWorlds.cleanUpWorlds(this)
        getLogger().info("MiniGameCore plugin disabled!")
    }

    //// Get all currently available games
    public fun getAvailableGames(): List<String> {
        return availableGames
    }

    //// Get all currently banned palyers
    public fun getBannedPlayers(): List<UUID> {
        return bannedPlayers
    }

    //// Get the setting whether to keep the worlds
    public getKeepWorlds(): Boolean {
        return keepWorlds
    }

    //// Update the banned players list in the config
    private fun writeBannedPlayers() {

        // Initialize banned players list
        var bannedPlayerString: List<String> = ArrayList()

        // For every player in banned players currently, add them to the banned players list
        for (player: UUID in bannedPlayers) {
            bannedPlayerString.add(player.toString())
        }

        // Save the banned players list into the config
        getConfig().set("banned-players", bannedPlayerString)
        saveConfig()
    }

    //// Ban a player and update the config
    public fun banPlayer(player: UUID) {
        bannedPlayers.add(player)
        writeBannedPlayers()
    }

    //// Unban a player and update the config
    public fun unbanPlayer(player: UUID) {
        bannedPlayers.remove(player)
        writeBannedPlayers()
    }

}