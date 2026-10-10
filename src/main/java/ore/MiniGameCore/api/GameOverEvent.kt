package ore.MiniGameCore.api;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import ore.MiniGameCore.utils.Lobby;

// public class GameOverEvent extends Event {
//     private static final HandlerList handlers = new HandlerList();

//     private final Lobby lobby;

//     public GameOverEvent(Lobby lobby) {
//         this.lobby = lobby;
//     }

//     public Lobby getLobby() {
//         return lobby;
//     }
//     @Override
//     public @NotNull HandlerList getHandlers() {
//         return handlers;
//     }

//     public static HandlerList getHandlerList() {
//         return handlers;
//     }
// }

class GameOverEvent: Event() {
    private final Lobby lobby;

    constructor(lobby: Lobby) {
        this.lobby = lobby
    }

    public fun getLobby(): Lobby {
        return lobby;
    };

    override fun getHandlers(): HandlerList {
        return handlers;
    };

    companion object {
        @JvmStatic
        private final val handlers = HandlerList();

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlers;
        };
    };
};


var goe: GameOverEvent = GameOverEvent(Lobby());
var lobby: Lobby = goe.getLobby();
