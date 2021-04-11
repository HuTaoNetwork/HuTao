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

import java.util.ArrayList;
import java.util.List;

public class ListGuilds extends Command {

    public ListGuilds() {
        super("guilds");
    }

    @Override
    public void sendMessage(MessageReceivedEvent event) {
        if (event.getAuthor().getId().equals(PropUtil.getProp("developer"))) {
            StringBuilder stringBuilder = new StringBuilder();

            List<Guild> guilds = new ArrayList<>(event.getJDA().getGuilds());
            for (Guild guild : guilds) {
                stringBuilder.append("**").append(guild.getName()).append("**")
                        .append(" - ").append("`").append(guild.getId()).append("`")
                        .append("\n**Users**: `").append(guild.getMemberCount()).append("`")
                        .append("\n**Boosters**: `").append(guild.getBoostCount()).append("`")
                        .append("\n\n");
            }
            event.getChannel().sendMessage(stringBuilder).queue();
        } else {
            event.getChannel().sendMessage("Sorry " + event.getAuthor().getAsMention() + ", this command is limited to **developers**.").queue();
        }
    }
}