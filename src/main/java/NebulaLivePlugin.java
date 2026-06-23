package hu.media.livecommand;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Plugin(
        id = "nebulalive",
        name = "NebulaLive",
        version = "1.0",
        description = "Globális /live parancs minden al-szerverre kattintható linkkel Velocity proxyn",
        authors = {"Media"}
)
public class NebulaLivePlugin {

    private final ProxyServer server;
    private final Logger logger;

    @Inject
    public NebulaLivePlugin(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        server.getCommandManager().register(
                server.getCommandManager().metaBuilder("live").build(),
                new LiveCommand(server)
        );
        logger.info("NebulaLive plugin sikeresen elindult!");
    }

    public static class LiveCommand implements SimpleCommand {

        private final ProxyServer server;

        public LiveCommand(ProxyServer server) {
            this.server = server;
        }

        @Override
        public void execute(Invocation invocation) {
            CommandSource source = invocation.source();
            String[] args = invocation.arguments();

            if (!(source instanceof Player)) {
                source.sendMessage(Component.text("§cEzt a parancsot csak játékosok használhatják!"));
                return;
            }

            Player player = (Player) source;
            String streamer = player.getUsername();

            if (args.length == 0) {
                player.sendMessage(Component.text("§cHasználat: /live [youtube/tiktok]"));
                return;
            }

            // ==================== YOUTUBE OPCIÓ ====================
            if (args.equalsIgnoreCase("youtube")) {
                Component line1 = Component.text("§c■■■■■■■■ §e● §c§lYouTube §f§lFigyelmeztetés! §e●\n");
                Component line2 = Component.text("§c■■§f■§c■■■■■ §8[§2Média§8] §b" + streamer + " §aéppen liveol!\n");
                
                Component linkLine = Component.text("§c■■§f■■§c■■■ §chttps://youtube.com" + streamer.toLowerCase() + "\n")
                        .clickEvent(ClickEvent.openUrl("https://youtube.com" + streamer.toLowerCase()))
                        .hoverEvent(HoverEvent.showText(Component.text("§eKattints ide az élő adás megnyitásához!")));
                
                Component line4 = Component.text("§c■■§f■§c■■■■■ §7Támogasd a médiás partnerünket\n");
                Component line5 = Component.text("§c■■■■■■■■ §7a live nézésével §c§lYouTube§7-on.");

                Component fullMessage = Component.text()
                        .append(line1)
                        .append(line2)
                        .append(linkLine)
                        .append(line4)
                        .append(line5)
                        .build();

                for (Player allPlayers : server.getAllPlayers()) {
                    allPlayers.sendMessage(fullMessage);
                }
                return;
            }

            // ==================== TIKTOK OPCIÓ ====================
            if (args.equalsIgnoreCase("tiktok")) {
                Component line1 = Component.text("§1■■■■§f■§c■§1■■■ §d● §f§lTikTok §f§lFigyelmeztetés! §d●\n");
                Component line2 = Component.text("§1■■§b■§1■§f■§c■§1■■■  §b" + streamer + " §aéppen liveol!\n");
                
                Component linkLine = Component.text("§1■§b■§1■■§f■§c■§1■■■  §://ctiktok.com@" + streamer.toLowerCase() + "/live\n")
                        .clickEvent(ClickEvent.openUrl("https://tiktok.com@" + streamer.toLowerCase() + "/live"))
                        .hoverEvent(HoverEvent.showText(Component.text("§eKattints ide a TikTok megnyitásához!")));
                
                Component line4 = Component.text("§1■§f■§1■■§f■§c■§1■■■  §7Támogasd a médiás\n");
                Component line5 = Component.text("§1■§f■§b■§1■§f■§c■§1■■■  §7partnerünket\n");
                Component line6 = Component.text("§1■■§f■■■§c■§1■■■  §7a live nézésével §d§lTikTok§7-on.\n");
                Component line7 = Component.text("§1■■■■■■■■■");

                Component fullMessage = Component.text()
                        .append(line1)
                        .append(line2)
                        .append(linkLine)
                        .append(line4)
                        .append(line5)
                        .append(line6)
                        .append(line7)
                        .build();

                for (Player allPlayers : server.getAllPlayers()) {
                    allPlayers.sendMessage(fullMessage);
                }
                return;
            }

            player.sendMessage(Component.text("§cIsmeretlen opció! Használat: /live [youtube/tiktok]"));
        }

        @Override
        public List<String> suggest(Invocation invocation) {
            String[] args = invocation.arguments();
            if (args.length <= 1) {
                String currentArg = args.length == 0 ? "" : args.toLowerCase();
                List<String> opciok = Arrays.asList("youtube", "tiktok");
                List<String> talalatok = new ArrayList<>();
                for (String opcio : opciok) {
                    if (opcio.startsWith(currentArg)) {
                        talalatok.add(opcio);
                    }
                }
                return talalatok;
            }
            return new ArrayList<>();
        }
    }
}
