package xyz.i35ak47.hutao;

/* 
 * Copyright (C) 2021 Velosh, all rights reserved. Source code available under the AGPL.
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import com.google.common.reflect.ClassPath;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;
import xyz.i35ak47.hutao.exceptions.BotTokenException;
import xyz.i35ak47.hutao.modules.android.EvolutionX;
import xyz.i35ak47.hutao.modules.android.StatiXOS;
import xyz.i35ak47.hutao.modules.fun.Echo;
import xyz.i35ak47.hutao.modules.misc.Hello;
import xyz.i35ak47.hutao.modules.info.ID;
import xyz.i35ak47.hutao.modules.misc.Ping;
import xyz.i35ak47.hutao.modules.moderative.BanMember;
import xyz.i35ak47.hutao.modules.moderative.KickMember;
import xyz.i35ak47.hutao.modules.moderative.TempBanMember;
import xyz.i35ak47.hutao.modules.owner.*;
import xyz.i35ak47.hutao.modules.info.ServerInfo;
import xyz.i35ak47.hutao.utils.BotUtil;
import xyz.i35ak47.hutao.utils.FileUtil;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Objects;

import org.slf4j.Logger;
import xyz.i35ak47.hutao.utils.PropUtil;

public class Main extends ListenerAdapter implements EventListener {

    public static BotUtil botUtil;

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static JDABuilder builder;

    @SuppressWarnings({"SpellCheckingInspection", "UnstableApiUsage"})
    public static void main(String[] args) throws LoginException {
        /*
         * Check prop config file
         */
        if (!FileUtil.checkIfFolderExists("configs")) {
            FileUtil.createFolder("configs");
            new PropUtil().createDefConfig();
            logger.warn("Setup bot info in configs folder!");
            System.exit(0);
        }

        /*
         * Setup bot token
         */
        try {
            botUtil = new BotUtil(Objects.requireNonNull(PropUtil.getProp("token")));
        } catch (BotTokenException botTokenException) {
            logger.error(botTokenException.getMessage());
            System.exit(1);
        }

        /*
         * Temp fix for opList
         */
        if (FileUtil.checkIfFolderExists("configs")) {
            if (!FileUtil.checkIfFileExists("configs/opList.json"))
                Shell.runBash("echo \"[]\" >> configs/opList.json");
        }

        /*
         * JDA Builder to create Bot service
         */
        builder = JDABuilder.createDefault(botUtil.getToken(), GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES);
        builder.disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOTE);
        builder.setActivity(Activity.listening("O-ya? O-ya-ya-ya?"));
        builder.addEventListeners(new Main());
        builder.build();

        /*
         * Load Class Loader class,
         * Based in VegaZS Bot's source.
         */
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();

        try {
            for (final ClassPath.ClassInfo info : ClassPath.from(loader).getTopLevelClasses()) {
                if (info.getName().startsWith("xyz.i35ak47.hutao.modules")) {
                    final Class<?> clasz = info.load();
                    try {
                        if (!clasz.getName().contains("base.Command")) logger.info("Detected module: " + clasz.getName().replace("xyz.i35ak47.", ""));
                    } catch (Exception exception) {
                        logger.error(exception.getMessage(), exception);
                    }
                }
            }
        } catch (IOException ignored) {}
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String[] msgComparableRaw = event.getMessage().getContentRaw().split(" ");

        switch (msgComparableRaw[0]) {
            case "!evox":
                logger.info("User " + event.getAuthor().getName() + " used the " + msgComparableRaw[0] + " command");
                runMethod(EvolutionX.class, event);
            break;

            case "!sxos":
                logger.info("User " + event.getAuthor().getName() + " used the " + msgComparableRaw[0] + " command");
                runMethod(StatiXOS.class, event);
            break;

            case "!ping":
                logger.info("User " + event.getAuthor().getName() + " used the " + msgComparableRaw[0] + " command");
                runMethod(Ping.class, event);
            break;

            case "!shell":
                logger.info("User " + event.getAuthor().getName() + " used the " + msgComparableRaw[0] + " command");
                runMethod(Shell.class, event);
            break;

            case "!id":
                logger.info("User " + event.getAuthor().getName() + " used the " + msgComparableRaw[0] + " command");
                runMethod(ID.class, event);
            break;

            case "!hello":
                logger.info("User " + event.getAuthor().getName() + " used the " + msgComparableRaw[0] + " command");
                runMethod(Hello.class, event);
            break;

            case "!echo":
                logger.info("User " + event.getAuthor().getName() + " used the " + msgComparableRaw[0] + " command");
                runMethod(Echo.class, event);
            break;

            case "!server":
                logger.info("User " + event.getAuthor().getName() + " used the " + msgComparableRaw[0] + " command");
                runMethod(ServerInfo.class, event);
            break;

            case "!eval":
                logger.info("User " + event.getAuthor().getName() + " used the " + msgComparableRaw[0] + " command");
                runMethod(Eval.class, event);
            break;

            case "!ban":
                logger.info("User " + event.getAuthor().getName() + " used the " + msgComparableRaw[0] + " command");
                runMethod(BanMember.class, event);
            break;

            case "!tban":
                logger.info("User " + event.getAuthor().getName() + " used the " + msgComparableRaw[0] + " command");
                runMethod(TempBanMember.class, event);
            break;

            case "!kick":
                logger.info("User " + event.getAuthor().getName() + " used the " + msgComparableRaw[0] + " command");
                runMethod(KickMember.class, event);
            break;

            case "!leave":
                logger.info("User " + event.getAuthor().getName() + " used the " + msgComparableRaw[0] + " command");
                runMethod(LeaveGuild.class, event);
            break;

            case "!guilds":
                logger.info("User " + event.getAuthor().getName() + " used the " + msgComparableRaw[0] + " command");
                runMethod(ListGuilds.class, event);
            break;

            case "!op":
                logger.info("User " + event.getAuthor().getName() + " used the " + msgComparableRaw[0] + " command");
                runMethod(OverPower.class, event);
            break;

            case "!stats":
                logger.info("User " + event.getAuthor().getName() + " used the " + msgComparableRaw[0] + " command");
                runMethod(Stats.class, event);
            break;

            case "!setavatar":
                logger.info("User " + event.getAuthor().getName() + " used the " + msgComparableRaw[0] + " command");
                runMethod(SetAvatar.class, event);
            break;

            default:
        }
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        logger.info("GuildJoin: I was added to the " + event.getGuild().getName() + "(" + event.getGuild().getId() + ") guild.");
    }

    @SuppressWarnings("rawtypes")
    private void runMethod(Class aClass, MessageReceivedEvent event) {
        try {
            /*
             * Based in VegaZS Bot's source.
             */
            Object instance = ((Class<?>) aClass).getDeclaredConstructor().newInstance();
            Method method = ((Class<?>) aClass).getDeclaredMethod("sendMessage", MessageReceivedEvent.class);
            method.invoke(instance, event);
        } catch (Exception exception) {
            logger.error("Maybe the class: " + aClass.getName() + " don't have MessageReceivedEvent() arg in it's sendMessage() method");
        }
    }
}
