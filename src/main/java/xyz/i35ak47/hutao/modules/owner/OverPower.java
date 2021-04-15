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

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.i35ak47.hutao.modules.base.Command;
import xyz.i35ak47.hutao.utils.FileUtil;
import xyz.i35ak47.hutao.utils.JsonUtil;
import xyz.i35ak47.hutao.utils.PropUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class OverPower extends Command {

    private static final Logger logger = LoggerFactory.getLogger(OverPower.class);

    public OverPower() {
        super("op");
    }

    @Override
    public void sendMessage(MessageReceivedEvent event) {
        String[] msgComparableRaw = event.getMessage().getContentRaw().split(" ");

        if (event.getAuthor().getId().equals(PropUtil.getProp("developer"))) {
            if (event.getMessage().getContentDisplay().contains(" ")) {
                if (msgComparableRaw[1].equals("-r")) {
                    if (msgComparableRaw[2].startsWith("<@")) {
                        if (event.getMessage().getMentionedMembers().get(0).getUser().getId().equals(event.getJDA().getSelfUser().getId())) {
                            event.getChannel().sendMessage("There is no need to \"try to remove me\" from the **OP** level, because I don't have it myself.").queue();
                        } else if (event.getMessage().getMentionedMembers().get(0).getUser().getId().equals(PropUtil.getProp("developer"))) {
                            event.getChannel().sendMessage("Are you trying to remove my own developer from the **OP** level? How wrong.").queue();
                        } else {
                            if (!isOp(event.getMessage().getMentionedMembers().get(0).getUser().getId())) {
                                event.getChannel().sendMessage("This user has no **OP** level.").queue();
                            } else {
                                removeOP(event.getMessage().getMentionedMembers().get(0).getUser().getId());
                                event.getChannel().sendMessage("Done! Now the user " + event.getMessage().getMentionedMembers().get(0).getUser().getAsMention() + " no longer has the **OP** level.").queue();
                            }
                        }
                    } else {
                        event.getChannel().sendMessage("A **user mention** is missing.").queue();
                    }
                } else if (msgComparableRaw[1].equals("-a")) {
                    if (msgComparableRaw[2].startsWith("<@")) {
                        if (event.getMessage().getMentionedMembers().get(0).getUser().getId().equals(event.getJDA().getSelfUser().getId())) {
                            event.getChannel().sendMessage("You are trying to add me to the **OP** level, this is weird.").queue();
                        } else if (event.getMessage().getMentionedMembers().get(0).getUser().getId().equals(PropUtil.getProp("developer"))) {
                            event.getChannel().sendMessage("This user is my developer, an **OP** level is not required.").queue();
                        } else {
                            if (isOp(event.getMessage().getMentionedMembers().get(0).getUser().getId())) {
                                event.getChannel().sendMessage("This user already has an **OP** level.").queue();
                            } else {
                                addOpPermission(event.getMessage().getMentionedMembers().get(0).getUser().getId());
                                event.getChannel().sendMessage("Done! Now the user " + event.getMessage().getMentionedMembers().get(0).getUser().getAsMention() + " has the **OP** level.").queue();
                            }
                        }
                    } else {
                        event.getChannel().sendMessage("A **user mention** is missing.").queue();
                    }
                } else {
                    event.getChannel().sendMessage("Well, there is an argument missing, do you want to remove (`-r`) or add (`-a`) a user?").queue();
                }
            } else {
                event.getChannel().sendMessage("Sorry " + event.getAuthor().getAsMention() + ", it is not possible to proceed with the incorrect use.").queue();
            }
        } else {
            event.getChannel().sendMessage("Sorry " + event.getAuthor().getAsMention() + ", this command is limited to **developers**.").queue();
        }
    }

    public static void removeOP(String id) {
        /*
         * Lazy method
         */
        Shell.runBash("sed -i \"s/" + id + "//g\" configs/opList.json");
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void addOpPermission(String id) {
        try {
            if (FileUtil.checkIfFileExists("configs/opList.json")) {
                ArrayList arrayList = JsonUtil.getArrayFromJSON("configs/opList.json");
                if (arrayList != null) {
                    arrayList.add(id);
                }
                JsonUtil.writeArrayToJSON(arrayList, "configs/opList.json");
            } else {
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add(id);
                JsonUtil.writeArrayToJSON(arrayList, "configs/opList.json");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static boolean isOp(String id) {
        if (Objects.equals(PropUtil.getProp("developer"), id))
            return true;

        String portConfigFile = "configs/opList.json";
        return Arrays.asList(Objects.requireNonNull(JsonUtil.getArrayFromJSON(portConfigFile)).toArray()).contains(id);
    }
}