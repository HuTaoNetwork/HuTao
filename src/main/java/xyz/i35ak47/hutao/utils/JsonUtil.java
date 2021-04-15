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

/*
 * Based on Vega's Bot3 base, I'm lazy to make my own.
 */

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;

@SuppressWarnings("rawtypes")
public class JsonUtil {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public static void writeArrayToJSON(ArrayList<String> values, String file) {
        Gson gson = new Gson();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(gson.toJson(values).getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static ArrayList getArrayFromJSON(String file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String json = stringBuilder.toString();
            Gson gson = new Gson();
            return gson.fromJson(json, ArrayList.class);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /*
     * Lazy methods
     */

    public static String getValueFromJSON(String json, String value) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return jsonObject.getString(value);
        } catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
        }
        return null;
    }

    public static long getLongFromJSON(String json, String value) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return jsonObject.getLong(value);
        } catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
        }
        return 0;
    }
}