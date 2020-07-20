/*
 * Copyright (c) 2020.
 * Author Tim & Felix
 */

package de.ft.interitus.projecttypes.device.BlockTypes.Arduino.Arduino.actionblocks;

import com.badlogic.gdx.graphics.Texture;
import de.ft.interitus.Block.Parameter;
import de.ft.interitus.loading.AssetLoader;
import de.ft.interitus.projecttypes.ProjectTypes;
import de.ft.interitus.projecttypes.device.BlockTypes.Arduino.Arduino.ArduinoBlock;
import de.ft.interitus.projecttypes.device.BlockTypes.BlockCategories;
import de.ft.interitus.projecttypes.device.BlockTypes.BlockTopParameter;
import de.ft.interitus.projecttypes.device.BlockTypes.PlatformSpecificBlock;
import de.ft.interitus.projecttypes.device.BlockTypes.ProjectTypesVar;

import java.awt.*;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;

public class SetPinMode implements PlatformSpecificBlock, ArduinoBlock {

    ArrayList<Parameter> parameters = new ArrayList<>();
    Parameter pin;
    Parameter mode;



    private final ProjectTypes type;

    public SetPinMode(ProjectTypes type) {


        pin = new Parameter(0, AssetLoader.img_WaitBlock_warteZeit_Parameter,"Pin","",null);
        mode = new Parameter(1,AssetLoader.img_WaitBlock_warteZeit_Parameter,"Mode","",null);


        parameters.add(pin);
        parameters.add(mode);
        this.type = type;



    }
    @Override
    public String getCode() {
        return "pinMode("+this.parameters.get(0).getParameter()+","+this.parameters.get(1).getParameter()+");";
    }

    @Override
    public ArrayList<Parameter> getBlockParameter() {
        return parameters;
    }

    @Override
    public String getName() {
        return "SetPinMode";
    }

    @Override
    public String getdescription() {
        return "";
    }

    @Override
    public ArrayList<BlockTopParameter> getblocktopparamter() {
        return null;
    }

    @Override
    public Color blockcolor() {
        return null;
    }

    @Override
    public BlockCategories getBlockCategoration() {
        return BlockCategories.ActionBlocks;
    }

    @Override
    public Texture getSmallImage() {
        return AssetLoader.img_mappe1;
    }

    @Override
    public Texture getImageRight() {
        return AssetLoader.WaitBlock_right;
    }

    @Override
    public Texture getImageLeft() {
        return AssetLoader.WaitBlock_left;
    }

    @Override
    public Texture getImageCenter() {
        return AssetLoader.WaitBlock_middle;
    }


    @Override
    public ProjectTypes getProjectType() {
        return null;
    }

    @Override
    public int getWidth() {
        return 150;
    }

    @Override
    public int getID() {
        for(int i = 0; i< ProjectTypesVar.projectTypes.get(ProjectTypesVar.projectTypes.indexOf(type)).getProjectblocks().size(); i++) {

            if(ProjectTypesVar.projectTypes.get(ProjectTypesVar.projectTypes.indexOf(type)).getProjectblocks().get(i).getClass()==this.getClass()) {

                return i;
            }

        }

        return -1;    }

    @Override
    public boolean canbedeleted() {
        return true;
    }

    @Override
    public boolean canhasrightconnector() {
        return true;
    }

    @Override
    public boolean canhasleftconnect() {
        return true;
    }
}
