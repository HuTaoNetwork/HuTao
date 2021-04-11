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

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import xyz.i35ak47.hutao.modules.base.Command;
import xyz.i35ak47.hutao.utils.PropUtil;

import java.util.Objects;

public class LeaveGuild extends Command {

    public LeaveGuild() {
        super("leave");
    }

    @Override
    public void sendMessage(MessageReceivedEvent event) {
        String[] msgComparableRaw = event.getMessage().getContentRaw().split(" ");

        if (event.getAuthor().getId().equals(PropUtil.getProp("developer"))) {
            if (event.getMessage().getContentDisplay().contains(" ")) {
                try {
                    Guild guild = event.getJDA().getGuildById(msgComparableRaw[1]);
                    String guildName = Objects.requireNonNull(guild).getName();
                    guild.leave().queue(response -> event.getChannel().sendMessage("Done! I left the server: " + guildName).queue());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                event.getChannel().sendMessage("Sorry " + event.getAuthor().getAsMention() + ", can't leave due wrong usage.").queue();
            }
        } else {
            event.getChannel().sendMessage("Sorry " + event.getAuthor().getAsMention() + ", this command is limited to **developers**.").queue();
        }
    }
}