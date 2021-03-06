/*
 * Copyright (c) 2020.
 * Copyright by Tim and Felix
 */

package de.ft.interitus.Block;


import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import de.ft.interitus.Block.Generators.BlockUpdateGenerator;
import de.ft.interitus.Block.Generators.BlocktoSaveGenerator;
import de.ft.interitus.DisplayErrors;
import de.ft.interitus.ProgrammingSpace;
import de.ft.interitus.RechtsKlick;
import de.ft.interitus.ThreadManager;
import de.ft.interitus.events.EventVar;
import de.ft.interitus.events.block.BlockCreateEvent;
import de.ft.interitus.events.block.BlockDeleteEvent;
import de.ft.interitus.events.block.BlockNeighborSetEvent;
import de.ft.interitus.events.rightclick.RightClickButtonSelectEvent;
import de.ft.interitus.events.rightclick.RightClickCloseEvent;
import de.ft.interitus.events.rightclick.RightClickEventListener;
import de.ft.interitus.events.rightclick.RightClickOpenEvent;
import de.ft.interitus.loading.AssetLoader;
import de.ft.interitus.projecttypes.BlockTypes.PlatformSpecificBlock;
import de.ft.interitus.projecttypes.ProjectManager;
import de.ft.interitus.utils.CheckKollision;
import de.ft.interitus.utils.ShapeRenderer;

import java.util.Objects;

/***
 *
 * @author Felix & Tim
 *
 *Hier werden die Blöcke erstellt die für die Programmmierung zuständig sind.
 * Nähere Informationen stehen in den Kommentaren oder im Github Wiki.
 *
 */

public abstract class Block implements VisibleObjects {
    private final Vector2 wireconnector_right = new Vector2(0, 0); //Die rechte wire-Anschluss Position
    private final Vector2 wireconnector_left = new Vector2(0, 0); //Die linke wire-Anschluss Position
    private final RightClickEventListener rightClickEventListener;
    private final BlockUpdateGenerator blockUpdateGenerator;
    private final BlocktoSaveGenerator blocktoSaveGenerator;
    private final GlyphLayout glyphLayout = new GlyphLayout();
    public boolean seted = true; //Ob der Block losgelassen wurde bzw ob der Block eine statische Position hat
    public boolean moved = false; // Ob der Block gerade mit der Maus bewegt wird
    private boolean marked = false; //Ob der Block gerade makiert ist
    private int x; //Die x Koordinate des Blocks
    private int y; //Die Y Koordinate des Blocks
    private int w; //Die Weite des Blocks
    private int h; //Die Höhe des Blocks
    private int index; //Der Index des Blocks (Der Gleiche wie im Array BlckVar.blocks)o
    private boolean showdupulicate_rechts; //Ob das Duplicat rechts angezeigt werden soll d.h. ob der Block der gerade bewegt wird hier hin springen wird
    private boolean showdupulicate_links; //Ob das Duplicat links ...
    private int x_dup_rechts; // Die X Position des Duplicates
    //Die Y Position des Duplicates  //Die Weite und Höhe ergeben sich aus der Block weite und Höhe
    private boolean moving = false; //Ob der Block gerade durch den Nutzer bewegt wird
    private BlockUpdate blockupdate; // Die Block update methode hier werden user actionen engegengenommen und verarbeitet
    private Block left = null; //Der rechte verbundene Block hier auf Null gesetzt, da zum erstell zeitpunkt noch kein Nachbar exsistiert
    private Block right = null; //Der linke verbundene Block hier auf Null gesetzt, da zum erstell zeitpunkt noch kein Nachbar exsistiert
    private Wire wire_left = null; //linke verbundene Wire
    private Wire wire_right = null; //rechte verbunde Wire
    private PlatformSpecificBlock blocktype;


