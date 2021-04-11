package xyz.i35ak47.hutao.modules.info;

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

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import xyz.i35ak47.hutao.modules.base.Command;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ServerInfo extends Command {

    public ServerInfo() {
        super("server");
    }

    @Override
    public void sendMessage(MessageReceivedEvent event) {
        if (!event.isFromType(ChannelType.PRIVATE)) {
            /*
             * Guild/EmbedBuilder
             */
            Guild guild = event.getGuild();
            EmbedBuilder embedBuilder = new EmbedBuilder();

            /*
             * Random color setup
             */
            Random random = new Random();
            int low = 0, high = 99999;
            int randomColor = random.nextInt(high - low) + low;

            /*
             * Set/get server information
             */
            embedBuilder.setAuthor(guild.getName(), guild.getIconUrl(), guild.getIconUrl());
            embedBuilder.setThumbnail(guild.getIconUrl());
            embedBuilder.setColor(randomColor);

            /*
             * Prepare embed
             */
            embedBuilder.addField(":eyes: | Members Count", String.valueOf(guild.getMemberCount()), false);
            embedBuilder.addField(":newspaper: | Text Channels Count", String.valueOf(guild.getTextChannels().size()), false);
            embedBuilder.addField(":fire: | Boosters Count", String.valueOf(guild.getBoostCount()), false);
            embedBuilder.addField(":clock3: | Foundation Day", new SimpleDateFormat("yyyy, MMMM dd").format(new Date(guild.getTimeCreated().toInstant().toEpochMilli())), false);
            embedBuilder.setFooter("Shared information may be incomplete or wrong", guild.getIconUrl());

            /*
             * Send the message
             */
            event.getChannel().sendMessage(embedBuilder.build()).queue();
        } else {
            /*
             * Okay so, the guy wants "server" command in PM xd
             */
            event.getChannel().sendMessage(":thinking: | This command is only available for Guilds.").queue();
        }
    }
}