/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.utils;

/**
 *
 * @author codex
 */
public class IntRange {
    
    private int min, max;
    
    public IntRange(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public boolean isWithin(int n) {
        return n >= min && n <= max;
    }
    
    public void setMin(int min) {
        this.min = min;
    }
    public void setMax(int max) {
        this.max = max;
    }
    
    public int getMin() {
        return min;
    }
    public int getMax() {
        return max;
    }
    
}
