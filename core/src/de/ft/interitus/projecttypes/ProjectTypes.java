/*
 * Copyright (c) 2020.
 * Copyright by Tim and Felix
 */

package de.ft.interitus.projecttypes;

import de.ft.interitus.Block.Generators.*;
import de.ft.interitus.DisplayErrors;
import de.ft.interitus.compiler.Compiler;
import de.ft.interitus.plugin.Plugin;
import de.ft.interitus.projecttypes.BlockTypes.PlatformSpecificBlock;
import de.ft.interitus.utils.ArrayList;

public class ProjectTypes {

    private final de.ft.interitus.plugin.Plugin pluginRegister;
    private final BlockVarGenerator blockVarGenerator;
    private final ProjectFunktions projectFunktions;
    private final Compiler compiler;
    String name;
    ArrayList<PlatformSpecificBlock> projectblocks;
    BlockUpdateGenerator blockUpdateGenerator = null;
    BlockGenerator blockGenerator = null;
    private WireGenerator wireGenerator;
    private WireNodeGenerator wireNodeGenerator;
    private BlocktoSaveGenerator blocktoSaveGenerator;

    public ProjectTypes(Plugin pluginRegister, String name, ArrayList<PlatformSpecificBlock> blocks, BlockGenerator blockgenerator, BlockUpdateGenerator updategenerator, WireGenerator wireGenerator, WireNodeGenerator wireNodeGenerator, BlocktoSaveGenerator blocktoSaveGenerator, BlockVarGenerator blockVarGenerator, ProjectFunktions projectFunktions, Compiler compiler) {
        this.projectblocks = blocks;

        this.name = name;
        this.blockGenerator = blockgenerator;
        this.blockUpdateGenerator = updategenerator;
        this.wireGenerator = wireGenerator;
        this.wireNodeGenerator = wireNodeGenerator;
        this.blocktoSaveGenerator = blocktoSaveGenerator;
        this.pluginRegister = pluginRegister;
        this.blockVarGenerator = blockVarGenerator;
        this.projectFunktions = projectFunktions;
        this.compiler = compiler;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<PlatformSpecificBlock> getProjectblocks() {
        return projectblocks;
    }

    public void setProjectblocks(ArrayList<PlatformSpecificBlock> projectblocks) {
        this.projectblocks = projectblocks;
    }

    public BlockGenerator getBlockGenerator() {
        return blockGenerator;
    }

    public void setBlockGenerator(BlockGenerator blockGenerator) {
        this.blockGenerator = blockGenerator;
    }

    public BlockUpdateGenerator getBlockUpdateGenerator() {
        return blockUpdateGenerator;
    }

    public void setBlockUpdateGenerator(BlockUpdateGenerator blockUpdateGenerator) {
        this.blockUpdateGenerator = blockUpdateGenerator;
    }

    public WireGenerator getWireGenerator() {
        return wireGenerator;
    }

    public void setWireGenerator(WireGenerator wireGenerator) {
        this.wireGenerator = wireGenerator;
    }

    public WireNodeGenerator getWireNodeGenerator() {
        return wireNodeGenerator;
    }

    public void setWireNodeGenerator(WireNodeGenerator wireNodeGenerator) {
        this.wireNodeGenerator = wireNodeGenerator;
    }

    public BlocktoSaveGenerator getBlocktoSaveGenerator() {
        return blocktoSaveGenerator;
    }

    public void setBlocktoSaveGenerator(BlocktoSaveGenerator blocktoSaveGenerator) {
        this.blocktoSaveGenerator = blocktoSaveGenerator;
    }

    public Plugin getPluginRegister() {
        return pluginRegister;
    }


    public ProjectVar init() {
        ProjectVar blockvar = this.blockVarGenerator.generate();
        blockvar.projectType = this;
        return blockvar;

    }


    public void initProject() {
        projectFunktions.create();
    }

    public void update() {


        try {

            projectFunktions.update();

        } catch (Throwable e) {

            DisplayErrors.customErrorstring = "Fehler im ProjectTyp";
            DisplayErrors.error = e;

            e.printStackTrace();
        }


    }

    public ProjectFunktions getProjectFunktions() {
        return projectFunktions;
    }

    public Compiler getCompiler() {
        return compiler;
    }
}
