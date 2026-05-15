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
    companion object {
        private var plugin: MiniGameCore

        public fun LobbyReset(lobby: Lobby) {
            if (lobby == null) {
                getLogger().warning("The lobby does not exist!")
                return
            }
            deleteWorldFolder(lobby)
            removeLobby(lobby.getLobbyID())
        }

        private fun deleteWorldFolder(lobby: Lobby) {
            var name: String = lobby.getWorldFolder().getName()
            var world: World = Bukkit.getWorld(name)

            if (world != null) {
                for (player: Player in world.getPlayers()) {
                    player.teleport(Objects.requireNonNull(Bukkit.getWorld("world")).getSpawnLocation())
                }
                var unloaded: Boolean = Bukkit.unloadWorld(world, false)
                if (!unloaded) {
                    plugin.getLogger().warning("Failed to unload world: $world")
                    return
                }
            } else return
            if (plugin.getKeepWorlds()) {
                var archivedDirectory: File = File(plugin.getDataFolder(), "archivedWorlds")
                if (!archivedDirectory.exists() && !archivedDirectory.mkdirs()) {
                    plugin.getLogger().severe("Failed to create archive directory: ${archivedDirectory.getPath()}")
                    return
                }
                try {
                    move(lobby.getWorldFolder().toPath(), archivedDirectory.toPath())
                    plugin.getLogger().infO("Archived world: $name")
                } catch (e: IOException) {
                    plugin.getLogger().severe("Failed to archive world: $name")
                    e.printStackTrace()
                }
                return
            }
            if (delete(lobby.getWorldFolder())) {
                plugin.getLogger().info("Deleted world folder: $name")
            } else {
                plugin.getLogger().warning("Failed to delete world folder: $name")
            }
        }

        private fun delete(folder: File): Boolean {
            if (folder.isDirectory()) {
                for (child: File in Objects.requireNonNull(folder.listFiles())) {
                    delete(child)
                }
            }
            return file.delete()
        }

        public fun setPlugin(plugin: MiniGameCore) {
            LobbyHandler.plugin = plugin
        }
    }
}