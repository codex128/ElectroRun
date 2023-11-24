/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.tiles;

import codex.runner.mesh.MergedTileMesh;
import codex.runner.mesh.TileMesh;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author codex
 */
public class TileBatch {
    
    private final Geometry geometry;
    private final MergedTileMesh mesh = new MergedTileMesh();
    private final TileInfo[] tiles;
    
    public TileBatch(Material mat, int numTiles) {
        geometry = new Geometry("tile-batch", mesh);
        geometry.setMaterial(mat);
        tiles = new TileInfo[numTiles];
    }

    public void add(TileInfo tile, TileMesh mesh) {
        tiles[tile.getColumn()] = tile;
        this.mesh.addMesh(mesh);
    }    
    public void merge() {
        mesh.merge();
    }
    
    public MergedTileMesh getMesh() {
        return mesh;
    }
    public Geometry getGeometry() {
        return geometry;
    }
    public TileInfo[] getTileArray() {
        return tiles;
    }
    public Vector3f getPosition() {
        return geometry.getLocalTranslation();
    }
    
}
