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

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import xyz.i35ak47.hutao.modules.base.Command;
import xyz.i35ak47.hutao.utils.PropUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@SuppressWarnings("SpellCheckingInspection")
public class Shell extends Command {

    public Shell() {
        super("shell");
    }

    @Override
    public void sendMessage(MessageReceivedEvent event) {
        if (event.getAuthor().getId().equals(PropUtil.getProp("developer")) && !System.getProperty("os.name").startsWith("Windows")) {
            if (!event.getMessage().getContentRaw().equals("!shell")) {
                /*
                 * StringBuilder for text base
                 */
                StringBuilder baseMessage = new StringBuilder();
                baseMessage.append(":keyboard: | `").append(runBash("whoami")).append("`").append(" (`").append(runBash("uname -n")).append("`)").append(" ~ ").append(event.getMessage().getContentRaw().replace("!shell ", "")).append("\n");

                /*
                 * Send message with live output
                 */
                event.getChannel().sendMessage(baseMessage).queue(response -> {
                    try {
                        /*
                         * Process base
                         */
                        final ProcessBuilder processBuilder;
                        processBuilder = new ProcessBuilder("/bin/bash", "-c", event.getMessage().getContentRaw().replace("!shell ", ""));
                        processBuilder.redirectErrorStream(true);
                        Process process = processBuilder.start();

                        /*
                         * Input/Output base
                         */
                        InputStream inputStream = process.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        String line;

                        /*
                         * Use while() for edit message (Remember: We've limit for 2k of chars)
                         */
                        while ((line = bufferedReader.readLine()) != null) {
                            baseMessage.append(":page_facing_up: | `").append(line).append("`").append("\n");
                            if (!(baseMessage.length() > 2000)) response.editMessageFormat(String.valueOf(baseMessage)).queue();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } else {
            event.getChannel().sendMessage("Sorry " + event.getAuthor().getAsMention() + ", this command is limited to **developers**.").queue();
        }
    }

    public static String runBash(String command) {
        StringBuilder baseCommand = new StringBuilder();
        try {
            ProcessBuilder pb;
            pb = new ProcessBuilder("/bin/bash", "-c", command);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            InputStream is = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                baseCommand.append(line);
            }
            return String.valueOf(baseCommand);
        } catch (Exception ignored) {}
        return null;
    }
}