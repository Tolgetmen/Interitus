/*
 * Copyright (c) 2020.
 * Copyright by Tim and Felix
 */

package de.ft.interitus.Block;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import de.ft.interitus.ProgrammingSpace;
import de.ft.interitus.UI.UIVar;
import de.ft.interitus.loading.AssetLoader;
import de.ft.interitus.projecttypes.ProjectManager;
import de.ft.interitus.utils.CheckKollision;

public abstract class WireNode implements VisibleObjects {
    public final static int public_w = 10;
    public final static int public_h = 10;
    private final Vector2 gemerktvector = new Vector2(0, 0);
    private final Vector3 tempvector = new Vector3();
    private final Vector3 tempvector1 = new Vector3();

    private Wire wire_left;
    private Wire wire_right;
    private int x;
    private int y;
    private int w;
    private int h;
    private boolean gemerkt = false;
    private Frustum frustum;

    public WireNode(Wire wiresleft, int x, int y, int w, int h) { //Wenn eine neue Node gesetzt wird

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        this.wire_left = wiresleft;

    }

    public WireNode(Wire wire_left, Wire wire_right) {

    }

    public void draw() {


        if (CheckKollision.checkmousewithobject(x, y, w, h, (int) ProgrammingSpace.viewport.unproject(tempvector.set(Gdx.input.getX(), Gdx.input.getY(), 0)).x, (int) ProgrammingSpace.viewport.unproject(tempvector1.set(Gdx.input.getX(), Gdx.input.getY(), 0)).y) && Gdx.input.isButtonJustPressed(0)) {

            if (!gemerkt) {
                gemerktvector.set(Gdx.input.getX() - x, Gdx.graphics.getHeight() - Gdx.input.getY() - y);

                gemerkt = true;
            }


        }

        if (!Gdx.input.isButtonPressed(0) || UIVar.isdialogeopend) {
            gemerkt = false;
        }

        if (gemerkt) {
            x = (int) (Gdx.input.getX() - gemerktvector.x);
            y = (int) (Gdx.graphics.getHeight() - Gdx.input.getY() - gemerktvector.y);
        }


        boolean temp = false;
        if (!ProgrammingSpace.batch.isDrawing()) {
            ProgrammingSpace.batch.begin();
            temp = true;
        }


        ProgrammingSpace.batch.draw(AssetLoader.wire_node, x, y, w, h);


        if (temp) {
            ProgrammingSpace.batch.end();
        }

        if (wire_left == null || wire_right == null) {
            ProjectManager.getActProjectVar().wireNodes.remove(this);
            ProjectManager.getActProjectVar().visibleWireNodes.remove(this);

            if (wire_right != null) {
                wire_right.setLeft_connection(null);
            }
            if (wire_left != null) {
                wire_left.setRight_connection(null);


            }

        }

    }

    public Wire getWire_right() {
        return wire_right;
    }

    @Override
    public void setWire_right(Wire wire_right) {
        if (wire_right == null) {
            this.wire_right.setLeft_connection(null);
        }

        this.wire_right = wire_right;
    }

    public Wire getWire_left() {
        return wire_left;
    }

    @Override
    public void setWire_left(Wire wire_left) {

        if (wire_left == null) {
            this.wire_left.setRight_connection(null);
        }

        this.wire_left = wire_left;


    }


    @Override
    public boolean isVisible() {
        frustum = ProgrammingSpace.cam.frustum;
        return frustum.boundsInFrustum(x, y, 0, w, h, 0);

    }

    @Override
    public boolean amiablock() {
        return false;
    }

    @Override
    public boolean amiwirenode() {
        return true;
    }

    @Override
    public Block getblock() {
        return null;
    }

    @Override
    public WireNode getwirenode() {
        return this;
    }

    @Override
    public float getX_entrance() {
        return x;
    }

    @Override
    public float getY_entrance() {
        return y;
    }

    @Override
    public float getW_entrance() {
        return w;
    }

    @Override
    public float getH_entrance() {
        return h;
    }

    @Override
    public float getX_exit() {
        return x;
    }

    @Override
    public float getY_exit() {
        return y;
    }

    @Override
    public float getW_exit() {
        return w;
    }

    @Override
    public float getH_exit() {
        return h;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }
}
