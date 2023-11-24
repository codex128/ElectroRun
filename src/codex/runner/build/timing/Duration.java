/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.build.timing;

/**
 *
 * @author codex
 */
public abstract class Duration {
    
    protected final int time;
    
    public Duration() {
        this(0);
    }
    public Duration(int time) {
        this.time = time;
    }
    
    public abstract boolean readyToStart(int step);
    public abstract boolean readyToEnd(int step);
    
    public int getTime() {
        return time;
    }
    
}