    public Block(final int index, int x, int y, int w, int h, PlatformSpecificBlock platformSpecificBlock, BlockUpdateGenerator update, BlocktoSaveGenerator blocktoSaveGenerator) { //Initzialisieren des Blocks
        this.blocktype = platformSpecificBlock;
        EventVar.blockEventManager.createBlock(new BlockCreateEvent(this, this));
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.x_dup_rechts = this.x + this.w; //Duplicats positionen werden berechnet

        wireconnector_right.set(x + w, y + h / 3f);
        this.index = index;
        this.blockUpdateGenerator = update;
        this.blockupdate = update.generate(this); //BlockUpdate Klasse wird initzilisieren
        this.blocktoSaveGenerator = blocktoSaveGenerator;

        if (this.isVisible()) { //Wenn der Block sichtbar ist...  //Das passiert deshalb weil nicht für nicht sichbare Blöcke ein Thread laufen muss
            blockupdate.start(); //...wird der updater gestartet
            Objects.requireNonNull(ProjectManager.getActProjectVar()).visibleblocks.add(this); //und wird zum Array der sichtbaren Blöcke hinzugefüt
        } else {
            blockupdate.isrunning = false; //...wenn nicht dann nicht
        }
        ThreadManager.add(blockupdate, this); //Blockupdate wird zum Array der laufenden Threads hinzugefügt

        final Block instance = this;

        rightClickEventListener = new RightClickEventListener() {
            @Override
            public void openrightclickwindow(RightClickOpenEvent e) {

            }

            @Override
            public void closerightclickwindow(RightClickCloseEvent e) {

            }

            @Override
            public void buttonclickedinwindow(RightClickButtonSelectEvent e) {

                if (e.getButton().getText().contains("Löschen") && blockupdate.toggle) {
                    if (RechtsKlick.mouseoverblockindex == Objects.requireNonNull(ProjectManager.getActProjectVar()).blocks.indexOf(instance) && getBlocktype().canbedeleted())
                        instance.delete(false);
                }
            }
        };
        EventVar.rightClickEventManager.addListener(rightClickEventListener);

    }


    /***
     *
     * @return if the Block is visible
     */

    @Override
    public boolean isVisible() {
        return ProgrammingSpace.cam.frustum.boundsInFrustum(this.getX(), this.getY(), 0, this.getW(), this.getH(), 0); //Ist der Block im Camera bereich?


    }

    /***
     *
     * @return is the
     *  @see VisibleObjects child a block
     */

    @Override
    public boolean amiablock() {
        return true;
    }

    /***
     *
     * @return is the
     * @see VisibleObjects
     * child a WireNode
     */
    @Override
    public boolean amiwirenode() {
        return false;
    }

    /**
     * @return the block variable it's need to set the block left und rights if you use wires
     */
    @Override
    public Block getblock() {
        return this;
    }

    /***
     *
     * @return the Node variable it's need to set the block left und rights if you use wires
     */

    @Override
    public WireNode getwirenode() {
        return null;
    }

    /***
     *
     * @return the entrace point of the Block is visible if you create a wire
     */
    @Override
    public float getX_entrance() {
        return getWireconnector_left().x;
    }

    /***
     *
     * @return the entrace point of the Block is visible if you create a wire
     */

    @Override
    public float getY_entrance() {
        return getWireconnector_left().y;
    }

    /***
     *
     * @return the entrace point of the Block is visible if you create a wire
     */
    @Override
    public float getW_entrance() {
        return 20;
    }

    /***
     *
     * @return the entrace point of the Block is visible if you create a wire
     */
    @Override
    public float getH_entrance() {
        return 20;
    }

    /***
     *
     * @return the exit point of the Block is visible if their are no neighbours
     */
    @Override
    public float getX_exit() {
        return (int) this.getwireconnector_right().x;
    }

    /***
     *
     * @return the exit point of the Block is visible if their are no neighbours
     */
    @Override
    public float getY_exit() {
        return (int) this.getwireconnector_right().y;
    }

    /***
     *
     * @return the exit point of the Block is visible if their are no neighbours
     */
    @Override
    public float getW_exit() {
        return 0;
    }

    /***
     *
     * @return the exit point of the Block is visible if their are no neighbours
     */
    @Override
    public float getH_exit() {
        return 0;
    }

    /***
     *
     * @return the running thread of the Block
     * @see ThreadManager
     */
    public BlockUpdate getBlockupdate() {
        return blockupdate; //Gibt den Blockupdater zurück
    }

    /***
     *
     * @return the duplication is visible if a block offers the neighbours place
     */

    public int getX_dup_rechts() {
        return x_dup_rechts; //Gibt die X Position des rechten duplicates zurück
    }


    /***
     *
     * @return is the Block moved by the user
     * You can also check this state with a Listener //TODO Create Moving Listener (with start and Stop position)
     * @see de.ft.interitus.events.block.BlockEventListener
     */
    public boolean isMoving() {
        return moving; //Gibt zurück ob der Block gerade in Bewegung ist
    }

