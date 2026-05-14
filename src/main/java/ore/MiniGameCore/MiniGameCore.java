package ore.MiniGameCore;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import ore.MiniGameCore.commands.MiniGameCommand;
import ore.MiniGameCore.commands.PartyCommand;
import ore.MiniGameCore.commands.TeamChatCommand;
import ore.MiniGameCore.managers.GameManager;
import ore.MiniGameCore.managers.LobbyManager;
import ore.MiniGameCore.managers.ScoreBoardManager;
import ore.MiniGameCore.utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class MiniGameCore extends JavaPlugin {
    private final MiniGameCore plugin = this;
    private List<String> availableGames;
    private List<UUID> bannedPlayers;
    private Boolean keepWorlds;
    private LobbyManager lobbyManager;

    @Override
    public void onEnable() {
        getLogger().info("MinigameCore enabled!");
        saveDefaultConfig();

        List<String> availableGames = getConfig().getStringList("available-games");
        List<UUID> bannedPlayers = new ArrayList<>();
        boolean keepWorlds = getConfig().getBoolean("keep-worlds");
        boolean disableScoreBoard = getConfig().getBoolean("disable-scoreboard");
        for (String UUIDstring : getConfig().getStringList("banned-players")) {
            try {
                UUID uuid = UUID.fromString(UUIDstring);
                bannedPlayers.add(uuid);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Found invalid UUID in banned players list " + UUIDstring + ". Ignoring.");
            }
        }
        this.availableGames = availableGames;
        this.bannedPlayers = bannedPlayers;
        this.keepWorlds = keepWorlds;
        getLogger().info("Config loaded!");

        Stats.setup();
        getLogger().info("Stats loaded!");

        getCommand("mg").setExecutor(new MiniGameCommand(this));
        getCommand("mg").setTabCompleter(new MiniGameTabCompleter(this));
        getCommand("party").setExecutor(new PartyCommand(this));
        getCommand("party").setTabCompleter(new PartyTabCompleter(this));
        getCommand("p").setExecutor(new PartyCommand(this));
        getCommand("p").setTabCompleter(new PartyTabCompleter(this));
        getCommand("teamchat").setExecutor(new TeamChatCommand());
        getCommand("tc").setExecutor(new TeamChatCommand());
        getLogger().info("Commands registered!");

        getLogger().info("Starting cleanup task...");
        CleanUpWorlds.cleanUpWorlds(this);
        LobbyManager.cleanUpLobbies(this);

        if (!disableScoreBoard) ScoreBoardManager.startAnimationLoop();

        LobbyHandler.setPlugin(this);
        lobbyManager = LobbyManager.getInstance();

        Bukkit.getPluginManager().registerEvents(new GameManager(this), this);
        getServer().getPluginManager().registerEvents(new PlayerHandler(this), this);
    }

    @Override
    public void onDisable() {
        for (Lobby lobby : Stream.concat(lobbyManager.getOpenLobbies().stream(), lobbyManager.getClosedLobbies().stream()).toList()) {
            String lobbyid = lobby.getLobbyId();
            for (Player player : lobby.getPlayers()) {
                PlayerHandler.PlayerReset(player);
            }
            getLogger().info("Lobby disabling: " + lobbyid);
            LobbyHandler.LobbyReset(lobby);
            getLogger().info("Shut down Lobby: " + lobbyid);
        }
        GameManager.frozenPlayers.clear();
        getLogger().info("Starting cleanup task...");
        CleanUpWorlds.cleanUpWorlds(this);
        getLogger().info("MinigameCore disabled!");
    }

    public List<String> getAvailableGames() {
        return availableGames;
    }

    public List<UUID> getBannedPlayers() {
        return bannedPlayers;
    }

    public Boolean getKeepWorlds() {
        return keepWorlds;
    }

    private void writeBannedPlayers() {
        List<String> bannedPlayersString = new ArrayList<>();
        for (UUID player: bannedPlayers) {
            bannedPlayersString.add(player.toString());
        }
        getConfig().set("banned-players", bannedPlayersString);
        saveConfig();
    }

    public void banPlayer(UUID player) {
        bannedPlayers.add(player);
        writeBannedPlayers();
    }

    public void unbanPlayer(UUID player) {
        bannedPlayers.remove(player);
        writeBannedPlayers();
    }
}
