#version 330 core
in vec2 texCoords;

out vec4 FragColor;

uniform vec2 resolution;
uniform sampler2D textureInput;

float random(vec2 st) {
    return fract(sin(dot(st.xy, vec2(12.9898, 78.233))) * 43758.5453123);
}

void main() {
    vec4 color = vec4(0.0);
    float total = 0.0;

    float radius = 5.0; 
    int samples = 16;   

    for (int i = 0; i < samples; ++i) {
        float angle = float(i) / float(samples) * 6.2831853; 
        float rnd = random(u_uv + float(i)); 
        vec2 offset = vec2(cos(angle), sin(angle)) * rnd * radius / resolution;
        color += texture2D(u_texture, u_uv + offset);
        total += 1.0;
    }

    gl_FragColor = color / total;
}
