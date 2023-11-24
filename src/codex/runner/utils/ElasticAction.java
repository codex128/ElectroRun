/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.utils;

/**
 *
 * @author codex
 */
public class ElasticAction {
    
    private float target;
    private float blend;
    private Elastic.Mode mode;

    public ElasticAction(float blend, Elastic.Mode mode) {
        this(0, blend, mode);
    }
    public ElasticAction(float target, float blend, Elastic.Mode mode) {
        this.target = target;
        this.blend = blend;
        this.mode = mode;
    }

    public float getTarget() {
        return target;
    }
    public float getBlend() {
        return blend;
    }
    public Elastic.Mode getMode() {
        return mode;
    }
    
    public void setTarget(float target) {
        this.target = target;
    }
    
}
