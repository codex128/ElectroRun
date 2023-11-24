
#import "Common/ShaderLib/GLSLCompat.glsllib"

#ifdef TEXTURE
    uniform sampler2D m_Texture;
    #ifdef DISRUPT
        uniform vec3 m_Disrupt;
    #endif
#endif
#ifdef FADE
    uniform vec2 m_Gradient1;
    uniform vec2 m_Gradient2;    
    float progress(in float value, in float a, in float b) {
        return clamp((value-a)/(b-a), 0.0, 1.0);
    }
#endif

uniform float g_Time;

varying vec3 wPosition;
varying vec2 texCoord;
varying vec4 wColor;

void main() {
    
    #ifdef TEXTURE
        vec2 uv = texCoord.xy;
        float value = 1f;
        #ifdef DISRUPT
            float z = m_Disrupt.z - wPosition.z;
            float y = m_Disrupt.y - wPosition.y;
            if (z > 0 && y >= 0) {
                float x = abs(wPosition.x - m_Disrupt.x);
                uv.y += smoothstep(0.2, 0.0, x) * smoothstep(5.0, 0.0, z) * smoothstep(1.0, 0.0, y) * 0.1;
                if (uv.y > 1.0) {
                    uv.y -= 1.0;
                }
                if (x <= 0.2) {
                    value = max(smoothstep(0.0, 0.2, x), max(smoothstep(0.0, 5.0, z), smoothstep(0.0, 1.0, y)));
                }
            }
        #endif
        gl_FragColor = texture2D(m_Texture, uv) * wColor * value;
    #else
        gl_FragColor = wColor;
    #endif
    
    #ifdef FADE
        float m = min(progress(wPosition.z, m_Gradient1.x, m_Gradient1.y), progress(wPosition.z, m_Gradient2.x, m_Gradient2.y));
        gl_FragColor *= m;
    #endif
    
}
