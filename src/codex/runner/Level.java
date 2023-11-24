/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner;

import codex.runner.build.BuildStack;
import codex.runner.build.TileBuilder;
import codex.runner.build.timing.Duration;
import java.util.ArrayList;

/**
 *
 * @author codex
 */
public class Level {
    
    private String name, music, slopeWeightAsset;
    private ArrayList<BuildStack> stacks = new ArrayList<>();

    public Level(String name) {
        this.name = name;
    }
    public Level(String name, String music) {
        this.name = name;
        this.music = music;
    }

    public String getName() {
        return name;
    }
    public String getMusic() {
        return music;
    }
    public String getSlopeWeightAsset() {
        return slopeWeightAsset;
    }
    
    public void addStack(BuildStack stack) {
        stacks.add(stack);
    }
    public void addStack(Duration duration, TileBuilder... builders) {
        stacks.add(new BuildStack(duration, builders));
    }
    public ArrayList<BuildStack> getBuildStacks() {
        return stacks;
    }

    public void setSlopeWeightAsset(String slopeWeightAsset) {
        this.slopeWeightAsset = slopeWeightAsset;
    }
    
}
