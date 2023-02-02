#version 330 core

in vec3 faColor;

out vec4 fragColor;

void main() {
    fragColor = vec4(faColor, 1.0);
}
