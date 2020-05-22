package de.ft.interitus.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.ft.interitus.ProgrammingSpace;

import java.util.Vector;

public class Unproject {

   static Vector2 temp1 = new Vector2();
   static Vector3 temp3 = new Vector3();
   static Vector3 temp4 = new Vector3();

    public static Vector2 unproject() {
        return temp1.set(ProgrammingSpace.cam.unproject(temp3.set(Gdx.input.getX(),Gdx.input.getY(),0)).x,ProgrammingSpace.cam.unproject(temp4.set(Gdx.input.getX(),Gdx.input.getY(),0)).y);
    }
}
