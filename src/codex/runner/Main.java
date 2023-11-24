package codex.runner;

import codex.runner.asset.SlopeWeightLoader;
import codex.runner.input.DevFunctions;
import codex.runner.tiles.TileManager;
import codex.runner.input.RunnerFunctions;
import com.jme3.app.SimpleApplication;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.BloomFilter;
import com.jme3.renderer.RenderManager;
import com.simsilica.lemur.GuiGlobals;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
        GuiGlobals.initialize(this);
        GuiGlobals.getInstance().setCursorEventsEnabled(false);
        RunnerFunctions.initialize(GuiGlobals.getInstance());
        DevFunctions.initialize(GuiGlobals.getInstance());
        
        assetManager.registerLoader(SlopeWeightLoader.class, "sw");
        
        //viewPort.setBackgroundColor(ColorRGBA.White);
        flyCam.setMoveSpeed(20);
        
        stateManager.attachAll(new TileManager(), new RunnerState(), new GameState());
        
        var fpp = new FilterPostProcessor(assetManager);
        var bloom = new BloomFilter(BloomFilter.GlowMode.Objects);
        bloom.setBlurScale(1.0f);
        bloom.setBloomIntensity(2.0f);
        bloom.setExposureCutOff(1.0f);
        bloom.setExposurePower(5.0f);
        fpp.addFilter(bloom);
        viewPort.addProcessor(fpp);
        
        cam.setFov(60);
        
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