    /***
     *
     * @param moving set if the Block is moved by the user
     */
    public void setMoving(boolean moving) {
        this.moving = moving; //Gibt an das der Block gerade in Bewegung ist
    }

    /***
     *
     * @return is the right duplicate shown
     */
    public boolean isShowdupulicate_rechts() {
        return showdupulicate_rechts; //Wird das rechte duplicat angezeigt?
    }


    public void setShowdupulicate_rechts(boolean showdupulicate_rechts) {
        this.showdupulicate_rechts = showdupulicate_rechts; //Das Rechte duplicat wird angzeigt oder nicht
    }

    /***
     *
     * @return is the left duplicate shown
     */

    public boolean isShowdupulicate_links() {
        return showdupulicate_links; //Wird das linke duplicat angezeigt?
    }

    public void setShowdupulicate_links(boolean showdupulicate_links) {
        this.showdupulicate_links = showdupulicate_links; //Das linke Duplicat Status soll geändert werden
    }

    /***
     *
     * @return is the Block marked with the mouse
     */

    public boolean isMarked() {
        return marked; //Ist der Block von User makiert worden?
    }


    public void setMarked(boolean marked) {

        this.marked = marked; //Soll der Block makiert sein?
    }

    public Wire getWire_left() {
        return wire_left;
    }

    /**
     * @param wire_left set the wire connection to the Block
     *                  Attention to run the Programm you need to set the neighbours from the Block
     *                  It's not only the wire which is used to find the Blocks order
     */

    @Override
    public void setWire_left(Wire wire_left) {
        this.wire_left = wire_left;
    }


    public Wire getWire_right() {
        return wire_right;
    }

    @Override
    public void setWire_right(Wire wire_right) {
        this.wire_right = wire_right;
    }

    public Block getLeft() {
        return left; //Gibt den linken VERBUNDENEN Nachbar zurück
    }

    /***
     *
     * @param left set The block neighbour left
     *              And it set the right neighbour of the block to this
     *
     */

    public void setLeft(Block left) { //Setzt einen neuen linken nachbar


        if (left == null) {
            if (this.left != null) {
                if (this.left.getRight() == null) {
                    this.left.setRight(null);
                }
            }
        }

        if (this.left != left) { //Wenn der linke Nachbar nicht der schon der Gleiche Block ist (Sonst tritt hier ein OverFlow auf siehe set Right)
            this.left = left;      //Block wird als Nachbar aufgenommen
        }
        if (left != null) { //Wenn der Block nicht null ist...
            if (left.getRight() != this) { //Und der wenn der Rechte Nachbar vom linken Block nicht man selbst ist
                left.setRight(this); //Wird auch diese Verbindung neu gesetzt (Um Nachbarsetzten zu erleichtern (Aus der Schlussfolgerung das der Rechte Nachbar vom linken Nachbar man selbst ist) )
            }
        }


        EventVar.blockEventManager.setNeighbor(new BlockNeighborSetEvent(this), this, left, false);

    }

    /***
     *
     * See Block.setLeft
     */

    public Block getRight() {
        return right; //Rück gabe des rechts VERBUNDENEN Nachbars
    }

    public void setRight(Block right) {

        if (right == null) {
            if (this.right != null) {
                if (this.right.getLeft() == null) {
                    this.right.setLeft(null);
                }
            }
        }

        if (this.right != right) { //Wenn der rechte Nachbar nicht der schon der Gleiche Block ist (Sonst tritt hier ein OverFlow auf siehe set Left)
            this.right = right;//Block wird als Nachbar aufgenommen
        }
        if (right != null) {//Wenn der Block nicht null ist..
            if (right.getLeft() != this) {//Und der wenn der linke Nachbar vom rechten Block nicht man selbst ist
                right.setLeft(this);//Wird auch diese Verbindung neu gesetzt (Um Nachbarsetzten zu erleichtern (Aus der Schlussfolgerung das der linke Nachbar vom rechten Nachbar man selbst ist))
            }
        }

        EventVar.blockEventManager.setNeighbor(new BlockNeighborSetEvent(this), this, right, true);

    }

    /**
     * @return Set and get the Block Positions
     */
    public int getX() {
        return x; //Rückgabe der X Position des eigenen Blockes
    }

