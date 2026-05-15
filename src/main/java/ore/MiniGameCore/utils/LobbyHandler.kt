package ore.MiniGameCore.utils

// Bukkit imports
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.Bukkit.getLogger

// MiniGameCore imports
import ore.MiniGameCore.MiniGameCore
import ore.MiniGameCore.managers.LobbyManager.removeLobby

// Java imports
import java.io.File
import java.io.IOException
import java.util.Objects
import java.nio.file.Files.move

public class LobbyHandler {

    // This is to make the variables and functions static
    companion object {

        // Initialize the plugin variable
        private var plugin: MiniGameCore

        // Resets the specified lobby
        public fun LobbyReset(lobby: Lobby) {
            if (lobby == null) {
                getLogger().warning("The lobby does not exist!")
                return
            }
            deleteWorldFolder(lobby)
            removeLobby(lobby.getLobbyID())
        }

        // Deletes the world folder of the specified lobby
        private fun deleteWorldFolder(lobby: Lobby) {

            // Initialize the world folder name and the world
            var name: String = lobby.getWorldFolder().getName()
            var world: World = Bukkit.getWorld(name)

            // Return if the world specified is null
            if (world != null) {

                // For every player in the world, teleport them back to spawn
                for (player: Player in world.getPlayers()) {
                    player.teleport(Objects.requireNonNull(Bukkit.getWorld("world")).getSpawnLocation())
                }

                // Unload the world
                var unloaded: Boolean = Bukkit.unloadWorld(world, false)

                // If the world failed to unload warn the console
                if (!unloaded) {
                    plugin.getLogger().warning("Failed to unload world: $world")
                    return

                }
            } else return

            // If the config is set to keep the worlds, archive the world folders in the archivedWorlds folder
            if (plugin.getKeepWorlds()) {

                // Create the archived worlds folder
                var archivedDirectory: File = File(plugin.getDataFolder(), "archivedWorlds")

                // If the folder still doesnt exist and it failed to create warn the console
                if (!archivedDirectory.exists() && !archivedDirectory.mkdirs()) {
                    plugin.getLogger().severe("Failed to create archive directory: ${archivedDirectory.getPath()}")
                    return
                }

                try {

                    // Try to move the world folders into the archived wolrds folder
                    move(lobby.getWorldFolder().toPath(), archivedDirectory.toPath())
                    plugin.getLogger().info("Archived world: $name")

                } catch (e: IOException) {

                    // Failed, warn the console and print the stack trace
                    plugin.getLogger().severe("Failed to archive world: $name")
                    e.printStackTrace()

                }
                return
            }

            // Attempt to delete the world folder, if it fails then warn the console
            if (delete(lobby.getWorldFolder())) {
                plugin.getLogger().info("Deleted world folder: $name")
            } else {
                plugin.getLogger().warning("Failed to delete world folder: $name")
            }
        }

        // Delete a file/directory
        private fun delete(file: File): Boolean {

            // If the file is a directory, delete all the children files
            if (file.isDirectory()) {
                for (child: File in Objects.requireNonNull(file.listFiles())) {
                    delete(child)
                }
            }

            // Then delete the file/directory itself
            return file.delete()
        }

        // Set the plugin variable
        public fun setPlugin(plugin: MiniGameCore) {
            LobbyHandler.plugin = plugin
        }
    }
}