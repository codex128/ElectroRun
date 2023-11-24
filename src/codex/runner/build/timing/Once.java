/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.build.timing;

/**
 *
 * @author codex
 */
public class Once extends Duration {

    public Once(int time) {
        super(time);
    }

    @Override
    public boolean readyToStart(int step) {
        return step >= time;
    }
    @Override
    public boolean readyToEnd(int step) {
        return step > time;
    }
    
}
