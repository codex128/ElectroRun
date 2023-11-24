/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.utils;

import com.jme3.math.FastMath;
import java.util.HashMap;

/**
 *
 * @author codex
 */
public class Elastic {
    
    public enum Mode {
        Linear, Lerp;
    }
    
    private float value;
    private ElasticAction current;
    private HashMap<String, ElasticAction> actions = new HashMap<>();

    public Elastic() {
        this(0);
    }
    public Elastic(float value) {
        this.value = value;
    }
    
    public float update(float tpf) {
        if (current == null) {
            return value;
        }
        else return switch (current.getMode()) {
            case Lerp -> updateLerp();
            case Linear -> updateLinear(tpf);
        };
    }
    private float updateLinear(float tpf) {
        if (FastMath.abs(value-current.getTarget()) < current.getBlend()*tpf) {
            return (value = current.getTarget());
        }
        return (value -= current.getBlend()*tpf*FastMath.sign(value-current.getTarget()));
    }
    private float updateLerp() {
        return (value = FastMath.interpolateLinear(current.getBlend(), value, current.getTarget()));
    }
    
    public void setValue(float value) {
        this.value = value;
    }
    public ElasticAction setCurrentAction(String action) {
        return (current = actions.get(action));
    }
    public ElasticAction setCurrentAction(String action, float target) {
        var a = setCurrentAction(action);
        a.setTarget(target);
        return a;
    }
    
    public ElasticAction addAction(String name, ElasticAction action) {
        actions.put(name, action);
        return action;
    }

    public float getValue() {
        return value;
    }
    public ElasticAction getCurrentAction() {
        return current;
    }
    public ElasticAction action(String action) {
        return actions.get(action);
    }
    
}
