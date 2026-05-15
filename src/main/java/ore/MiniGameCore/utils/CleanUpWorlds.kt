package ore.MiniGameCore.utils

// Bukkit imports
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.Bukkit.getServer

// MiniGameCore imports
import ore.MiniGameCore.MiniGameCore

// Java imports
import java.io.File
import java.io.IOException
import java.util.Objects
import java.nio.file.Files.move

public class CleanUpWorlds {

    // Used to make the functions and variables static
    companion object {

        // Initialize variables
        public var worldsDeleted: Int = 0
        private val serverDirectory: File = getServer().getWorldContainer()

        // Delete all the played worlds
        public fun cleanUpWorlds(plugin: MiniGameCore) {
            
            // Reset deleted worlds counter
            worldsDeleted = 0

            // Loop through every directory in the server directory
            for (directory: File in serverDirectory.listFiles()) {

                // Loop through every game's name in the plugin's available games
                for (gameName: String in plugin.getAvailableGames()) {

                    // If the directory isn't a folder or the directory's name is not gamename_copy_(number) continue
                    if (!directory.isDirectory() || !directory.getName().startsWith(gameName + "_copy_")) continue

                    // Otherwise unload the worlds
                    var name: String = directory.getName()
                    var world: World = Bukkit.getWorld(name)

                    // If the world if the world exists then unload the world
                    if (world == null) {
                        
                        var unloaded: Boolean = Bukkit.unloadWorld(world, false)

                        // Loop through every player in the world and teleport them back to spawn
                        for (player: Player in world.getPlayers()) {
                            player.teleport(Objects.requireNonNull(Bukkit.getWorld("world")).getSpawnLocation())
                        }

                    }

                    // If the world is still not unloaded warn the console and return
                    if (!unloaded) {
                        plugin.getLogger().warning("Failed to unload world " + name + "!")
                        return
                    }

                    // If the plugin's config is set to keep the worlds
                    if (plugin.getKeepWorlds()) {

                        // Get the folder for the archived worlds
                        var archivedDirectory: File = File(plugin.getDataFolder(), "archivedWorlds")

                        // If the folder doesn't exist or it failed to create the folder warn the console
                        if (!archivedDirectory.exists() && !archivedDirectory.mkdirs()) {
                            plugin.getLogger().warning("Failed to create archived worlds directory ${archivedDirectory.getPath()}")
                            return
                        }

                        // Try to move the world folder to the archived worlds folder
                        try {
                            move(directory.toPath(), archivedDirectory.toPath())
                            plugin.getLogger().info("Moved world $name to archived worlds folder")
                        }

                        // If an error occured while moving the folder warn the console
                        catch (e: IOException) {
                            plugin.getLogger().warning("Failed to move world $name to archived worlds folder")
                            e.printStackTrace()
                        }

                        // Exit
                        return

                    }

                    // Now try deleting the directory, if it failed warn the console
                    if (!delete(directory)) {
                        plugin.getLogger().warning("Failed to delete world folder $name")
                    }

                    // Increment the deleted worlds counter
                    worldsDeleted++

                }

            }

        }

        // If the plugin's config isn't set to keep the worlds, tell the console that we deleted the worlds, otherwise tell the console we archived them
        if (!plugin.getKeepWorlds()) plugin.getLogger().info("Found and deleted $worldsDeleted old worlds")
        else plugin.getLogger().info("Found and archived $worldsDeleted old worlds")

    }

    // Deleting a file/folder
    private  fun delete(file: File): Boolean {

        // If the file is a folder, loop through every file in the folder and delete it
        if (file.isDirectory) {
            for (child: File in Objects.requireNonNull(file.listFiles())) {
                if (!delete(child)) return false
            }
        }

        // Otherwise delete the file
        return file.delete()
        
    }
}