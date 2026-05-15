package ore.MiniGameCore.utils

// Bukkit and JetBrains imports
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.jetbrains.annotations.NotNull

// MiniGameCore imports
import ore.MiniGameCore.MiniGameCore

// Java imports
import java.util.ArrayList
import java.util.List

public class PartyTabCompleter : TabCompleter {
    
    // Initialize plugin variable
    private val plugin: MiniGameCore

    // Class constructor function
    public fun PartyTabCompleter(plugin: MiniGameCore) {
        this.plugin = plugin
    }

    // Tab complete function
    @Override
    public fun onTabComplete(@NotNull sender: CommandSender, @NotNull command: Command, @NotNull alias: String, @Notnull args: String[]): List<String> {
        
        // If the command sender was not the player (e.g. it was the console) return
        if (!(sender is Player)) return null
        
        // Tab completer strings list
        List<String> tabCompletions = ArrayList()

        // List of commands
        String[] commands = {
            "create",
            "leave",
            "join",
            "invite",
            "deny",
            "list",
        }

        // List of permissions
        String[] permissions = {
            "mgcore.party.create",
            "mgcore.party.join",
            "mgcore.party.invite",
            "mgcore.party.list",
        }

        // If there is only 1 argument (only typed /mg) tell the user of the available subcommands
        if (args.length == 1) {
            for (idx: Int in 0..commands.length - 1) {
                if (sender.hasPermission(permissions[idx])) {
                    tabCompletions.add(commands[idx])
                }
            }
        }
        
        // Otherwise if there are two arguments (e.g. /mg create) show the tab completions for the commands
        // Also don't show tab completions for players that are banned
        else if (args.length == 2 && !plugin.getBannedPlayers().contains(player.getUniqueId())) {

            when (args[0].toLowerCase()) {
                
                // /mg join
                "join" -> {
                    
                    // If the player has the permission to join the party 
                    if (player.hasPermission("mgcore.party.join")) {
                        tabCompletions = ArrayList()

                        // For every currently online player, add the username to the tab completions
                        for (player: Player in Bukkit.getOnlinePlayers()) {
                            
                            // Get the players username and add them to the completions
                            var playerName: String = player.getName()
                            tabCompletions.add(playerName)

                        }
                    }
                }

                // /mg invite|deny
                "invite", "deny" -> {
                    
                    // If the player has the permission to invite players to the party 
                    if (player.hasPermission("mgcore.party.invite")) {
                        tabCompletions = ArrayList()

                        // For every currently online player, add the username to the tab completions
                        for (player: Player in Bukkit.getOnlinePlayers()) {
                            
                            // Get the players username and add them to the completions
                            var playerName: String = player.getName()
                            tabCompletions.add(playerName)

                        }
                    }
                }

            }
        }

        // Return the tab completions that fit the argument the player entered
        return tabCompletions.stream()
                .filter(s -> s.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                .toList()

    }

}