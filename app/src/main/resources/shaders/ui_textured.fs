#version 330 core

in vec3 vertTint;
in vec2 vertUv;

out vec4 fragColor;

uniform sampler2D tex;

void main() {
    
    fragColor = texture(tex, vertUv) * vec4(vertTint, 1.0);
}
