/*
 * Copyright (c) 2020.
 * Copyright by Tim and Felix
 */

package de.ft.interitus.datamanager.programmdata;

import com.kotcrab.vis.ui.util.dialog.Dialogs;
import de.ft.interitus.Settings;
import de.ft.interitus.UI.UI;
import de.ft.interitus.Var;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class Updater {

    public static void check(final boolean showincaseoffalse) {

        //Noch nicht commited da hier noch viele änderungen passieren

        Thread checkupdate;
        checkupdate = new Thread() {


            //TODO Hier caching problem
            @Override
            public void run() {
                try {

                    BufferedReader br = null;
                    InputStreamReader isr = null;
                    URL url = null;

                    String urlString = Settings.updateurl;

                    try {
                        url = new URL(urlString);
                    } catch (MalformedURLException e) {
                        if (showincaseoffalse) {

                            Dialogs.showErrorDialog(UI.stage, "Der Update-Server reagiert leider nicht oder du hast einfach keine Internetverbindung");
                        }
                    }


                    try {

                        isr = new InputStreamReader(url.openStream());


                    } catch (IOException e) {
                        if (showincaseoffalse) {
                            Dialogs.showErrorDialog(UI.stage, "Der Update-Server reagiert leider nicht oder du hast einfach keine Internetverbindung");
                        }
                    }

                    br = new BufferedReader(isr);


                    try {
                        double version_get = Double.parseDouble(br.readLine());
                        System.out.println(version_get);
                        if (version_get == Var.PROGRAMM_VERSION_ID) {
                            if (showincaseoffalse) {
                                Dialogs.showOKDialog(UI.stage, "Kein Update", "Du bist auf dem neusten Stand");
                            }

                        } else if (version_get < Var.PROGRAMM_VERSION_ID) {
                            if (showincaseoffalse) {
                                Dialogs.showErrorDialog(UI.stage, "Bitte Kontaktiere die Herausgeber dieser Software! Hier liegt ein Fehler vor der alle Nutzer betrifft!");
                            }
                        } else if (version_get > Var.PROGRAMM_VERSION_ID) {
                            Dialogs.showOKDialog(UI.stage, "Update", "Hier liegt ein Update vor!"); //TODO hier update vorgang einleiten
                        }
                    } catch (IOException e) {
                        if (showincaseoffalse) {
                            Dialogs.showErrorDialog(UI.stage, "Der Update-Server reagiert leider nicht oder du hast einfach keine Internetverbindung");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace(); //for debug to find errors
                }
            }
        };

        checkupdate.start();


    }
}
