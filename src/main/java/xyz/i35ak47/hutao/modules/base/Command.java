package xyz.i35ak47.hutao.modules.base;

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

@SuppressWarnings("unused")
public abstract class Command {
    private String command;

    public Command(String command) {
        this.command = command;
    }

    public String getAlias() {
        return command;
    }

    public void setAlias(String command) {
        this.command = command;
    }

    public abstract void sendMessage(MessageReceivedEvent event);
}