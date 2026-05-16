package ore.MiniGameCoe.api

// Bukkit imports
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

// JetBrains import
import org.jetbrains.annotations.NotNull

// MiniGameCore imports
import ore.MiniGameCore.utils.Lobby

public class GameStartEvent : Event {

    // Handler list variable
    // Needed to make to static
    companion object { private val handlers: HandlerList = HandlerList() }

    // The game name and lobby variable
    private val gameName: String
    private val lobby: Lobby

    // Constructor function
    public fun GameStartEvent(gameName: String, lobby: Lobby) {
        this.gameName = gameName
        this.lobby = lobby
    }

    // Get game name variable
    public fun getGameName(): String {
        return gameName
    }

    // Get lobby variable
    public fun getLobby(): Lobby {
        return lobby
    }

    // Get handlers
    @Override
    public @NotNull getHandlers(): HandlerList {
        return handlers
    }

    // Get handler list
    // Needed to make it static
    companion object {

        public fun getHandlerList(): HandlerList {
            return handlers;
        }

    }

}