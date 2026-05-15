package ore.MiniGameCore.utils

// Bukkit imports
import org.bukkit.entity.Player

// Java imports
import java.util.HashSet
import java.util.Set

public class Party {

    // Initialize variables
    private val partyID: String
    private val partyName: String
    private val owner: Player
    private val players: Set<Player> = HashSet()
    private val playersInvited: Set<Player> = HashSet()

    // Class constructor function
    public fun Party(partyID: String, partyName: String, owner: Player) {
        this.partyID = partyID
        this.partyName = partyName
        this.owner = owner
        this.players.add(owner)
    }

    // Add player into a party
    public fun addPlayer(player: Player): Boolean {
        
        // If the player is already in the party return
        if (players.contains(player)) return false

        // If the player wasn't invited return
        if (!playersInvited.contains(player)) return false

        // Remove the player from the invited list and add them to the party
        playersInvited.remove(player)
        return players.add(player)

    }

    // Remove the player from a party
    public fun removePlayer(player: Player): Boolean {
        return players.remove(player)
    }

    // Invite a player into a party
    public fun invitePlayer(player: Player): Boolean {

        // If the player is already in the party return
        if (players.contains(player)) return false

        // If the player was already invited return
        if (playersInvited.contains(player)) return false

        // Invite the player
        return playersInvited.add(player)

    }

    // Returns whether the player is in a party
    public fun containsPlayer(player: Player): Boolean {
        return players.contains(player)
    }

    // Returns whether the palyer was invited to a party
    public fun isInvited(player: Player): Boolean {
        return playersInvited.contains(player)
    }

    // Get the list of players in a party
    public fun getPlayers(): Set<Player> {
        return players
    }

    // Get the party ID
    public fun getPartyID(): String {
        return partyID
    }

    // Get the party name
    public fun getPartyName(): String {
        return partyName
    }

    // Returns whether a player is the owner of a party
    public fun isOwner(player: Player): Boolean {
        return owner == player
    }

    // Gets the owner of a party
    public fun getOwner(): Player {
        return owner
    }

    // Run if the player denies an invite
    public fun denyInvite(player: Player): Boolean {
        return playersInvited.remove(player)
    }

}