    public void setX(int x) {
        this.x = x; //Die X Position wird geupdated
        this.x_dup_rechts = this.x + this.w; //Im gleichen Zug werden auch die beiden Duplikate auf ihre neue Poisition gesetzt

    }

    public int getY() {
        return y; //Rückgabe der Y Position des eigenen Blockes
    }

    public void setY(int y) {
        this.y = y; //Setzen der Y Position

        //Hinweis die Y Position der Duplikate muss nicht gesetzt werden da die die geleichen wie der Block selbst haben

    }

    public int getH() {
        return h; //Die höhe wird ausgegeben
    }

    public int getW() {
        return w; //Die Weite wird ausgegeben
    }

    public void setPosition(int x, int y) { //X und Y werden aufeinmal gesetzt
        this.x = x;
        this.y = y;
        this.x_dup_rechts = this.x + this.w;

    }


    public void setWH(int w, int h) { //Höhe und Weite werden auf einmal gesetzt
        this.w = w;
        this.h = h;
    }

    /**
     * Getter and Setter for the Index of the Block
     * Must be the same as in the ProjectManager.getactProjectVar().blocks array
     *
     * @return the index from this Block
     */
    public int getIndex() { //Der Index wird ausgegeben
        return index;
    }

    public void setIndex(int index) { //Der Index wird gesetzt WARNUNG nur aufrufen in Kombination mit einer ProjectManager.getactProjectVar().blocks Array aktion
        this.index = index;
    }

    /**
     * Deletes the Block a change the Index of the other Blocks in the Array depending on complete
     *
     * @param complete means that the whole Programm is clearing for example if you open a new Project
     */

    public void delete(boolean complete) { //Der Block soll gelöscht werden (complete beduetet das alle Blöcke gelöscht werden sollen)


        if (ProjectManager.getActProjectVar().Blockwitherrors.contains(this.getIndex())) {
            ProjectManager.getActProjectVar().Blockwitherrors.remove((Object) this.getIndex());
        }

        EventVar.rightClickEventManager.removeListener(this.rightClickEventListener);
        EventVar.blockEventManager.deleteBlock(new BlockDeleteEvent(this, this)); //Fire Delete Event
        ProjectManager.getActProjectVar().markedblock = null; //Der Makierte Block wird auf null gesetzt da nur ein makierter block gelöscht werden kann //Anmerkung falls das ganze Programm gelöscht wird spielt das sowieso keine Rolle
        ProjectManager.getActProjectVar().marked = false; //Ob ein Block makiert ist wird auf false gesetzt da nur ein makierter Block gelöscht werden kann
        ProjectManager.getActProjectVar().ismoving = false; // Ob ein Block bewegt wird, wird auf false gesetzt da wenn ein Block bewegt und gelöscht wird kann es nur der bewegte Block sein


        if (this.getWire_left() != null) {
            this.getWire_left().setRight_connection(null);
        }

        if (this.getWire_right() != null) {
            this.getWire_right().setLeft_connection(null);
        }

        this.setIndex(-1); //Der Index wird auf -1 gesetzt dann merkt der BlockUpdater das der laufenden Timer beendet werden soll
        if (left != null) { //Wenn ein linker Nachbar exsistiert
            left.setRight(null); //wird dem linken Nachbar gesagt das er keinen Rechten Nachbar mehr hat

            try {
                left.setWire_right(null); //Die Wire des nachbar block wird gelöscht damit die Wire kein zweites mal gelöscht wird (passiert nur bei ganz vielen wire nodes)
            } catch (NullPointerException e) {
                //Falls es gar keine Wire gab
            }
        }

        if (right != null) { // wenn ein Rechter nachbar exsitiert
            try {
                right.setLeft(null); // wird dem rechten Nachbar gesagt das er keinen linken nachbar mehr hat
            } catch (IndexOutOfBoundsException e) {


            }
            try {
                right.setWire_left(null); //Die Wire des nachbar block wird gelöscht damit die Wire kein zweites mal gelöscht wird (passiert nur bei ganz vielen wire nodes)
            } catch (NullPointerException e) {
                //Falls es gar keine Wire gab
            } catch (IndexOutOfBoundsException e) {

            }
        }


        left = null; //Die Referenzierung zum linken Nachbar wird gelöscht
        right = null; //Die Referenzierung zum rechten Nachbar wird gelöscht

        if (ProjectManager.getActProjectVar().threads.indexOf(this.blockupdate) != -1) { //Überprüfen ob Thread überhaupt läuft
            ProjectManager.getActProjectVar().threads.remove(this.blockupdate); //Wenn ja wird er aus dem Array der Threads entfernt
        }


        try {

            blockupdate.time.cancel(); //Der Blockupdate timer wird beendet
            blockupdate.interrupt(); // Der Thread wird interrupted
            blockupdate.block = null; //Die referenzierung des Threads BLocks wird getrennt
        } catch (Exception e) {


        }

        if (!complete) { //das trifft nur nicht zu wenn das ganze programm gecleart wird
            ProjectManager.getActProjectVar().blocks.remove(this); //Der block wird aus dem Blocks Array entfernt
            ProjectManager.getActProjectVar().visibleblocks.remove(this); //Der block wird aus dem Visible Blocks Array entfernt

            try {
                ProjectManager.getActProjectVar().blocks.remove(this); //Der Block entfernet sich selbst aus dem Blocks Array
            } catch (Exception e) {

            }

            Thread calcnew = new Thread() { //Da dies relativ lange dauert dauert in einem eigenen Thread
                @Override
                public void run() {
                    for (int i = 0; i < ProjectManager.getActProjectVar().visibleblocks.size(); i++) { //Durch alle Indexe des Block Arrays wird durchgegangen alle die einen größeren Index haben //Durch die Temp variable kann der alte Index des Blocks hier wieder verwendet werden
                        try {
                            if (ProjectManager.getActProjectVar().visibleblocks.get(i) != getblock()) {
                                ProjectManager.getActProjectVar().visibleblocks.get(i).findnewindex(); //Alle anderen Blöcke werden um einen Index verschoben
                            }
                        } catch (Exception e) { //Wenn man bei Milliarden von Blöcken der erste und der zweite gelöscht werden gibt es hier fehler
                            DisplayErrors.error = e;
                            e.printStackTrace();
                        }

                    }
                }
            };

            calcnew.start(); //Der Thread wird gestarted


        }

    }

