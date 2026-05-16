package ore.MiniGameCore.api

// Bukkit imports
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

// JetBrains imports
import org.jetbrains.annotations.NotNull

// MiniGameCore imports
import ore.MiniGameCore.utils.Lobby

public class GameOverEvent : Event {

    // Handler list variable
    // Needed to make it static
    companion object { private val handlers: HandlerList = HandlerList() }

    // Lobby variable
    private val lobby: Lobby

    // Constructor function
    public fun GameOverEvent(lobby: Lobby) {
        this.lobby = lobby
    }

    // Get the lobby
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