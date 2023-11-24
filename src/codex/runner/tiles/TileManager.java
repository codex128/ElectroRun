/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.tiles;

import codex.boost.GameAppState;
import codex.boost.Listenable;
import codex.runner.Runner;
import codex.runner.build.*;
import codex.runner.build.impl.*;
import codex.runner.build.timing.*;
import codex.runner.input.DevFunctions;
import codex.runner.mesh.TileMesh;
import static codex.runner.utils.GameConstants.TILE_SIZE;
import codex.runner.utils.GameUtils;
import codex.runner.utils.SimpleNoise;
import com.jme3.app.Application;
import com.jme3.asset.TextureKey;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.FunctionId;
import com.simsilica.lemur.input.InputState;
import com.simsilica.lemur.input.StateFunctionListener;
import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author codex
 */
public class TileManager extends GameAppState
        implements Listenable<TileEventListener>, StateFunctionListener {
    
    private TileColumn[] columns;
    private BuildManager buildManager;
    private LinkedList<BuildStack> stacks = new LinkedList<>();
    private TileBuildContext context = new TileBuildContext();
    private SlopeWeightData weightData;
    private BuildTools buildTools;
    private final Node tileScene = new Node();
    private final LinkedList<TileBatch> batches = new LinkedList<>();
    private Material tileMaterial;
    private Runner runner;
    private LinkedList<TileEventListener> listeners = new LinkedList<>();
    private BitmapText stepCounter;
    
    public TileManager() {
        setEnabled(false);
    }
    public TileManager(Runner runner) {
        setRunner(runner);
    }
    
    @Override
    protected void init(Application app) {        
        rootNode.attachChild(tileScene);
        buildManager = new BuildManager(new BuildStack(new Lazy(), new StaticBuilder()));
        initColumns(51);
        initMaterial();
        initGui();
        initInput();
        buildTools = new BuildTools(this);
        fillTileVoid();
    }
    @Override
    protected void cleanup(Application app) {
        tileScene.removeFromParent();
        listeners.clear();
        var im = GuiGlobals.getInstance().getInputMapper();
        im.removeStateListener(this,
            DevFunctions.F_PRINT_STEP,
            DevFunctions.F_PRINT_WEIGHT_1,
            DevFunctions.F_PRINT_WEIGHT_2,
            DevFunctions.F_PRINT_WEIGHT_3
        );
    }
    @Override
    protected void onEnable() {
        GuiGlobals.getInstance().getInputMapper().activateGroup(DevFunctions.GROUP);
    }
    @Override
    protected void onDisable() {
        GuiGlobals.getInstance().getInputMapper().deactivateGroup(DevFunctions.GROUP);
    }
    @Override
    public void update(float tpf) {
        fillTileVoid();
        for (var b : batches) {
            b.getGeometry().move(0f, 0f, -runner.getVelocity().z*tpf);
        }
        while (!batches.isEmpty() && batches.getFirst().getPosition().z < context.getKillThreshold()) {
            destroyFirstBatch();
            runner.incrementStep();
        }
        for (var c : columns) {
            c.getColor().h += context.getColorChangeRate()*tpf;
            c.getColor().wrapHue();
        }
        stepCounter.setText("step="+buildManager.getGlobalStep());
    }    
    @Override
    public Collection<TileEventListener> getListeners() {
        return listeners;
    }
    @Override
    public void valueChanged(FunctionId func, InputState value, double tpf) {
        if (value == InputState.Positive) {
            if (func == DevFunctions.F_PRINT_STEP) {
                System.out.println("GlobalStep="+buildManager.getGlobalStep());
            }
            else if (func == DevFunctions.F_PRINT_WEIGHT_1) {
                System.out.println("  weight=1");
            }
            else if (func == DevFunctions.F_PRINT_WEIGHT_2) {
                System.out.println("  weight=2");
            }
            else if (func == DevFunctions.F_PRINT_WEIGHT_3) {
                System.out.println("  weight=3");
            }
        }
    }
    
    private void initColumns(int n) {
        columns = new TileColumn[n];
        float hue = 0f;
        SimpleNoise tendency = new SimpleNoise(0f);
        tendency.getStepDomain().set(0f, 2f);
        tendency.getGlobalDomain().set(-2f, 6f);
        for (int i = 0; i < columns.length; i++) {
            columns[i] = new TileColumn(i);
            columns[i].setUpTendency((int)tendency.getNext());
            columns[i].getColor().setHue(hue);
            hue += 0.05f;
            if (hue > 1) hue = 0;
        }
        context.setColumnOffset(-0.5f*TILE_SIZE.x*n);
    }
    private void initMaterial() {
        tileMaterial = new Material(assetManager, "MatDefs/tile.j3md");
        tileMaterial.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        tileMaterial.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        tileMaterial.setTransparent(true);
        var texKey = new TextureKey("Textures/tile-texture.png");
        texKey.setFlipY(false);
        texKey.setGenerateMips(true);
        texKey.setAnisotropy(7);
        tileMaterial.setTexture("Texture", assetManager.loadTexture(texKey));
        tileMaterial.setVector2("Gradient1", new Vector2f(context.getSpawnPoint(), context.getSpawnPoint()-TILE_SIZE.z*1.3f));
        tileMaterial.setVector2("Gradient2", new Vector2f(context.getKillThreshold()+TILE_SIZE.z*2.5f
                , context.getKillThreshold()+TILE_SIZE.z*3));
    }
    private void initGui() {
        BitmapFont guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        stepCounter = new BitmapText(guiFont);
        stepCounter.setSize(guiFont.getCharSet().getRenderedSize());
        stepCounter.setText("");
        stepCounter.setLocalTranslation(5, windowSize.y-5, 0);
        guiNode.attachChild(stepCounter);
    }
    private void initInput() {
        var im = GuiGlobals.getInstance().getInputMapper();
        im.addStateListener(this,
            DevFunctions.F_PRINT_STEP,
            DevFunctions.F_PRINT_WEIGHT_1,
            DevFunctions.F_PRINT_WEIGHT_2,
            DevFunctions.F_PRINT_WEIGHT_3
        );
    }
    
    private void fillTileVoid() {
        while (batches.isEmpty() || batches.getLast().getPosition().z < context.getSpawnPoint()) {
            spawnBatch();
        }
    }
    private void spawnBatch() {
        buildTools.updateGlobals();
        buildTools.updateSlopeBias(weightData);
        var batch = new TileBatch(tileMaterial, columns.length);
        batch.getGeometry().setQueueBucket(RenderQueue.Bucket.Transparent);
        if (!batches.isEmpty()) {
            batch.getGeometry().setLocalTranslation(0, 0, getLastBatch().getPosition().z+TILE_SIZE.z);
        }
        else {
            batch.getGeometry().setLocalTranslation(0, 0, context.getKillThreshold()+TILE_SIZE.z);
        }
        tileScene.attachChild(batch.getGeometry());
        BuildStack stack = buildManager.getCurrentStack();
        for (var m : stack) {
            m.preBatch(buildTools);
        }
        on: for (var c : columns) {
            var tileInfo = new TileInfo(c.getIndex(), c.getHeight());
            tileInfo.setPosition(new Vector3f(c.getIndex()*TILE_SIZE.x+context.getColumnOffset(), c.getHeight()*TILE_SIZE.y, 0));
            tileInfo.setNoiseSample(GameUtils.sampleNoiseByInt(context.getNoise(), c.getIndex(), buildManager.getGlobalStep()));
            tileInfo.setColor(c.getColor());
            buildTools.update(c, tileInfo);
            for (var m : stack) if (!m.processTile(buildTools)) {
                continue on;
            }
            // height will only change if a tile is created
            c.setHeight(c.getHeight()+tileInfo.getLevelChange());
            batch.add(tileInfo, createTileMesh(tileInfo));
        }
        for (var m : stack) {
            m.postBatch(buildTools);
        }
        buildManager.updateStep(context.getStepDelay());
        batches.addLast(batch);
        batch.merge();
        notifyListeners(l -> l.onBatchCreated(batch));
    }
    private void destroyFirstBatch() {
        var batch = batches.removeFirst();
        batch.getGeometry().removeFromParent();
        notifyListeners(l -> l.onBatchDestroyed(batch));
    }
    private TileMesh createTileMesh(TileInfo info) {
        var mesh = new TileMesh();
        mesh.updateArrays(
            info.getPosition(),
            info.getPosition().add(0, info.getLevelChange()*TILE_SIZE.y, TILE_SIZE.z),
            TILE_SIZE.x,
            info.getColor().toRGBA(),
            0, 5
        );
        return mesh;
    }
    private TileBatch getLastBatch() {
        return batches.getLast();
    }
    
    public final void setRunner(Runner runner) {
        this.runner = runner;
        setEnabled(this.runner != null);
    }
    public void setWeightData(SlopeWeightData data) {
        this.weightData = data;
    }
    
    public BuildManager getBuildManager() {
        return buildManager;
    }
    public TileBuildContext getContext() {
        return context;
    }
    public Runner getRunner() {
        return runner;
    }
    public TileColumn[] getColumns() {
        return columns;
    }
    public LinkedList<TileBatch> getBatches() {
        return batches;
    }
    
    public TileBatch getBatchByPosition(Vector3f position) {
        // the batches are, by nature, sorted by Z position
        for (var b : batches) {
            float z = b.getPosition().z;
            if (z <= position.z && z+TILE_SIZE.z > position.z) {
                return b;
            }
        }
        return null;
    }
    public TileColumn getColumnByPosition(Vector3f position) {
        int i = (int)((position.x+context.getColumnOffset())/TILE_SIZE.x);
        if (i < 0 || i >= columns.length) return null;
        else return columns[i];
    }
    public TileInfo getTileByPosition(Vector3f position) {
        var column = getColumnByPosition(position);
        var batch = getBatchByPosition(position);
        return batch.getTileArray()[column.getIndex()];
    }
    public int getHeightByPosition(Vector3f position) {
        return (int)(position.y/TILE_SIZE.y)+(position.y < 0 ? -1 : 0);
    }
    
}
