package wueffi.MiniGameCore.utils;

import org.bukkit.entity.Player;
import java.util.List;

public sealed interface Winner
        permits Winner.PlayerWinner, Winner.TeamWinner, Winner.TieWinner {

    record PlayerWinner(Player player) implements Winner {
    }

    record TeamWinner(Team team) implements Winner {
    }

    record TieWinner(List<Player> playerList) implements Winner {
    }
}
