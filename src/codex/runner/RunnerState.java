/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner;

import codex.runner.tiles.TileManager;
import codex.runner.input.RunnerFunctions;
import codex.boost.GameAppState;
import codex.runner.mesh.TrailingGeometry;
import com.jme3.anim.SkinningControl;
import com.jme3.app.Application;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.input.AnalogFunctionListener;
import com.simsilica.lemur.input.FunctionId;

/**
 *
 * @author codex
 */
public class RunnerState extends GameAppState implements AnalogFunctionListener {
    
    private TileManager tileManager;
    private Runner runner;
    
    @Override
    protected void init(Application app) {
        
        Spatial model = assetManager.loadModel("Models/motorbike/motorbike.j3o");
        model.setLocalTranslation(0f, 0f, 0f);
        model.setLocalRotation(new Quaternion().fromAngleAxis(FastMath.PI, Vector3f.UNIT_Y));
        model.setLocalScale(0.6f);
        Material mat = new Material(assetManager, "MatDefs/bike.j3md");
        mat.setTexture("Texture", assetManager.loadTexture(new TextureKey("Models/motorbike/bike-diffuse.png", false)));
        mat.setFloat("Alpha", 1f);
        //mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        //mat.setTransparent(true);
        //model.setQueueBucket(RenderQueue.Bucket.Transparent);
        
        //mat.getAdditionalRenderState().setWireframe(true);
        model.setMaterial(mat);
        rootNode.attachChild(model);
        
        var skin = model.getControl(SkinningControl.class);
        
        var trail = new TrailingGeometry();
        trail.generateMesh();
        trail.getMesh().setRadius(.1f);
        trail.getMesh().setLimit(10);
        
        trail.setSpawnRate(.0f);
        var tMat = new Material(assetManager, "MatDefs/trail.j3md");
        tMat.setTexture("Texture", assetManager.loadTexture("Textures/Effects/wispy-trail.png"));
        tMat.setColor("Color1", ColorRGBA.Blue.mult(.5f));
        tMat.setColor("Color2", ColorRGBA.Cyan.mult(.5f));
        tMat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        //mat.getAdditionalRenderState().setWireframe(true);
        tMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        tMat.setTransparent(true);
        trail.setMaterial(tMat);
        //skin.getAttachmentsNode("rear-wheel-attach").attachChild(trail);
        
        runner = new Runner();
        runner.setSpatial(model);
        //trail.addPointInfluencer(runner);
        
        tileManager = getState(TileManager.class, true);
        tileManager.setRunner(runner);
        
        var im = GuiGlobals.getInstance().getInputMapper();
        im.addAnalogListener(this, RunnerFunctions.F_STRAFE, RunnerFunctions.F_JUMP);
        
    }
    @Override
    protected void cleanup(Application app) {        
        var im = GuiGlobals.getInstance().getInputMapper();
        im.removeAnalogListener(this, RunnerFunctions.F_STRAFE, RunnerFunctions.F_JUMP);
    }
    @Override
    protected void onEnable() {
        GuiGlobals.getInstance().getInputMapper().activateGroup(RunnerFunctions.GROUP);
    }
    @Override
    protected void onDisable() {
        GuiGlobals.getInstance().getInputMapper().deactivateGroup(RunnerFunctions.GROUP);
    }
    @Override
    public void update(float tpf) {
        runner.update(tileManager, tpf);
    }
    @Override
    public void valueActive(FunctionId func, double value, double tpf) {
        if (func == RunnerFunctions.F_STRAFE) {
            runner.getInputVector().x = -(float)value;
        }
        else if (func == RunnerFunctions.F_JUMP) {
            runner.getInputVector().y = 1;
        }
    }
    
}
