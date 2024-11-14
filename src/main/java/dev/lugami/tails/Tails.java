package dev.lugami.tails;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.minexd.pidgin.Pidgin;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.qrakn.honcho.Honcho;
import com.qrakn.phoenix.lang.file.type.BasicConfigurationFile;
import dev.lugami.tails.cache.RedisCache;
import dev.lugami.tails.chat.Chat;
import dev.lugami.tails.chat.ChatListener;
import dev.lugami.tails.chat.command.ClearChatCommand;
import dev.lugami.tails.chat.command.MuteChatCommand;
import dev.lugami.tails.chat.command.SlowChatCommand;
import dev.lugami.tails.config.ConfigValidation;
import dev.lugami.tails.disguise.DisguiseManager;
import dev.lugami.tails.disguise.commands.DisguiseAdvancedCommand;
import dev.lugami.tails.disguise.commands.DisguiseNormalCommand;
import dev.lugami.tails.disguise.commands.UndisguiseCommand;
import dev.lugami.tails.disguise.commands.staff.*;
import dev.lugami.tails.essentials.Essentials;
import dev.lugami.tails.essentials.EssentialsListener;
import dev.lugami.tails.essentials.command.*;
import dev.lugami.tails.network.NetworkPacketListener;
import dev.lugami.tails.network.command.ReportCommand;
import dev.lugami.tails.network.command.RequestCommand;
import dev.lugami.tails.network.packet.*;
import dev.lugami.tails.profile.Profile;
import dev.lugami.tails.profile.ProfileListener;
import dev.lugami.tails.profile.ProfileTypeAdapter;
import dev.lugami.tails.profile.command.AddPermission;
import dev.lugami.tails.profile.command.DelPermission;
import dev.lugami.tails.profile.conversation.command.MessageCommand;
import dev.lugami.tails.profile.conversation.command.ReplyCommand;
import dev.lugami.tails.profile.grant.GrantListener;
import dev.lugami.tails.profile.grant.command.ClearGrantsCommand;
import dev.lugami.tails.profile.grant.command.GrantCommand;
import dev.lugami.tails.profile.grant.command.GrantsCommand;
import dev.lugami.tails.profile.option.command.ToggleGlobalChatCommand;
import dev.lugami.tails.profile.option.command.TogglePrivateMessagesCommand;
import dev.lugami.tails.profile.option.command.ToggleSoundsCommand;
import dev.lugami.tails.profile.punishment.command.*;
import dev.lugami.tails.profile.punishment.listener.PunishmentListener;
import dev.lugami.tails.profile.staff.command.AltsCommand;
import dev.lugami.tails.profile.staff.command.FreezeCommand;
import dev.lugami.tails.profile.staff.command.StaffChatCommand;
import dev.lugami.tails.profile.staff.command.StaffModeCommand;
import dev.lugami.tails.rank.Rank;
import dev.lugami.tails.rank.RankTypeAdapter;
import dev.lugami.tails.rank.command.*;
import dev.lugami.tails.tags.PrefixCommand;
import dev.lugami.tails.tags.Tag;
import dev.lugami.tails.tags.commands.*;
import dev.lugami.tails.util.CC;
import dev.lugami.tails.util.CommandUtil;
import dev.lugami.tails.util.DoubleTypeAdapter;
import dev.lugami.tails.util.TipsTask;
import dev.lugami.tails.util.adapter.ChatColorTypeAdapter;
import dev.lugami.tails.util.duration.Duration;
import dev.lugami.tails.util.duration.DurationTypeAdapter;
import dev.lugami.tails.util.menu.MenuListener;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

@Getter
public class Tails extends JavaPlugin {

	public static final Gson GSON = new Gson();
	public static final Type LIST_STRING_TYPE = new TypeToken<List<String>>() {}.getType();

	private static Tails tails;

	private BasicConfigurationFile mainConfig;

	private Honcho honcho;
	private Pidgin pidgin;

	private MongoDatabase mongoDatabase;
	private JedisPool jedisPool;
	private RedisCache redisCache;

	private Essentials essentials;
	private Chat chat;

	@Setter private boolean debug;
	private DisguiseManager disguiseManager;

