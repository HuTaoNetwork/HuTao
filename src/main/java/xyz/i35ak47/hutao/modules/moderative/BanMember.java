package xyz.i35ak47.hutao.modules.moderative;

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
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import xyz.i35ak47.hutao.modules.base.Command;
import xyz.i35ak47.hutao.modules.owner.OverPower;
import xyz.i35ak47.hutao.utils.PropUtil;

import java.util.Objects;
import java.util.Random;

public class BanMember extends Command {

    /*
     * Command "ban" specially inspired by https://github.com/LorranyNetwork/Lorrany/blob/master/modules/ban.js
     */

    public BanMember() {
        super("ban");
    }

    @Override
    public void sendMessage(MessageReceivedEvent event) {
        String[] msgComparableRaw = event.getMessage().getContentRaw().split(" ");

        if (!event.isFromType(ChannelType.PRIVATE)) {
            if (event.getGuild().getSelfMember().hasPermission(Permission.BAN_MEMBERS)) {
                if (Objects.requireNonNull(event.getMember()).hasPermission(Permission.BAN_MEMBERS)
                        || Objects.requireNonNull(event.getMember()).hasPermission(Permission.ADMINISTRATOR)
                        || OverPower.isOp(event.getAuthor().getId())) {
                    if (msgComparableRaw.length >= 2 && msgComparableRaw[1].startsWith("<")) {
                        if (event.getMessage().getMentionedMembers().get(0).getId().equals(event.getAuthor().getId())) {
                            event.getChannel().sendMessage("Are you trying to ban yourself? This is bizarre!").queue();
                        } else if (event.getMessage().getMentionedMembers().get(0).getId().equals(event.getJDA().getSelfUser().getId())) {
                            event.getChannel().sendMessage("Well, that sounds weird to me, do you want me to ban myself? Oh my...").queue();
                        } else if (event.getMessage().getMentionedMembers().get(0).getId().equals(PropUtil.getProp("developer"))) {
                            event.getChannel().sendMessage("Sorry " + event.getAuthor().getAsMention() + ", this user is my developer, I can't ban.").queue();
                        } else if (event.getMessage().getMentionedMembers().get(0).hasPermission(Permission.BAN_MEMBERS)
                                || event.getMessage().getMentionedMembers().get(0).hasPermission(Permission.ADMINISTRATOR)) {
                            event.getChannel().sendMessage("Sorry " + event.getAuthor().getAsMention() + ", this user appears to have the same administrative permissions.").queue();
                        } else {
                            /*
                             * Set member and embed var
                             */
                            EmbedBuilder embedBuilder = new EmbedBuilder();
                            Member user = event.getMessage().getMentionedMembers().get(0);

                            /*
                             * Based on: https://github.com/VeloshGSIs/RequestBotJava/blob/master/src/main/java/org/velosh/requestbot/iqx/Bot.java#L80
                             */
                            StringBuilder str = new StringBuilder();
                            String reason = "";
                            for (int i = 2; i < msgComparableRaw.length; i++) {
                                str.append(msgComparableRaw[i]).append(" ");
                                reason = String.valueOf(str);
                            }
                            if (reason.equals("")) reason = "Information not shared by the user.";

                            /*
                             * Random color setup
                             */
                            Random random = new Random();
                            int low = 0, high = 99999;
                            int randomColor = random.nextInt(high - low) + low;

                            /*
                             * I'll get my sledgehammer xd, brazilian meme
                             */
                            String finalReason = reason;
                            event.getGuild().ban(user, 0, reason).queue(
                            response -> {
                                /*
                                 * Send ban message
                                 */
                                embedBuilder.setTitle(":hammer: | **BAN**");
                                embedBuilder.addField(":thinking: | Banned", user.getUser().getName(), false);
                                embedBuilder.addField(":oncoming_police_car: | Staff", event.getMember().getUser().getName(), false);
                                embedBuilder.addField(":exclamation: | Reason", finalReason, false);
                                embedBuilder.setFooter(event.getMember().getUser().getAsTag(), event.getMember().getUser().getAvatarUrl());
                                embedBuilder.setColor(randomColor);
                                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
                            },
                            error -> {
                                if (!error.getMessage().contains("50013")) {
                                    event.getChannel().sendMessage("Failed to ban, maybe you can put me at the top of the hierarchy, if it doesn't help, here's the error:\n`" + error.getMessage() + "`").queue();
                                } else {
                                    event.getChannel().sendMessage("Failed to ban, permission to ban is missing, ask someone higher than me to put that permission.").queue();
                                }
                            });
                        }
                    } else {
                        event.getChannel().sendMessage("Sorry " + event.getAuthor().getAsMention() + ", can't ban due wrong usage.").queue();
                    }
                } else {
                    event.getChannel().sendMessage("Sorry " + event.getAuthor().getAsMention() + ", you don't have permission for it.").queue();
                }
            } else {
                event.getChannel().sendMessage("I don't have ban permission! Give me permission and run the command again.").queue();
            }
        }
    }
}