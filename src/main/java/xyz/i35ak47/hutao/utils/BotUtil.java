package xyz.i35ak47.hutao.utils;

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

import xyz.i35ak47.hutao.exceptions.BotTokenException;

public class BotUtil {

    public String token;

    public BotUtil(String token) throws BotTokenException {
        if (!(token.length() >= 59)) {
            throw new BotTokenException("The bot token usually has a length greater than or equal to 59 characters");
        } else {
            this.token = token;
        }
    }

    public String getToken() {
        return token;
    }
}