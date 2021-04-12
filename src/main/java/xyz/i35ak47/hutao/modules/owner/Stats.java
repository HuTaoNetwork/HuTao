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

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import xyz.i35ak47.hutao.modules.base.Command;
import xyz.i35ak47.hutao.utils.PropUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Stats extends Command {

    public Stats() {
        super("stats");
    }

    @Override
    public void sendMessage(MessageReceivedEvent event) {
        if (event.getAuthor().getId().equals(PropUtil.getProp("developer"))) {
            /*
             * EmbedBuilder
             */
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
            embedBuilder.setAuthor(event.getJDA().getSelfUser().getName(), event.getJDA().getSelfUser().getAvatarUrl(), event.getJDA().getSelfUser().getAvatarUrl());
            embedBuilder.setThumbnail(event.getJDA().getSelfUser().getAvatarUrl());
            embedBuilder.setColor(randomColor);

            /*
             * Get user count
             */
            List<Guild> guilds = new ArrayList<>(event.getJDA().getGuilds());
            int usersCount = 0;
            for (Guild guild : guilds) {
                usersCount = usersCount + guild.getMemberCount();
            }

            /*
             * Prepare embed
             */
            embedBuilder.addField(":door: | Guilds Count", String.valueOf(event.getJDA().getGuilds().size()), true);
            embedBuilder.addField(":person_frowning: | Users Count", String.valueOf(usersCount), true);
            embedBuilder.addField(":newspaper: | Text Channels Count", String.valueOf(event.getJDA().getTextChannels().size()), true);
            embedBuilder.addField(":microphone2: | Voice Channels Count", String.valueOf(event.getJDA().getVoiceChannels().size()), true);
            embedBuilder.addField(":recycle: | Shard", String.valueOf(event.getJDA().getShardInfo().getShardId()), false);
            embedBuilder.setFooter(event.getAuthor().getAsTag(), event.getAuthor().getAvatarUrl());

            /*
             * Send the message
             */
            event.getChannel().sendMessage(embedBuilder.build()).queue();
        } else {
            event.getChannel().sendMessage("Sorry " + event.getAuthor().getAsMention() + ", this command is limited to **developers**.").queue();
        }
    }
}
