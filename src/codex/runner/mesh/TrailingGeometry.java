/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.mesh;

import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import java.util.LinkedList;

/**
 *
 * @author codex
 */
public class TrailingGeometry extends Geometry {
    
    private TrailingMesh tMesh;
    private final Transform worldSpawn = new Transform();
    private float spawnRate = .03f;
    private float time = 0f;
    private boolean useVertSmoothing = true;
    private LinkedList<PointInfluencer> influencers = new LinkedList<>();
    
    public TrailingGeometry() {
        //setCullHint(Spatial.CullHint.Never);
        setQueueBucket(RenderQueue.Bucket.Transparent);
    }
    
    @Override
    public void updateLogicalState(float tpf) {
        updateNormal();
        updateInfluences(tpf);
        updateSpawn(tpf);
        if (useVertSmoothing) {
            material.setVector3("TrailOrigin", worldSpawn.getTranslation(null));
        }
    }    
    @Override
    public void updateWorldTransforms() {
        if (parent == null) {
            //worldTransform.set(localTransform);
            worldSpawn.set(localTransform);
            //worldTransform.set(Transform.IDENTITY);
            refreshFlags &= ~RF_TRANSFORM;
        }
        else {
            // check if transform for parent is updated
            //assert ((parent.refreshFlags & RF_TRANSFORM) == 0) : "Illegal rf transform update. Problem spatial name: " + getName();
            worldSpawn.set(localTransform);
            worldSpawn.combineWithParent(parent.getWorldTransform());
            //worldTransform.set(Transform.IDENTITY);
            refreshFlags &= ~RF_TRANSFORM;
        }
        worldTransform.set(Transform.IDENTITY);
        computeWorldMatrix();
        if (isGrouped()) {
            groupNode.onTransformChange(this);
        }
        worldLights.sort(true);
    }
    @Override
    public void setMesh(Mesh mesh) {
        if (!(mesh instanceof TrailingMesh)) {
            throw new IllegalArgumentException("TrailGeometry only accepts TrailMesh.");
        }
        super.setMesh(mesh);
        tMesh = (TrailingMesh)mesh;
        if (material != null) {
            material.setBoolean("FaceCamera", tMesh.isFaceCamera());
        }
    }
    @Override
    public TrailingMesh getMesh() {
        return tMesh;
    }
    @Override
    public void setMaterial(Material material) {
        super.setMaterial(material);
        if (this.material != null && !useVertSmoothing) {
            setUseVertSmoothing(false);
            if (tMesh != null) {
                this.material.setBoolean("FaceCamera", tMesh.isFaceCamera());
            }
        }
    }
    @Override
    public Transform getWorldTransform() {
        return worldSpawn;
    }
    @Override
    public Vector3f getWorldTranslation() {
        return worldSpawn.getTranslation();
    }
    @Override
    public Quaternion getWorldRotation() {
        return worldSpawn.getRotation();
    }
    @Override
    public Vector3f getWorldScale() {
        return worldSpawn.getScale();
    }
    
    private void updateNormal() {
        if (tMesh == null) return;
        tMesh.setNormal(getWorldRotation().getRotationColumn(2));
    }
    private void updateInfluences(float tpf) {
        for (var p : tMesh.getPoints()) {
            for (var i : influencers) {
                i.influence(p, tpf);
            }
        }
    }
    private void updateSpawn(float tpf) {
        time += tpf;
        if (time >= spawnRate) {
            tMesh.addPoint(worldSpawn.getTranslation());
            tMesh.updateMesh();
            time = 0;
        }
    }
    
    public void setMesh(TrailingMesh mesh) {
        super.setMesh(mesh);
        tMesh = mesh;
    }
    public TrailingMesh generateMesh() {
        tMesh = new TrailingMesh();
        super.setMesh(tMesh);
        return tMesh;
    }
    
    public void addPointInfluencer(PointInfluencer p) {
        influencers.addLast(p);
    }
    public void removePointInfluencer(PointInfluencer p) {
        influencers.remove(p);
    }
    
    /**
     * Sets the rate (in seconds) at which new vertex pairs are spawned.
     * <p>
     * default=0.03
     * 
     * @param rate 
     */
    public void setSpawnRate(float rate) {
        this.spawnRate = rate;
    }
    
    /**
     * Makes the leading (newest) vertex pair updated to the
     * trail origin.
     * <p>
     * This makes the leading part of the trail not jump, but
     * move smoothly. May cause visibly texture stretching if
     * the mesh resolution is not great enough.
     * 
     * @param useVertSmoothing 
     */
    public void setUseVertSmoothing(boolean useVertSmoothing) {
        this.useVertSmoothing = useVertSmoothing;
        if (!this.useVertSmoothing) {
            material.clearParam("TrailOrigin");
        }
    }
    
    /**
     * Makes this geometry's mesh face the camera.
     * <p>
     * Does nothing if this geometry has no mesh.
     * 
     * @param faceCamera 
     */
    public void setFaceCamera(boolean faceCamera) {
        if (tMesh == null) return;
        tMesh.setFaceCamera(faceCamera);
        if (material != null) {
            material.setBoolean("FaceCamera", tMesh.isFaceCamera());
        }
    }
    
}
