/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.utils;

import com.jme3.light.PointLight;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author codex
 */
public class PointLightControl extends AbstractControl {

    private PointLight light;
    private Vector3f offset = new Vector3f();

    public PointLightControl(PointLight light) {
        this.light = light;
    }    
    public PointLightControl(PointLight light, Vector3f offset) {
        this.light = light;
        this.offset.set(offset);
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        light.setPosition(spatial.getWorldTranslation().add(offset));
    }
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {}
    
}
