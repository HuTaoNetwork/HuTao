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
 * Based on: https://github.com/jagrosh/MusicBot/blob/master/src/main/java/com/jagrosh/jmusicbot/commands/owner/SetavatarCmd.java#L44-L65
 * to: https://github.com/jagrosh/MusicBot/blob/master/src/main/java/com/jagrosh/jmusicbot/utils/OtherUtil.java#L95-L108
 * All credits to: John <john.a.grosh@gmail.com>
 */

import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.i35ak47.hutao.modules.base.Command;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class SetAvatar extends Command {

    private static final Logger logger = LoggerFactory.getLogger(SetAvatar.class);

    @SuppressWarnings("SpellCheckingInspection")
    public SetAvatar() {
        super("setavatar");
    }

    @Override
    public void sendMessage(MessageReceivedEvent event) {
        String[] msgComparableRaw = event.getMessage().getContentRaw().split(" ");
        String urlBase = null;
        boolean pass;

        if (OverPower.isOp(event.getAuthor().getId())) {
            if (!event.getMessage().getAttachments().isEmpty() && event.getMessage().getAttachments().get(0).isImage()) {
                urlBase = event.getMessage().getAttachments().get(0).getUrl();
                pass = true;
            } else if (event.getMessage().getContentDisplay().contains(" ")) {
                urlBase = msgComparableRaw[1];
                pass = true;
            } else {
                event.getChannel().sendMessage("You can upload an image with the command caption, or use the image link.").queue();
                pass = false;
            }

            if (pass) {
                InputStream inputStream = ImageFromURL(urlBase);
                if (inputStream == null) {
                    event.getChannel().sendMessage("Oops... **Invalid** URL! Check if it's correct.").queue();
                } else {
                    try {
                        event.getJDA().getSelfUser().getManager().setAvatar(Icon.from(inputStream)).queue(
                                response -> event.getChannel().sendMessage("Done! Photo changed successfully.").queue(),
                                error -> event.getChannel().sendMessage("Something is wrong! Error:\n`" + error.getMessage() + "`").queue());
                        inputStream.close();
                    } catch (IOException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        } else {
            event.getChannel().sendMessage("Sorry " + event.getAuthor().getAsMention() + ", this command is limited to **developers**.").queue();
        }
    }

    @SuppressWarnings("SpellCheckingInspection")
    private InputStream ImageFromURL(String url) {
        if (url == null)
            return null;

        try {
            URL u = new URL(url);
            URLConnection urlConnection = u.openConnection();
            urlConnection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.112 Safari/537.36");
            return urlConnection.getInputStream();
        } catch (IOException | IllegalArgumentException error) {
            logger.error(error.getMessage(), error);
        }
        return null;
    }
}