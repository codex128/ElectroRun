
#import "Common/ShaderLib/GLSLCompat.glsllib"
#import "Common/ShaderLib/Instancing.glsllib"

#ifdef USE_SMOOTHING
    uniform vec3 m_TrailOrigin;
#endif

attribute vec3 inPosition;
attribute vec2 inTexCoord;
attribute vec2 inTexCoord2;

uniform vec3 g_CameraPosition;
uniform bool m_FaceCamera;
attribute vec4 inTexCoord3;

varying vec3 wPosition;
varying vec2 texCoord;
varying float fadeCoord;
varying float leadingPair;

void main() {
    
    texCoord = inTexCoord;
    fadeCoord = inTexCoord2.x;
    leadingPair = inTexCoord2.y;
    
    if (m_FaceCamera) {
        #ifdef USE_SMOOTHING
            vec3 originPos;
            if (leadingPair > 0.5) {
                originPos = m_TrailOrigin;
            } else {
                originPos = inPosition;
            }
        #else
            vec3 originPos = inPosition;
        #endif
        vec3 outward = cross(normalize(inTexCoord3.xyz), normalize(g_CameraPosition - originPos)) * inTexCoord3.w;
        wPosition = originPos + outward;
        gl_Position = g_WorldViewProjectionMatrix * vec4(wPosition, 1.0);
    }
    else {
        vec4 modelSpacePos = vec4(inPosition, 1.0);    
        wPosition = TransformWorld(modelSpacePos).xyz;
        gl_Position = TransformWorldViewProjection(modelSpacePos);
    }
    
}
