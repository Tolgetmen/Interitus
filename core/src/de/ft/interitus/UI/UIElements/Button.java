package de.ft.interitus.UI.UIElements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.ft.interitus.UI.UI;
import de.ft.interitus.UI.UIElements.check.Check;
import de.ft.interitus.Var;
import de.ft.interitus.utils.RoundRectangle;

public class Button {
    BitmapFont font = new BitmapFont();
    private int x;
    private int y;
    private int w;
    private int h;
    private String text;
    private Texture image = null;
    private Texture image_mouseover = null;
    private Texture image_pressed = null;
    private boolean visible = true;
    private boolean disable = false;
    //private final SpriteBatch batch = new SpriteBatch();
    private final ShapeRenderer s = new ShapeRenderer();
    private final GlyphLayout glyphLayout = new GlyphLayout();
    private final Check check = new Check();
    private boolean flipX=false;
    private boolean flipY=false;
    public float hovertransparancy = 0.8f;
    public static boolean disablepresscolorchange = false;


    public Button(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

    }

    public Button() {


    }



    public void setBounds(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

    }


    public boolean isjustPressed() {
        boolean pressed = false;
        if (!disable&&! Var.isdialogeopend) {

            pressed = check.isjustPressed(x, y, w, h);

        } else {
            return false;
        }

        return pressed;

    }

    public boolean isjustPressednormal(){
        boolean pressed = false;
        if (!disable&&!Var.isdialogeopend) {

            pressed = check.isJustPressedNormal(x, y, w, h);

        } else {
            return false;
        }

        return pressed;
    }


    public boolean isPresseded() {
        if (!disable) {

            return check.isPressed(x, y, w, h);
        } else {
            return false;
        }
    }


    public boolean isMouseover() {

        if(Var.isdialogeopend) {
            return false;
        }else {
            if (!disable) {

                return check.isMouseover(x, y, w, h);
            } else {
                return false;
            }
        }
    }

    public void draw() {
        if (isVisible()) {
            if (image == null) {

                s.begin(ShapeRenderer.ShapeType.Filled);
                RoundRectangle.abgerundetesRechteck(s, this.x, this.y, this.w, this.h, 5);
                s.end();
            } else if(image_mouseover == null) {
                UI.UIbatch.begin();
                if (isMouseover()) {
                    UI.UIbatch.setColor(1, 1, 1, hovertransparancy);
                } else {
                    UI.UIbatch.setColor(1, 1, 1, 1);
                }
                if (isMouseover() && Gdx.input.isButtonPressed(0)&&!disablepresscolorchange) {
                    UI.UIbatch.setColor(1, 0.5f, 0.5f, 1);
                }
                if (isDisable()) {
                    UI.UIbatch.setColor(1, 1, 1, 0.2f);
                }
                //batch.draw(image, this.x, this.y, this.w, this.h);
                UI.UIbatch.draw(image,this.x,this.y,this.w,this.h,0,0,image.getWidth(),image.getHeight(),this.flipX,this.flipY);
                UI.UIbatch.end();
            }else if(image_pressed==null){
                UI.UIbatch.begin();
                if(isMouseover()){
                    UI.UIbatch.draw(image_mouseover,this.x,this.y,this.w,this.h,0,0,image.getWidth(),image.getHeight(),this.flipX,this.flipY);
                }else{
                    UI.UIbatch.draw(image,this.x,this.y,this.w,this.h,0,0,image.getWidth(),image.getHeight(),this.flipX,this.flipY);
                }
                UI.UIbatch.end();
            }else{
                UI.UIbatch.begin();
                if(isMouseover()){
                    UI.UIbatch.draw(image_mouseover,this.x,this.y,this.w,this.h,0,0,image.getWidth(),image.getHeight(),this.flipX,this.flipY);
                }else{
                    UI.UIbatch.draw(image,this.x,this.y,this.w,this.h,0,0,image.getWidth(),image.getHeight(),this.flipX,this.flipY);
                }
                if (isMouseover() && Gdx.input.isButtonPressed(0)) {
                    UI.UIbatch.draw(image_pressed,this.x,this.y,this.w,this.h,0,0,image.getWidth(),image.getHeight(),this.flipX,this.flipY);
                }
                UI.UIbatch.end();
            }
            if (text != null) {
                UI.UIbatch.begin();
                UI.UIbatch.setColor(1, 1, 1, 1);
                glyphLayout.setText(font, this.text);
                font.draw(UI.UIbatch, glyphLayout, x + 5, y + glyphLayout.height + h / 2 - glyphLayout.height / 2);

                UI.UIbatch.end();
            }
        }
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Texture getImage() {
        return image;
    }

    public void setImage(Texture image) {
        this.image = image;
    }

    public void setImage_mouseover(Texture image_mouseover) {
        this.image_mouseover = image_mouseover;
    }

    public void setImage_pressed(Texture image_pressed) {
        this.image_pressed = image_pressed;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    public void setFlipX(boolean flipX) {
        this.flipX = flipX;
    }

    public void setFlipY(boolean flipY) {
        this.flipY = flipY;
    }

    public boolean isFlipX() {
        return flipX;
    }

    public boolean isFlipY() {
        return flipY;
    }



}

