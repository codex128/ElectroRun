/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner;

import codex.boost.GameAppState;
import codex.runner.build.BuildStack;
import codex.runner.build.SlopeWeightData;
import codex.runner.build.impl.*;
import codex.runner.build.impl.NormalizedHeightBuilder.Mode;
import codex.runner.build.timing.*;
import codex.runner.tiles.TileBatch;
import codex.runner.tiles.TileEventListener;
import codex.runner.tiles.TileManager;
import com.jme3.app.Application;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;

/**
 *
 * @author codex
 */
public class GameState extends GameAppState implements TileEventListener {
    
    private TileManager tileManager;
    private Level level;
    private AudioNode music;
    
    @Override
    protected void init(Application app) {
        
        level = new Level("Hello World", "Sounds/music/emotional-battle.wav");
        level.setSlopeWeightAsset("Properties/main.sw");
        level.addStack(new Lazy(),      new StaticBuilder());
        level.addStack(new Begin(10),   new StaticBuilder(1, 3, 0));
        level.addStack(new Begin(22),   new StaticBuilder(1, 2, 0));
        level.addStack(new Begin(30),   new StaticBuilder(1, 1, 0));
        level.addStack(new Begin(35),   new StaticBuilder(2, 1, 0));
        level.addStack(new Begin(41),   new IslandBuilder(.1f));
        level.addStack(new Once(73),    new LevelChangeBuilder(1));
        level.addStack(new Lazy(),      new FlatBuilder(.55f));
        level.addStack(new Begin(90),   new FlatBuilder(.5f));
        level.addStack(new Begin(104),  new FlatBuilder(.35f));
        level.addStack(new Once(135),   new LevelChangeBuilder(1));
        level.addStack(new Lazy(),      new LimitedTerrainBuilder(.6f, .1f, 2, 4));
        // the player should hit the ground at 197
        level.addStack(new Once(188),   new NormalizedHeightBuilder(Mode.Absolute, -5, true));
        level.addStack(new Lazy(),      new InverseMaskBuilder(new WaveBuilder(), new FlatBuilder(.05f)));
        level.addStack(new Begin(210),  new WaveBuilder());
        level.addStack(new Once(259),   new LevelChangeBuilder(1));
        level.addStack(new Lazy(),      new AdvancedTerrainBuilder(1f, 0f));
        level.addStack(new Once(570),   new LevelChangeBuilder(1));
        level.addStack(new Lazy(),      new AdvancedTerrainBuilder(.8f, .09f));
        level.addStack(new Once(695),   new LevelChangeBuilder(1));
        level.addStack(new Lazy(),      new AdvancedTerrainBuilder(.8f, .0f));
        level.addStack(new Once(942),   new NormalizedHeightBuilder(Mode.ToHighest, -7, true),
                                        new RaiseMiddleBuilder(2));
        level.addStack(new End(1000),   new StaticBuilder(-1, 2, 5));
        tileManager = getState(TileManager.class, true);
        tileManager.addListener(this);
        
        music = new AudioNode(assetManager, level.getMusic(), AudioData.DataType.Stream);
        //music.setLooping(true);
        music.setPositional(false);
        rootNode.attachChild(music);
        
    }
    @Override
    protected void cleanup(Application app) {
        tileManager.removeListener(this);
        music.stop();
        music.removeFromParent();
    }
    @Override
    protected void onEnable() {
        tileManager.getBuildManager().reset();
        tileManager.getBuildManager().addBuildStacks(level.getBuildStacks());
        tileManager.setWeightData((SlopeWeightData)assetManager.loadAsset(level.getSlopeWeightAsset()));
    }
    @Override
    protected void onDisable() {
        tileManager.getBuildManager().clearStackList();
    }
    @Override
    public void onBatchCreated(TileBatch batch) {
        if (tileManager.getBuildManager().getGlobalStep() == 1) {
            music.play();
        }
    }
    @Override
    public void onBatchDestroyed(TileBatch batch) {}
    @Override
    public void onBuildStackSwitched(BuildStack prev, BuildStack next) {}
    
}
