/*
 * Copyright (c) 2020.
 * Copyright by Tim and Felix
 */

package de.ft.interitus.plugin.store;

import de.ft.interitus.DisplayErrors;
import de.ft.interitus.Programm;
import de.ft.interitus.utils.DownloadFile;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ReadStorePlugins {
    static String jsonfile = null;
    static int plugincounter = 0;

    public static void read() {


        try {
            jsonfile = DownloadFile.downloadFile("https://raw.githubusercontent.com/FT-Interitus/Interitus-Plugins/master/plugins.json");
        } catch (IOException e) {

        }
        //System.out.println(jsonfile);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonfile);
        } catch (JSONException e) {
            DisplayErrors.error = e;
            e.printStackTrace();
        }


        try {
            while (true) {
                if (!jsonObject.has("plugin" + plugincounter)) {
                    break;
                }
                plugincounter++;
            }

            for (int i = 0; i < plugincounter; i++) {


                int id = jsonObject.getJSONObject("plugin" + i).getInt("id");
                String name = jsonObject.getJSONObject("plugin" + i).getString("name");
                double version = jsonObject.getJSONObject("plugin" + i).getDouble("version");
                String path = jsonObject.getJSONObject("plugin" + i).getString("path");
                String description = jsonObject.getJSONObject("plugin" + i).getString("description");
                String image = jsonObject.getJSONObject("plugin" + i).getString("image");
                String detailed_description = jsonObject.getJSONObject("plugin" + i).getString("detailed_description");
                StorePluginsVar.pluginEntries.add(new StorePluginEntry(id, name, version, path, description, image, detailed_description));


            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        Programm.logger.config("Loaded 10 Store Plugin Entrys");
    }


}
