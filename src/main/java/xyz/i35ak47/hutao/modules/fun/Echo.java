package xyz.i35ak47.hutao.modules.fun;

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

public class Echo extends Command {

    public Echo() {
        super("echo");
    }

    @Override
    public void sendMessage(MessageReceivedEvent event) {
        if (!event.getMessage().getContentRaw().equals("!echo")) event.getChannel().sendMessage(event.getMessage().getContentRaw().replace("!echo ", "")).queue();
    }
}