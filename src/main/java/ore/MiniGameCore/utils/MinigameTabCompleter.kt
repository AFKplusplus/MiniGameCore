package ore.MiniGameCore.utils

// Bukkit imports
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

// JetBrains imports
import org.jetbrains.annotations.NotNull

// MiniGameCore imports
import ore.MiniGameCore.MiniGameCore
import ore.MiniGameCore.managers.LobbyManager

// Java imports
import java.util.ArrayList
import java.util.List
import java.util.stream.Collectors

public class MinigameTabCompleter : TabCompleter {

    // Initialize plugin variable
    private val plugin: MiniGameCore

    // Constructor function
    public fun MinigameTabCompleter(plugin: MiniGameCore) {
        this.plugin = plugin
    }

    // Tab complete function when the player types a command
    @Override
    public fun onTabComplete(@NotNull sender: CommandSender, @NotNull command: Command, @NotNull alias: String, @NotNull args: String[]) {

        // If the command sender wasn't a player (e.g. it was the console) return
        if (!(sender is Player)) return null

        // Tab completions list
        var tabCompletions: List<String> = ArrayList()

        // Commands list
        var commands: String[] = {
            "host",
            "join",
            "ready",
            "unready",
            "confirm",
            "leave",
            "start",
            "spectate",
            "unspectate",
            "stats",
            "reload",
            "stopall",
            "stop",
            "ban",
            "unban",
        }

        // Permissions list
        var permissions: String[] = {
            "mgcore.host",
            "mgcore.join",
            "mgcore.ready",
            "mgcore.ready",
            "mgcore.confirm",
            "mgcore.leave",
            "mgcore.start",
            "mgcore.spectate",
            "mgcore.spectate",
            "mgcore.stats",
            "mgcore.admin",
            "mgcore.admin",
            "mgcore.admin",
            "mgcore.admin",
            "mgcore.admin",
        }

        // If there was only 1 argument (the player only typed /mg)
        if (args.length == 1) {

            // Loop over every command and if the player has the permission tell them the available subcommands
            for (idx: Int in 0..commands.length - 1) {
                if (sender.hasPermission(permissions[idx])) {
                    tabCompletions.add(commands[idx])
                }
            }

        }
        
        // Otherwise they was 2 arguments (e.g. /mg host)
        // Only show the tab completions for people with the correct permissions
        else if (args.length == 2) {

            // Go over each subcommand
            switch (args[0].toLowerCase()) {

                // /mg host
                "host" -> {
                    
                    // If the player is banned OR the player doesn't have the permission needed for the command return null
                    if (plugin.getBannedPlayers().contains(sender.getUniqueId()) || !sender.hasPermission("mgcore.host")) return null

                    // Otherwise set the tab completions to the available games set in the config
                    tabCompletions = plugin.getAvailableGames()

                }

                // /mg join
                "join" -> {

                    // If the player is banned OR the player doesn't have the permission needed for the command return null
                    if (plugin.getBannedPlayers().contains(sender.getUniqueId()) || !sender.hasPermission("mgcore.join")) return null

                    // Loop over all the current lobbies and add them to the tab completers
                    tabCompletions = ArrayList()

                    for (lobby: Lobby in LobbyManager.getInstance().getOpenLobbies()) {
                        var lobbyID: String = lobby.getLobbyID()
                        tabCompletions.add(lobbyID)  
                    }
                    
                }

                // /mg spectate
                "spectate" -> {

                    // If the player doesn't have the permission return null
                    if (sender.hasPermission("mgcore.spectate")) return null

                    // Otherwise get all online players and lobbies
                    tabCompletions = Bukkit.getOnlinePlayers().stream()
                                    .map(Player::getName)
                                    .collect(Collectors.toList())
                    
                    for (lobby: Lobby in LobbyManager.getInstance().getOpenLobbies()) {
                        var lobbyID: String = lobby.getLobbyID()
                        tabCompletions.add(lobbyID)  
                    }
                    
                }

                // /mg stats
                "stats" -> {

                    // If the player doesn't have the permission return null
                    if (sender.hasPermission("mgcore.stats")) return null

                    // Otherwise get all the online players
                    tabCompletions = Bukkit.getOnlinePlayers().stream()
                                    .map(Player::getName)
                                    .collect(Collectors.toList())

                }

                // /mg stop
                "stop" -> {

                    // If the player doesn't have admin permissions return null
                    if (sender.hasPermission("mgcore.admin")) return null

                    // Otherwise get all the lobbies
                    tabCompletions = ArrayList()
                    for (lobby: Lobby in LobbyManager.getInstance().getOpenLobbies()) {
                        var lobbyID: String = lobby.getLobbyID()
                        tabCompletions.add(lobbyID)  
                    }

                }

                // /mg ban|unban
                "ban", "unban" -> {

                    // If the player doesn't have admin permissions return null
                    if (sender.hasPermission("mgcore.admin")) return null

                    // Otherwise get all the online players
                    tabCompletions = Bukkit.getOnlinePlayers().stream()
                                    .map(Player::getName)
                                    .collect(Collectors.toList())

                }

            }

        }

        // Return the tab completions
        return tabCompletions.stream()
                .filter(s -> s.toLowerCase()
                .startsWith(args[args.length - 1]
                .toLowerCase()))
                .toList()

    }

}