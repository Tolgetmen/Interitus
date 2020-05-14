package de.ft.interitus;

import com.badlogic.gdx.Game;
import com.kotcrab.vis.ui.VisUI;
import de.ft.interitus.data.programm.Data;
import de.ft.interitus.data.user.experience.ExperienceManager;
import de.ft.interitus.events.EventVar;
import de.ft.interitus.events.UI.UILoadEvent;
import de.ft.interitus.loading.Loading;
import de.ft.interitus.plugin.PluginManagerHandler;
import de.ft.interitus.plugin.store.ReadStorePlugins;
import de.ft.interitus.projecttypes.device.BlockTypes.Init;
import de.ft.interitus.utils.NetworkScan;
import org.lwjgl.openal.AL;

public class Programm extends Game {

    public static Programm INSTANCE;
    public static boolean inLoading = true;


    public Programm() {


        INSTANCE = this;
    }

    @Override
    public void create() {

        Init.initBlocks();

        //  SSHConnection.update("192.168.2.112","pi","Pi-Server");
        Thread loadplugins = new Thread() {
            @Override
            public void run() {
                Var.pluginManager = new PluginManagerHandler();
                DisplayErrors.error = Var.pluginManager.init();

            }
        };
        VisUI.load(VisUI.SkinScale.X1);

        EventVar.uiEventManager.UILoadEvent(new UILoadEvent(this));

        try {
            loadplugins.start(); //Plugins laden
            ReadStorePlugins.read(); //Ersten 10 Plugins im Store laden
        } catch (Exception e) {
            Var.nointernetconnection = true;
        }

        Thread seachnetwork = new Thread() {
            @Override
            public void run() {
                NetworkScan.get();
            }
        };
        seachnetwork.start();
        Data.init();
        ExperienceManager.init();
        setScreen(new Loading());
    }


    public void dispose() {

        ThreadManager.stopall();

        Data.close();

        AL.destroy(); //Destroy Sound System
       System.exit(0);

    }


    public void pause() {

    }


}


