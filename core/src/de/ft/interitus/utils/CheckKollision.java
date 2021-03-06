/*
 * Copyright (c) 2020.
 * Copyright by Tim and Felix
 */

package de.ft.interitus.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.*;
import de.ft.interitus.Block.Block;
import de.ft.interitus.ProgrammingSpace;

public class CheckKollision {

    private static final int mousesize = 1;
    static Vector3 temp3 = new Vector3();
    static Vector3 temp4 = new Vector3();
    static Rectangle rec1 = new Rectangle();
    static Rectangle rec2 = new Rectangle();
    Vector2 temp1 = new Vector2();
    Vector2 temp2 = new Vector2();

    public static boolean object(float obj1_x, float obj1_y, float obj1_w, float obj1_h, float obj2_x, float obj2_y, float obj2_w, float obj2_h) {


        rec1.set(obj1_x, obj1_y, obj1_w, obj1_h);
        rec2.set(obj2_x, obj2_y, obj2_h, obj2_w);

        return rec1.overlaps(rec2);

    }

    public static boolean objectwithrotation(float obj1_x, float obj1_y, float obj1_w, float obj1_h, float obj1_angle, float obj2_x, float obj2_y, int obj2_h, int obj2_w, float obj2_angle) {


        Polygon obj1 = new Polygon(new float[]{0, 0, obj1_w, 0, obj1_w, obj1_h, 0, obj1_h});
        Polygon obj2 = new Polygon(new float[]{0, 0, obj2_w, 0, obj2_w, obj2_h, 0, obj2_h});
        obj1.setOrigin(0, 0);
        obj2.setOrigin(0, 0);
        obj1.setPosition(obj1_x, obj1_y);
        obj2.setPosition(obj2_x, obj2_y);
        obj1.setRotation(obj1_angle);
        obj2.setRotation(obj2_angle);

        return Intersector.overlapConvexPolygons(obj1, obj2);

    }

    public static boolean checkmousewithblock(Block block) {


        return object(block.getX(), block.getY(), block.getW(), block.getH(), (int) ProgrammingSpace.viewport.unproject(temp3.set(Gdx.input.getX(), Gdx.input.getY(), 0)).x, (int) ProgrammingSpace.viewport.unproject(temp4.set(Gdx.input.getX(), Gdx.input.getY(), 0)).y, mousesize, mousesize);

    }


    public static boolean checkmousewithobject(int obj1_x, int obj1_y, int obj1_w, int obj1_h, Vector2 mousepos) {


        return object(obj1_x, obj1_y, obj1_w, obj1_h, (int) mousepos.x, (int) mousepos.y, mousesize, mousesize);

    }

    public static boolean checkmousewithobject(int obj1_x, int obj1_y, int obj1_w, int obj1_h, float x, float y) {


        return object(obj1_x, obj1_y, obj1_w, obj1_h, x, y, mousesize, mousesize);

    }


    public static boolean checkmousewithblock(Block block, Vector2 mousepos) {


        return object(block.getX(), block.getY(), block.getW(), block.getH(), (int) mousepos.x, (int) mousepos.y, mousesize, mousesize);

    }

    public static boolean checkblockwithduplicate(Block normal, Block duplicate, int rl) {
        if (rl == 0) { //0=Right
            return object(normal.getX(), normal.getY(), normal.getW(), normal.getH(), duplicate.getX_dup_rechts(), duplicate.getY(), normal.getW() / 1.5f, duplicate.getH()); //TODO Unexpected Kollision Reaction
        } else {
            return object(normal.getX(), normal.getY(), normal.getW(), normal.getH(), duplicate.getX() - normal.getW() / 1.5f, duplicate.getY(), normal.getW() / 1.5f, duplicate.getH());
        }
    }


    public static int flache(int x, int y, int w, int h, int xm, int ym) {
        int flache = 0;

        int xx = w - (xm - x);
        int yy = (ym + h - y);
        if (xx > w) {
            xx = w - (xx - w);
        }
        if (yy > h) {
            yy = h - (yy - h);
        }

        //System.out.println("y: " + yy + "x: " + xx + "flache: "+ yy*xx);
        flache = xx * yy;
        return flache;
    }


}

