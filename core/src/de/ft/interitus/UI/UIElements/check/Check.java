/*
 * Copyright (c) 2020.
 * Copyright by Tim and Felix
 */

package de.ft.interitus.UI.UIElements.check;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import de.ft.interitus.Var;

public class Check {
    private final Vector2 mousesave = new Vector2();
    private boolean touched;

    public boolean isjustPressed(int x, int y, int w, int h) {
        boolean pressed = false;

        if (Gdx.input.isButtonPressed(0)) {
            if (Gdx.input.getX() > x && Gdx.input.getX() < x + w && Gdx.input.getY() > Gdx.graphics.getHeight() - y - h && Gdx.input.getY() < Gdx.graphics.getHeight() - y) {
                if (touched == false) {
                    mousesave.set(Gdx.input.getX(), Gdx.input.getY());
                    touched = true;
                }
            }

        }
        if (!Gdx.input.isButtonPressed(0) && Math.abs(Gdx.input.getX() - mousesave.x) < 1 && Math.abs(Gdx.input.getY() - mousesave.y) < 1 && touched) {

            pressed = true;
            touched = false;
        } else {
            pressed = false;
        }
        if (Math.abs(Gdx.input.getX() - mousesave.x) > 1 && Math.abs(Gdx.input.getY() - mousesave.y) > 1 && touched && !Gdx.input.isButtonPressed(0)) {
            pressed = false;
            touched = false;
        }


        return pressed;

    }

    public boolean isJustPressedNormal(int x, int y, int w, int h) {
        return Gdx.input.getX() > x && Gdx.input.getX() < x + w && Gdx.input.getY() > Gdx.graphics.getHeight() - y - h && Gdx.input.getY() < Gdx.graphics.getHeight() - y && Gdx.input.isButtonJustPressed(0);

    }

    public boolean isMouseover(int x, int y, int w, int h) {
        return Gdx.input.getX() > x && Gdx.input.getX() < x + w && Gdx.input.getY() > Gdx.graphics.getHeight() - y - h && Gdx.input.getY() < Gdx.graphics.getHeight() - y;
    }


    public boolean isPressed(int x, int y, int w, int h) {
        return Gdx.input.getX() > x && Gdx.input.getX() < x + w && Gdx.input.getY() > Gdx.graphics.getHeight() - y - h && Gdx.input.getY() < Gdx.graphics.getHeight() - y && Gdx.input.isButtonPressed(0);
    }

    public boolean wasMousePressed(int x, int y, int w, int h) {
        return Var.mousepressedoldwihoutunproject.x > x && Var.mousepressedoldwihoutunproject.x < x + w && Var.mousepressedoldwihoutunproject.y > Gdx.graphics.getHeight() - y - h && Var.mousepressedoldwihoutunproject.y < Gdx.graphics.getHeight() - y && Gdx.input.isButtonPressed(0);
    }


}
