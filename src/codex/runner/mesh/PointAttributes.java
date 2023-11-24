/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.mesh;

import com.jme3.math.Vector3f;

/**
 *
 * @author codex
 */
public class PointAttributes {
        
    public Vector3f point;
    public Vector3f normal;
    public float radius;

    public PointAttributes(Vector3f point, Vector3f normal, float radius) {
        this.point = point;
        this.normal = normal;
        this.radius = radius;
    }

}
