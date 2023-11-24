/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.mesh;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.simsilica.lemur.Axis;

/**
 *
 * @author codex
 */
public class TileMesh extends Mesh {
    
    private float[] verts, uvs, colors;
    private int[] faces;
    
    public void updateArrays(Vector3f p1, Vector3f p2, float w, ColorRGBA rgb, int slot, int numSlots) {
        verts = new float[] {
            p1.x, p1.y, p1.z,
            p2.x, p2.y, p2.z,
            p2.x+w, p2.y, p2.z,
            p1.x+w, p1.y, p1.z
        };
        float low = (1f/numSlots)*slot;
        float hi = (1f/numSlots)*(slot+1);
        uvs = new float[] {low, 0, low, 1, hi, 1, hi, 0};
        faces = new int[] {0, 1, 3, 2, 3, 1};
        colors = new float[16];
        for (int i = 0; i < colors.length;) {
            colors[i++] = rgb.r;
            colors[i++] = rgb.g;
            colors[i++] = rgb.b;
            colors[i++] = rgb.a;
        }
    }
    public void updateBuffers(Vector3f p1, Vector3f p2, float w, ColorRGBA rgb, int slot, int numSlots) {
        updateArrays(p1, p2, 2, rgb, slot, numSlots);
        setBuffer(VertexBuffer.Type.Position, 3, verts);
        setBuffer(VertexBuffer.Type.TexCoord, 2, uvs);
        setBuffer(VertexBuffer.Type.Index, 3, faces);
        setBuffer(VertexBuffer.Type.Color, 4, colors);
        updateBound();
    }

    public float[] getVerts() {
        return verts;
    }
    public float[] getUvs() {
        return uvs;
    }
    public float[] getColors() {
        return colors;
    }
    public int[] getFaces() {
        return faces;
    }
    
}