	@Override
	public void onEnable() {
		tails = this;
		
		mainConfig = new BasicConfigurationFile(this, "config");

		new ConfigValidation(mainConfig.getFile(), mainConfig.getConfiguration(), 4).check();

		loadMongo();
		loadRedis();

		redisCache = new RedisCache(this);
		essentials = new Essentials(this);
		chat = new Chat(this);

		honcho = new Honcho(this);
		honcho.registerTypeAdapter(Double.class, new DoubleTypeAdapter());
		disguiseManager = new DisguiseManager();

		Arrays.asList(
				new BroadcastCommand(),
				new ClearCommand(),
				new GameModeCommand(),
				new HealCommand(),
				new ShowAllPlayersCommand(),
				new ShowPlayerCommand(),
				new HidePlayerCommand(),
				new LocationCommand(),
				new MoreCommand(),
				new RenameCommand(),
				new SetSlotsCommand(),
				new StaffDisguiseAdd(),
				new StaffDisguiseList(),
				new StaffDisguiseRemove(),
				new DisguiseCheck(),
				new DisguiseCheckAll(),
				new DisguiseNormalCommand(),
				new DisguiseAdvancedCommand(),
				new UndisguiseCommand(),
				new UnblacklistCommand(),
				new PrefixCommand(),
				new TagCommand(),
				new TagCreateCommand(),
				new TagDeleteCommand(),
				new TagListCommand(),
				new TagPermCommand(),
				new FreezeCommand(),
				new TagPrefixCommand(),
				new AddPermission(),
				new DelPermission(),
				new BlacklistCommand(),
				new DayCommand(),
				new NightCommand(),
				new SunsetCommand(),
				new ClearChatCommand(),
				new SlowChatCommand(),
				new AltsCommand(),
				new BanCommand(),
				new CheckCommand(),
				new KickCommand(),
				new MuteCommand(),
				new UnbanCommand(),
				new UnmuteCommand(),
				new WarnCommand(),
				new GrantCommand(),
				new GrantsCommand(),
				new PluginsCommand(),
				new StaffChatCommand(),
				new StaffModeCommand(),
				new MuteChatCommand(),
				new RankAddPermissionCommand(),
				new RankCreateCommand(),
				new RankDeleteCommand(),
				new RankHelpCommand(),
				new RankInfoCommand(),
				new RankInheritCommand(),
				new RankRemovePermissionCommand(),
				new RanksCommand(),
				new RankSetColorCommand(),
				new RankSetPrefixCommand(),
				new RankSetWeightCommand(),
				new RankUninheritCommand(),
				new TailsDebugCommand(),
				new TeleportWorldCommand(),
				new MessageCommand(),
				new ReplyCommand(),
				new ToggleGlobalChatCommand(),
				new TogglePrivateMessagesCommand(),
				new ToggleSoundsCommand(),
				new PingCommand(),
				new MisplaceCommand(),
				new ListCommand(),
				new ReportCommand(),
				new RequestCommand(),
				new ClearGrantsCommand(),
				new ClearPunishmentsCommand(),
				new SudoCommand(),
				new SudoAllCommand()
		).forEach(honcho::registerCommand);

		honcho.registerTypeAdapter(Rank.class, new RankTypeAdapter());
		honcho.registerTypeAdapter(Profile.class, new ProfileTypeAdapter());
		honcho.registerTypeAdapter(Duration.class, new DurationTypeAdapter());
		honcho.registerTypeAdapter(ChatColor.class, new ChatColorTypeAdapter());

		pidgin = new Pidgin("tails",
				mainConfig.getString("REDIS.HOST"),
				mainConfig.getInteger("REDIS.PORT"),
				mainConfig.getBoolean("REDIS.AUTHENTICATION.ENABLED") ?
						mainConfig.getString("REDIS.AUTHENTICATION.PASSWORD") : null
		);

		Arrays.asList(
				PacketAddGrant.class,
				PacketBroadcastPunishment.class,
				PacketDeleteGrant.class,
				PacketDeleteRank.class,
				PacketRefreshRank.class,
				PacketStaffChat.class,
				PacketStaffJoinNetwork.class,
				PacketStaffLeaveNetwork.class,
				PacketStaffSwitchServer.class,
				PacketStaffReport.class,
				PacketStaffRequest.class,
				PacketClearGrants.class,
				PacketClearPunishments.class
		).forEach(pidgin::registerPacket);

		pidgin.registerListener(new NetworkPacketListener(this));

		Arrays.asList(
				new ProfileListener(),
				new MenuListener(),
				new EssentialsListener(),
				new ChatListener(),
				new GrantListener(),
				new PunishmentListener()
		).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

		Rank.init();
		Tag.load();
		Bukkit.getScheduler().runTaskTimerAsynchronously(this, new TipsTask(), 20L * 60, 20L * 60);
	}

	@Override
	public void onDisable() {
		try {
			jedisPool.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Broadcasts a message to all server operators.
	 *
	 * @param message The message.
	 */
	public static void broadcastOps(String message) {
		Bukkit.getOnlinePlayers().stream().filter(Player::isOp).forEach(op -> op.sendMessage(CC.translate(message)));
	}

	private void loadMongo() {
		if (mainConfig.getBoolean("MONGO.AUTHENTICATION.ENABLED")) {
			ServerAddress serverAddress = new ServerAddress(mainConfig.getString("MONGO.HOST"),
					mainConfig.getInteger("MONGO.PORT"));

			MongoCredential credential = MongoCredential.createCredential(
					mainConfig.getString("MONGO.AUTHENTICATION.USERNAME"), "admin",
					mainConfig.getString("MONGO.AUTHENTICATION.PASSWORD").toCharArray());

			mongoDatabase = new MongoClient(serverAddress, credential, MongoClientOptions.builder().build())
					.getDatabase("tails");
		} else {
			mongoDatabase = new MongoClient(mainConfig.getString("MONGO.HOST"),
					mainConfig.getInteger("MONGO.PORT")).getDatabase("tails");
		}
	}

	private void loadRedis() {
		jedisPool = new JedisPool(mainConfig.getString("REDIS.HOST"), mainConfig.getInteger("REDIS.PORT"));

		if (mainConfig.getBoolean("REDIS.AUTHENTICATION.ENABLED")) {
			try (Jedis jedis = jedisPool.getResource()) {
				jedis.auth(mainConfig.getString("REDIS.AUTHENTICATION.PASSWORD"));
			}
		}
	}

	public static Tails get() {
		return tails;
	}

}
