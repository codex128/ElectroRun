/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runnertest;

import codex.runner.mesh.TrailingGeometry;
import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.scene.Node;

/**
 *
 * @author codex
 */
public class TestTrailing extends SimpleApplication {
    
    public static void main(String[] args) {
        new TestTrailing().start();
    }
    
    @Override
    public void simpleInitApp() {
        
        viewPort.setBackgroundColor(ColorRGBA.DarkGray);
        flyCam.setMoveSpeed(10);
        
        var fpp = new FilterPostProcessor(assetManager);
        var bloom = new BloomFilter(BloomFilter.GlowMode.Objects);
        fpp.addFilter(bloom);
        viewPort.addProcessor(fpp);
        
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        //bot.move(0f, 0f, tpf*2);
        //cam.lookAt(bot.getLocalTranslation().add(0f, 3f, 0f), Vector3f.UNIT_Y);
    }
    
    private TrailingGeometry createTrailEffect(Node node) {
        var geometry = new TrailingGeometry();
        geometry.generateMesh();
        geometry.getMesh().setRadius(.07f);
        geometry.getMesh().setLimit(20);
        geometry.setSpawnRate(.0f);
        var mat = new Material(assetManager, "MatDefs/trail.j3md");
        mat.setTexture("Texture", assetManager.loadTexture("Textures/Effects/wispy-trail.png"));
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        //mat.getAdditionalRenderState().setWireframe(true);
        mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        mat.setTransparent(true);
        geometry.setMaterial(mat);
        node.attachChild(geometry);
        return geometry;
    }
    
}
