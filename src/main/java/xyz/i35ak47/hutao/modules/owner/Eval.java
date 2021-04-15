package xyz.i35ak47.hutao.modules.owner;

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

/*
 * Based on: https://github.com/DV8FromTheWorld/Yui/blob/master/src/main/java/net/dv8tion/discord/commands/EvalCommand.java
 * All credits to: Austin Keener and and its (Eval command) contributors
 */

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.i35ak47.hutao.modules.base.Command;
import xyz.i35ak47.hutao.utils.PropUtil;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class Eval extends Command {

    private static final Logger logger = LoggerFactory.getLogger(Eval.class);

    public Eval() {
        super("eval");
    }

    @SuppressWarnings("SpellCheckingInspection")
    @Override
    public void sendMessage(MessageReceivedEvent event) {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        String[] msgComparableRaw = event.getMessage().getContentRaw().split(" ");

        if (OverPower.isOp(event.getAuthor().getId())) {
            try {
                engine.eval("var imports = new JavaImporter(" +
                        "java," +
                        "java.io," +
                        "java.lang," +
                        "java.util," +
                        "Packages.xyz.i35ak47.lyconsky" +
                        "Packages.net.dv8tion.jda.api," +
                        "Packages.net.dv8tion.jda.api.entities," +
                        "Packages.net.dv8tion.jda.api.entities.impl," +
                        "Packages.net.dv8tion.jda.api.managers," +
                        "Packages.net.dv8tion.jda.api.managers.impl," +
                        "Packages.net.dv8tion.jda.api.utils);"
                );

                engine.put("event", event);
                engine.put("message", event.getMessage());
                engine.put("channel", event.getChannel());
                engine.put("args", msgComparableRaw);

                /*
                 * Avoid abuse/hacking from OP users
                 */
                if (event.getAuthor().getId().equals(PropUtil.getProp("developer"))) {
                    engine.put("api", event.getJDA());
                    engine.put("engine", engine);
                }

                if (event.isFromType(ChannelType.TEXT)) {
                    engine.put("guild", event.getGuild());
                    engine.put("member", event.getMember());
                }

                String command = event.getMessage().getContentDisplay().substring(msgComparableRaw[0].length());
                command = command.replace("Shell", "").replace("runBash", "");

                Object out = engine.eval(
                        "(function() {" +
                                "with (imports) {\n" +
                                command +
                                "\n}" +
                                "})();"
                );
                event.getChannel().sendMessage(out == null ? ":white_check_mark: | Executed without error." : out.toString()).queue();
            } catch (Exception exception) {
                event.getChannel().sendMessage(":exclamation: | `" + exception.getMessage() + "`").queue();
                logger.error(exception.getMessage());
            }
        }
    }
}