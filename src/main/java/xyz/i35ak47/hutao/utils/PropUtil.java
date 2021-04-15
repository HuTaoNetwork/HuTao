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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class PropUtil {

    private static final Logger logger = LoggerFactory.getLogger(PropUtil.class);

    public void createDefConfig() {
        try {
            /*
             * Create Properties obj
             */
            Properties saveProps = new Properties();

            /*
             * Check if exists config folder
             */
            FileUtil.createFolder("configs");

            /*
             * Set prop
             */
            saveProps.setProperty("token", "");
            saveProps.setProperty("developer", "");
            FileOutputStream fileOutputStream = new FileOutputStream("configs/config.prop");
            saveProps.store(fileOutputStream, null);
            fileOutputStream.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static String getProp(String prop) {
        try {
            Properties getProps = new Properties();
            getProps.load(new FileInputStream("configs/config.prop"));
            return getProps.getProperty(prop);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}