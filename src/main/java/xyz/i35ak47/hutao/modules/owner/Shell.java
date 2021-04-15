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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.i35ak47.hutao.modules.base.Command;
import xyz.i35ak47.hutao.utils.PropUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@SuppressWarnings("SpellCheckingInspection")
public class Shell extends Command {

    private static final Logger logger = LoggerFactory.getLogger(Shell.class);

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
                    InputStream inputStream = null;
                    InputStreamReader inputStreamReader = null;
                    BufferedReader bufferedReader = null;
                    try {
                        /*
                         * Process base
                         */
                        final ProcessBuilder processBuilder;
                        processBuilder = new ProcessBuilder("/bin/bash", "-c", event.getMessage().getContentRaw().replace("!shell ", ""));
                        processBuilder.redirectErrorStream(true);
                        Process process = processBuilder.start();

                        /*
                         * Stream base
                         */
                        inputStream = process.getInputStream();
                        inputStreamReader = new InputStreamReader(inputStream);
                        bufferedReader = new BufferedReader(inputStreamReader);
                        String line;

                        /*
                         * Use while() for edit message (Remember: We've limit for 2k of chars)
                         */
                        while ((line = bufferedReader.readLine()) != null) {
                            baseMessage.append(":page_facing_up: | `").append(line).append("`").append("\n");
                            if (!(baseMessage.length() > 2000)) response.editMessageFormat(String.valueOf(baseMessage)).queue();
                        }
                    } catch (Exception exception) {
                        logger.error(exception.getMessage(), exception);
                    } finally {
                        if (inputStream != null && inputStreamReader != null && bufferedReader != null) {
                            try {
                                /*
                                 * Close stream, due 'lack' of memory
                                 */
                                inputStream.close();
                                inputStreamReader.close();
                                bufferedReader.close();
                            } catch (IOException ioException) {
                                logger.error(ioException.getMessage(), ioException);
                            }
                        }
                    }
                });
            }
        } else {
            event.getChannel().sendMessage("Sorry " + event.getAuthor().getAsMention() + ", this command is limited to **developers**.").queue();
        }
    }

    public static String runBash(String command) {
        StringBuilder baseCommand = new StringBuilder();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            /*
             * Process base
             */
            ProcessBuilder pb;
            pb = new ProcessBuilder("/bin/bash", "-c", command);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            /*
             * Stream base
             */
            inputStream = process.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                baseCommand.append(line);
            }
            return String.valueOf(baseCommand);
        } catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
        } finally {
            if (inputStream != null && inputStreamReader != null && bufferedReader != null) {
                try {
                    /*
                     * Close stream, due 'lack' of memory
                     */
                    inputStream.close();
                    inputStreamReader.close();
                    bufferedReader.close();
                } catch (IOException ioException) {
                    logger.error(ioException.getMessage(), ioException);
                }
            }
        }
        return null;
    }
}