package xyz.i35ak47.hutao.modules.android;

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
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.i35ak47.hutao.modules.base.Command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Random;

@SuppressWarnings("SpellCheckingInspection")
public class StatiXOS extends Command {

    private static final Logger logger = LoggerFactory.getLogger(StatiXOS.class);

    /*
     * ── Bugs
     * 1st
     *  |-> When converting Timestamp to Date, the year, month and day are natively incorrect for an unknown reason, ex:
     * 1970-01-19 14:22:18.84 (Of Fajita's StatiXOS ROM, ID: 055b2f5e28c81cc4b63641c1997a5530)
     */

    public StatiXOS() {
        super("sxos");
    }

    @Override
    public void sendMessage(MessageReceivedEvent event) {
        String[] msgComparableRaw = event.getMessage().getContentRaw().split(" ");

        if (event.getMessage().getContentRaw().contains(" ")) {
            event.getChannel().sendMessage("Wait...").queue(response -> {
                try {
                    URL urlBase = new URL("https://downloads.statixos.com/json/" + msgComparableRaw[1] + ".json");
                    HttpURLConnection connection = (HttpURLConnection) urlBase.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();

                    StringBuilder stringBuilder = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    logger.info("Response Code: " + connection.getResponseCode());

                    String jsonResponse;

                    while ((jsonResponse = bufferedReader.readLine()) != null) {
                        stringBuilder.append(jsonResponse).append("\n");
                    }
                    bufferedReader.close();

                    if (connection.getResponseCode() == 200) {
                        /*
                         * Delete the message
                         */
                        response.delete().queue();

                        /*
                         * Set json(Object|Array) var
                         */
                        JSONObject jsonObject = new JSONObject(String.valueOf(stringBuilder));
                        JSONArray jsonArray = jsonObject.getJSONArray("response");

                        /*
                         * Timestamp and EmbedBuilder var
                         */
                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        Timestamp timestamp = new Timestamp(jsonArray.getJSONObject(0).getLong("datetime"));

                        /*
                         * Random color setup
                         */
                        Random random = new Random();
                        int low = 0, high = 99999;
                        int randomColor = random.nextInt(high - low) + low;

                        /*
                         * Get/set info
                         */
                        embedBuilder.setAuthor("StatiXOS", "https://avatars.githubusercontent.com/u/36259882?s=200&v=4", "https://avatars.githubusercontent.com/u/36259882?s=200&v=4");
                        embedBuilder.setThumbnail("https://avatars.githubusercontent.com/u/36259882?s=200&v=4");
                        embedBuilder.setFooter("StatiXOS", "https://avatars.githubusercontent.com/u/36259882?s=200&v=4");

                        embedBuilder.addField(":date: | Build Date", String.valueOf(timestamp), false);
                        embedBuilder.addField(":round_pushpin: | Version", jsonArray.getJSONObject(0).getString("version"), false);
                        embedBuilder.addField(":page_facing_up: | File Name", jsonArray.getJSONObject(0).getString("filename"), false);
                        embedBuilder.addField(":interrobang: | ROM Type", jsonArray.getJSONObject(0).getString("romtype"), false);
                        embedBuilder.addField(":paperclips: | ID", jsonArray.getJSONObject(0).getString("id"), false);
                        embedBuilder.addField(":link: | URL", jsonArray.getJSONObject(0).getString("url"), false);
                        embedBuilder.setColor(randomColor);

                        /*
                         * Send the message
                         */
                        event.getChannel().sendMessage(embedBuilder.build()).queue();
                    } else {
                        response.editMessage("This device may not officially exist on the StatiXOS download site, please contact a maintainer.").queue();
                    }
                } catch (IOException ioException) {
                    response.editMessage("This device may not officially exist on the StatiXOS download site, please contact a maintainer.").queue();
                    logger.error(msgComparableRaw[1] + " <- This codename may not exist in the official StatiXOS download site");
                }
            });
        } else {
            event.getChannel().sendMessage("Sorry " + event.getAuthor().getAsMention() + ", you must specify the device's codename.").queue();
        }
    }
}