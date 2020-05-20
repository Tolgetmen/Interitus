package de.ft.interitus.UI.input.shortcut;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import de.ft.interitus.Programm;
import de.ft.interitus.ProgrammingSpace;
import de.ft.interitus.UI.input.check.InputManager;

import java.util.ArrayList;

public class ShortCut {
    boolean disable=false;
    ArrayList<Integer>kombination=new ArrayList<>();
    public ShortCut(int... kombination) {
        for(int i=0;i<kombination.length;i++){
             this.kombination.add(kombination[i]);
        }



    }


    public void setShortCut(int... kombination) {
        for(int i=0;i<kombination.length;i++){
            this.kombination.add(kombination[i]);
        }
    }


    public boolean isPressed(){
        boolean pressed=true;
if(!disable) {
   /* for (int i = 0; i < kombination.size(); i++) {

        if(kombination.get(i)<600) {
            if (!Gdx.input.isKeyPressed(kombination.get(i))) {
                pressed = false;
            }
        }else{
            switch (kombination.get(i)){
                case 600:
                    if (!Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && !Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT)) {
                        pressed = false;
                    }
                    break;
                case 601:
                    if (!Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) && !Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
                        pressed = false;
                    }
                    break;
            }
        }
    }*/
for(int i=0;i<kombination.size();i++) {
    if (ProgrammingSpace.pressedKeys.getPressedkeys().indexOf(kombination.get(i)) == -1) {
        System.out.println("blublubblubblubblab");
        pressed = false;
    }
}
if(kombination.size()!=ProgrammingSpace.pressedKeys.getPressedkeys().size()){
    pressed=false;
}

}else{
    pressed=false;
}

        return pressed;
    }

}