    private void findnewindex() {
        this.index = ProjectManager.getActProjectVar().blocks.indexOf(this);
    }

    /**
     * @return is the Block hovered
     */

    public boolean getmousecollision() {
        return blockupdate.toggle;
    }

    /**
     * Draw the Block and the connectors
     *
     * @param batch to draw the Block
     * @param shape the connectors
     * @param font  the debug index
     */

    public void draw(SpriteBatch batch, ShapeRenderer shape, BitmapFont font) {

        if (ProjectManager.getActProjectVar().Blockwitherrors.contains(this.getIndex())) {
            batch.setColor(1, 0.8f, 0.8f, 1);
        } else {
            batch.setColor(1, 1, 1, 1);
        }


        if (!this.blockupdate.toggle) {
            batch.draw(getBlocktype().getImageCenter(), this.getX() + 6, this.getY(), this.getW() - 12, this.getH()); // Block ohne das er makiert ist
            batch.draw(getBlocktype().getImageLeft(), this.getX(), this.getY(), 6, this.getH());
            batch.draw(getBlocktype().getImageRight(), this.getX() + this.getW() - 6, this.getY(), 6, this.getH());
        } else {
            batch.draw(getBlocktype().getImageCenter(), this.getX() + 6, this.getY(), this.getW() - 12, this.getH()); // Block ohne das er makiert ist
            batch.draw(getBlocktype().getImageLeft(), this.getX(), this.getY(), 6, this.getH());
            batch.draw(getBlocktype().getImageRight(), this.getX() + this.getW() - 6, this.getY(), 6, this.getH());
            batch.draw(AssetLoader.mouse_over_mitte, this.getX() + 6, this.getY(), this.getW() - 12, this.getH()); // Block ohne das er makiert ist
            batch.draw(AssetLoader.mouseover_links, this.getX(), this.getY(), 6, this.getH());
            batch.draw(AssetLoader.mouse_over_rechts, this.getX() + this.getW() - 6, this.getY(), 6, this.getH());
        }

        if (this.isMarked()) {
            batch.draw(getBlocktype().getImageCenter(), this.getX() + 6, this.getY(), this.getW() - 12, this.getH()); // Block ohne das er makiert ist
            batch.draw(getBlocktype().getImageLeft(), this.getX(), this.getY(), 6, this.getH());
            batch.draw(getBlocktype().getImageRight(), this.getX() + this.getW() - 6, this.getY(), 6, this.getH());
            batch.draw(AssetLoader.marked_mitte, this.getX() + 6, this.getY(), this.getW() - 12, this.getH()); // Block ohne das er makiert ist
            batch.draw(AssetLoader.marked_links, this.getX(), this.getY(), 6, this.getH());
            batch.draw(AssetLoader.marked_rechts, this.getX() + this.getW() - 6, this.getY(), 6, this.getH());
        }
        if (ProjectManager.getActProjectVar().biggestblock == this) {
            if (this.isShowdupulicate_rechts() && this.getBlocktype().canhasrightconnector()) {
                batch.setColor(1, 1, 1, 0.5f);
                batch.draw(AssetLoader.img_block, this.x_dup_rechts, this.y, ProjectManager.getActProjectVar().markedblock.getW(), this.getH()); //Wenn der Block die größte überlappung hat wird er als show duplicat angezigt
                batch.setColor(1, 1, 1, 1);
            }

            if (this.isShowdupulicate_links() && this.getBlocktype().canhasleftconnector()) {
                batch.setColor(1, 1, 1, 0.5f);
                batch.draw(AssetLoader.img_block, this.x - ProjectManager.getActProjectVar().markedblock.getW(), this.y, ProjectManager.getActProjectVar().markedblock.getW(), this.getH()); //das gleiche für links
                batch.setColor(1, 1, 1, 1);
            }
        }
/*
        if (this.getLeft() != null) { //Verbindungs marke ob der Nachbar verbunden ist
            batch.end();
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(1f, 0.4f, 0.4f, 0.4f);
            shape.ellipse(this.getX() - 6, this.getY() + this.getH() / 2 - 6, 12, 12);
            shape.end();
            batch.begin();
        }

        if (this.getRight() != null) {//Verbindungs marke ob der Nachbar verbunden ist
            batch.end();
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.setColor(1f, 0.4f, 0.4f, 0.4f);
            shape.ellipse(this.getX() - 6 + this.getW(), this.getY() + this.getH() / 2 - 6, 12, 12);
            shape.end();
            batch.begin();
        }

 */
        if (this == ProjectManager.getActProjectVar().biggestblock) { //DEBUG Wer ist der größte Block?
            batch.end();
            shape.begin(ShapeRenderer.ShapeType.Filled);
            shape.rect(x, y, 20, 20);
            shape.end();
            batch.begin();
        }

        if (!this.blockupdate.isIsconnectorclicked() && ProjectManager.getActProjectVar().showleftdocker && this.getLeft() == null && this.getBlocktype().canhasleftconnector()) {
            batch.draw(AssetLoader.connector_offerd, getWireconnector_left().x, getWireconnector_left().y, 20, 20);
        }

        if (this.getRight() == null && this.getBlocktype().canhasrightconnector()) {
            batch.draw(AssetLoader.connector, getwireconnector_right().x, getwireconnector_right().y, 20, 20);
        }

        //font.draw(batch, "index:  " + this.getIndex() + " Block: " + this.getBlocktype().getName(), this.getX() + 5, this.getY() + 30); //DEBUG Block Index auf dem Block anzeigen
        //font.draw(batch,this.getBlocktype().getName(), this.getX() + 5, this.getY() + this.getH() - 10); //DEBUG Block Index auf dem Block anzeigen


        ///////////////////////////////PARAMETER//ANZEIGE/////////////////////////////////////////////////

        if (this.getBlocktype().getBlockParameter() != null && this.getBlocktype().getBlockParameter().size() > 0) {
            float aktualX = this.getX();

            aktualX += 5;
            batch.draw(this.getBlocktype().getDescriptionImage(), aktualX, this.getY() + this.getH() - 30 - 15, 30, 30);
            aktualX += 35;
            for (int i = 0; i < this.getBlocktype().getBlockParameter().size(); i++) {
                batch.draw(this.getBlocktype().getBlockParameter().get(i).getParameterTexture(), aktualX + 5, this.getY() + 30, 20, 20);
                batch.draw(AssetLoader.Plug_ZahlParameter, aktualX, this.getY(), 30, 30);
                font.getData().setScale(0.9f);
                glyphLayout.setText(font, "" + this.getBlocktype().getBlockParameter().get(i).getParameter());//TODO var type!!
                font.draw(batch, glyphLayout, aktualX + 15 - glyphLayout.width / 2, y + glyphLayout.height * 1.5f);
                aktualX += 30;
            }
        }
        /*
        if(this.getBlocktype().getDescriptionImage()!=null) {
            batch.draw(this.getBlocktype().getDescriptionImage(), this.getX() + 5, this.getY() + this.getH() - this.getBlocktype().getDescriptionImage().getWidth()-15);
        }

        try {
    for (int i = 0; i < this.getBlocktype().getBlockParameter().size(); i++) {
        glyphLayout.setText(font, "" +  this.getBlocktype().getBlockParameter().get(i).getParameter());//TODO var type!!
        float x;
        if(this.getBlocktype().getDescriptionImage()!=null) {
            x = this.getX() + (25 * i) + 5 + this.getBlocktype().getDescriptionImage().getWidth() + 15;
        }else{
            x = this.getX() + (25 * i) + 20;
        }
        float y=this.getY() + glyphLayout.height + 5;
        batch.draw(this.getBlocktype().getBlockParameter().get(i).getParameterTexture(),x-10,y+10,20,20);
        font.draw(batch, glyphLayout, x-glyphLayout.width/2, y);
    }
}catch(NullPointerException e){}
        */

    }

