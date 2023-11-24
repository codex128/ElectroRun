/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.mesh;

import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import java.util.LinkedList;

/**
 *
 * @author codex
 */
public class MergedTileMesh extends Mesh {
    
    private LinkedList<TileMesh> meshes = new LinkedList<>();
    
    public void merge() {
        float[] verts = new float[meshes.size() * 4 * 3];
        float[] uvs = new float[meshes.size() * 4 * 2];
        float[] colors = new float[meshes.size() * 4 * 4];
        int[] faces = new int[meshes.size() * 6];
        int iVert = 0, iUv = 0, iColor = 0, iFace = 0, chunk = 0;
        for (var mesh : meshes) {
            for (float v : mesh.getVerts()) {
                verts[iVert++] = v;
            }
            for (float u : mesh.getUvs()) {
                uvs[iUv++] = u;
            }
            for (float c : mesh.getColors()) {
                colors[iColor++] = c;
            }
            for (int f : mesh.getFaces()) {
                faces[iFace++] = chunk+f;
            }
            chunk += 4;
        }
        setBuffer(VertexBuffer.Type.Position, 3, verts);
        setBuffer(VertexBuffer.Type.TexCoord, 2, uvs);
        setBuffer(VertexBuffer.Type.Index, 3, faces);
        setBuffer(VertexBuffer.Type.Color, 4, colors);
        updateBound();
    }
    public void addMesh(TileMesh mesh) {
        meshes.add(mesh);
    }
    
}
