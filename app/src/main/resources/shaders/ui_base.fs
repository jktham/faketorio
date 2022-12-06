#version 330 core

in vec3 faNormal;
in vec3 faColor;

in vec3 fuColor;
in vec3 fuTint;

out vec4 fragColor;

void main() {
    vec3 color = faColor;
    if (fuColor.x >= 0.0) {
        color = faColor * fuColor;
    }
    if (fuTint.x >= 0.0) {
        color = mix(color, fuTint, 0.5);
    }
    fragColor = vec4(color, 1.0);
}