    /**
     * @return the position of the two connectors
     */

    public Vector2 getwireconnector_right() {
        wireconnector_right.set(x + w - 7, y + h / 3 + 12);
        return wireconnector_right;
    }

    /**
     * @return the position of the two connectors
     */
    public Vector2 getWireconnector_left() {
        wireconnector_left.set(x + 2, y + h / 3 + 9);
        return wireconnector_left;
    }


    /**
     * Returns the size of the biggest Area which overlaps over the blocks
     *
     * @return
     */

    public int getDublicatmarkedblockuberlappungsflache() {
        int flaeche = 0;
        if (this.isShowdupulicate_rechts()) { //Für rechts

            try {

                flaeche = (CheckKollision.flache(this.getX_dup_rechts(), this.getY(), this.getW(), this.getH(), ProjectManager.getActProjectVar().markedblock.getX(), ProjectManager.getActProjectVar().markedblock.getY())); //Fläsche mit der die Blöcke überlappen um zu brechenen an welchen Block der Block springen wird

            } catch (NullPointerException ignored) {

            }


        }


        if (this.isShowdupulicate_links()) { //Für links
            try {

                flaeche = (CheckKollision.flache(this.x - ProjectManager.getActProjectVar().markedblock.getW(), this.getY(), this.getW(), this.getH(), ProjectManager.getActProjectVar().markedblock.getX(), ProjectManager.getActProjectVar().markedblock.getY()));//Fläsche mit der die Blöcke überlappen um zu brechenen an welchen Block der Block springen wird

            } catch (NullPointerException ignored) {

            }

        }
        return flaeche;
    }

