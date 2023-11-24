/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.build;

import codex.runner.build.timing.Duration;
import java.util.LinkedList;

/**
 *
 * @author codex
 */
public class BuildStack extends LinkedList<TileBuilder> {
    
    private Duration duration;
    
    public BuildStack(Duration duration) {
        this.duration = duration;
    }
    public BuildStack(Duration duration, TileBuilder... machines) {
        this(duration);
        for (var m : machines) {
            addLast(m);
        }
    }

    public Duration getDuration() {
        return duration;
    }
    
}
