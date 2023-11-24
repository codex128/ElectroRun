/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.runner.mesh;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import java.util.ArrayList;

/**
 *
 * @author codex
 */
public class TrailingMesh extends Mesh {
    
    private final ArrayList<PointAttributes> points = new ArrayList<>();
    private final Vector3f normal = new Vector3f(1, 0, 0);
    private int limit = 50;
    private float radius = .3f;
    private float uvExtent = 1f;
    private boolean faceCamera = true;
    
    /**
     * Updates the mesh data to changes in points and mesh properties.
     * <p>
     * Must be manually called unless using {@link TrailingGeometry}.
     */
    public void updateMesh() {
        if (points.size() < 2) {
            clearBuffer(VertexBuffer.Type.Position);
            clearBuffer(VertexBuffer.Type.TexCoord);
            clearBuffer(VertexBuffer.Type.TexCoord2);
            clearBuffer(VertexBuffer.Type.TexCoord3);
            clearBuffer(VertexBuffer.Type.Index);
            updateBound();
            return;
        }
        // TODO: save previous calculations, so we don't have to calculate everything for each update
        Vector3f[] verts = new Vector3f[points.size() * 2];
        Vector2f[] uvs = new Vector2f[points.size() * 2];
        // x=fadeCoord, y=leading-vert-pair
        Vector2f[] info = new Vector2f[points.size() * 2];
        int[] faces = new int[(points.size()-1) * 6];
        Vector4f[] axis = new Vector4f[points.size() * 2];
        int iVert = 0, iFace = 0;
        float uvx = 0f, fadeX = 0f;
        for (int i = 0; i < points.size(); i++) {
            PointAttributes attr = points.get(i);
            // configure the rotation axis
            Vector3f rotAxis;
            if (i+1 >= points.size()) {
                // axis away from previous point
                rotAxis = points.get(i-1).point.subtract(attr.point).normalizeLocal().negateLocal();
            }
            else if (i == 0) {
                // axis towards next point
                rotAxis = points.get(i+1).point.subtract(attr.point).normalizeLocal();
            }
            else {
                // axis as average between direction to next (forward) and previous (negated)
                Vector3f toNext = points.get(i+1).point.subtract(attr.point).normalizeLocal();
                Vector3f toPrev = points.get(i-1).point.subtract(attr.point).normalizeLocal();
                rotAxis = toNext.addLocal(toPrev.negateLocal()).normalizeLocal();
            }
            if (faceCamera) {
                verts[iVert] = verts[iVert+1] = attr.point;
                axis[iVert] = buildVector4f(rotAxis, attr.radius);
                axis[iVert+1] = buildVector4f(rotAxis, -attr.radius);
            }
            else {
                verts[iVert] = rotAxis.cross(attr.normal).multLocal(attr.radius);
                verts[iVert+1] = verts[iVert].negate().addLocal(attr.point);
                verts[iVert].addLocal(attr.point);
            }
            uvs[iVert] = new Vector2f(uvx, 0);
            uvs[iVert+1] = new Vector2f(uvx, 1);
            info[iVert] = info[iVert+1] = new Vector2f(fadeX, isLeadingVertPair(i));
            if (i+1 < points.size()) {
                fadeX += 1f/points.size();
                uvx += uvExtent/limit;
                faces[iFace]   = iVert;
                faces[iFace+1] = iVert+1;
                faces[iFace+2] = iVert+2;
                faces[iFace+3] = iVert+3;
                faces[iFace+4] = iVert+2;
                faces[iFace+5] = iVert+1;
            }
            iVert += 2;
            iFace += 6;
        }
        setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(verts));
        setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(uvs));
        setBuffer(VertexBuffer.Type.TexCoord2, 2, BufferUtils.createFloatBuffer(info));
        if (faceCamera) {
            setBuffer(VertexBuffer.Type.TexCoord3, 4, BufferUtils.createFloatBuffer(axis));
        }
        setBuffer(VertexBuffer.Type.Index, 3, faces);
        updateBound();
    }
    
    private Vector4f buildVector4f(Vector3f vec, float w) {
        return new Vector4f(vec.x, vec.y, vec.z, w);
    }
    private void trimPointList() {
        if (points.size() > limit) {
            //System.out.println("remove point");
            points.remove(0);
            trimPointList();
        }
    }
    private float isLeadingVertPair(int i) {
        return i == points.size()-1 ? 1 : 0;
    }
    protected void setNormal(Vector3f normal) {
        this.normal.set(normal);
    }
    
    /**
     * Add a point to the leading end of this mesh.
     * <p>
     * If the number of points exceeds the point limit,
     * the oldest (trailing end) point will be removed.
     * <p>
     * Note: this method does not update the mesh.
     * 
     * @param p 
     */
    public void addPoint(Vector3f p) {
        points.add(new PointAttributes(p.clone(), normal.clone(), radius));
        trimPointList();
    }
    
    /**
     * Sets hardware normals enabled/disabled.
     * <p>
     * Hardware normals make mesh segments face the camera. This is the
     * most optimal setting, and is well suited for most trailing effects.
     * <p>
     * Software normals make mesh segments face a set normal direction.
     * This is less efficient than hardware normals, and is best suited for
     * effects such as sword stroke trails. This setting is dependent on a number
     * of mesh properties, such as radius and normal.
     * <p>
     * It is recommended that this be set during initialization only. Otherwise
     * the mesh may suddenly change shape.
     * <p>
     * This setting must agree with the corresponding material parameter
     * ({@link TrailingGeometry} does this automatically when using its setter).
     * <p>
     * default=hardware
     * 
     * @param faceCam 
     */
    public void setFaceCamera(boolean faceCam) {
        faceCamera = faceCam;
    }
    
    /**
     * Sets the radius of the mesh.
     * <p>
     * default=0.3
     * 
     * @param radius radius of mesh (radius > 0)
     */
    public void setRadius(float radius) {
        assert radius > 0;
        this.radius = radius;
    } 
    
    /**
     * Sets the maximum number of points.
     * <p>
     * default=50
     * 
     * @param limit maximum number of points (limit >= 2)
     */
    public void setLimit(int limit) {
        assert limit >= 2;
        this.limit = limit;
    }
    
    /**
     * Clears the mesh of all points.
     */
    public void clearAllPoints() {
        points.clear();
    }
    
    public ArrayList<PointAttributes> getPoints() {
        return points;
    }
    public boolean isFaceCamera() {
        return faceCamera;
    }
    
}