    public int getBlockMarkedblockuberlappungsflache() {
        int flaeche = 0;
        try {
            flaeche = CheckKollision.flache(this.getX(), this.getY(), this.getW(), this.getH(), ProjectManager.getActProjectVar().markedblock.getX(), ProjectManager.getActProjectVar().markedblock.getY()); //Fläche des Markierten Blocks
        } catch (NullPointerException e) {
        }
        return flaeche;
    }

    /**
     * restart the Block Thread if the block is in the cam frustrum again
     * ATTENTION DO NOT CALL THIS FUNKTION OUTSIDE THREADMANAGER THIS MAY CAUSE PERFORMANCE PROBLEMS AND CAN END IN A CRASH
     *
     * @return the block Thread
     */
    public final Thread allowedRestart() { //WARNUNG diese Methode darf nur von ThreadMananger aufgerufen werden
        this.findnewindex();
        blockupdate = blockUpdateGenerator.generate(this); //Wenn der Block wieder in den Sichtbereich Rückt
        blockupdate.start(); //             ...wird der update Thread gestarted

        return blockupdate;
    }

    /***
     *
     * @return the Block Type from this Block
     */
    public PlatformSpecificBlock getBlocktype() {
        return blocktype;
    }

    public void setBlocktype(PlatformSpecificBlock blocktype) {
        this.blocktype = blocktype;
    }

    public BlocktoSaveGenerator getBlocktoSaveGenerator() {
        return blocktoSaveGenerator;
    }


}
