package de.ft.robocontrol;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import de.ft.robocontrol.data.programm.Data;
import de.ft.robocontrol.loading.Loading;

public class Programm extends Game {

    public static Programm INSTANCE;
    public static boolean inLoading = true;

    public Programm() {

        INSTANCE = this;
    }
    @Override
    public void create() {
        setScreen(new Loading());
    }


    public void dispose() {
        System.out.println("Save");
        Data.close();
        System.out.println("saved");
    }


    public void pause() {

    }
}


