package de.ft.interitus.UI.input.bar.tappedbar;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class TappedBar {
    int x=0;
    int y=0;
    int weight=500;
    int height=100;
    int itemabstand=30;
    SpriteBatch batch = new SpriteBatch();
    ShapeRenderer renderer = new ShapeRenderer();
    BitmapFont font = new BitmapFont();
    ArrayList<TapContent>taps=new ArrayList<>();
    TapContent selectetContent=null;
    public TappedBar(int x,int y){
        this.x=x;
        this.y=y;

    }
    public TappedBar(int x,int y,int w,int h){
        this.x=x;
        this.y=y;
        this.weight=w;
        this.height=h;
    }

    public void setContent(TapContent... tc){
        taps.clear();
        for(int i=0;i<tc.length;i++){
            taps.add(tc[i]);
        }
        selectetContent=tc[0];
    }

    private void drawBar(){
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.rect(x,y,weight,height);
        renderer.line(x+weight/2,y,x+weight/2,y+height);
        renderer.end();
            for(int i=0;i<taps.size();i++){
                TapContent tb=taps.get(i);
                //Vector2 tabbuttonsize=new Vector2(64,15);
                tb.getTab_button().setW(64);
                tb.getTab_button().setH(15);

                if(tb.getTab_button().isjustPressednormal()){
                    selectetContent=tb;
                }


                if(selectetContent==taps.get(i)){
                    tb.getTab_button().setBounds(x+(((taps.size()*tb.getTab_button().getW())/taps.size()*i)+weight/2-taps.size()*tb.getTab_button().getW()/2),y+height+10,tb.getTab_button().getW(),tb.getTab_button().getH());

                }else{
                    tb.getTab_button().setBounds(x+(((taps.size()*tb.getTab_button().getW())/taps.size()*i)+weight/2-taps.size()*tb.getTab_button().getW()/2),y+height,tb.getTab_button().getW(),tb.getTab_button().getH());

                }
                tb.getTab_button().draw();
            }
    }

    private void drawContent(){
        if(selectetContent!=null) {
            int gedachtweight=0;
            for (int i = 0; i < selectetContent.items.size(); i++) {
                gedachtweight+=selectetContent.items.get(i).getW();
            }
            for (int i = 0; i < selectetContent.items.size(); i++) {
                selectetContent.items.get(i).setY(height/2-selectetContent.items.get(i).getH()/2+y);
                selectetContent.items.get(i).setX(x+(weight/2-(     gedachtweight    )/2)+(i*    selectetContent.items.get(i).getW()     )-((selectetContent.items.size()/2-i)*itemabstand));
                selectetContent.items.get(i).draw();
                System.out.println(gedachtweight);

            }
        }
    }

    public void draw(){
        drawBar();
        drawContent();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setItemabstand(int itemabstand) {
        this.itemabstand = itemabstand;
    }

    public int getItemabstand() {
        return itemabstand;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }
}
