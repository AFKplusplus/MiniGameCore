package ore.MiniGameCore.utils

// Bukkit imports
import org.bukkit.entity.Player

// Java imports
import java.util.List

public sealed interface Winner {

    // For a player winning
    data class PlayerWinner(player: Player) : Winner

    // For a team winning
    data class TeamWinner(team: Team) : Winner

    // For a tie
    data class TieWinner(playerList: List<Player>) : Winner
    
}