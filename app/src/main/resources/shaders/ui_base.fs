#version 330 core

in vec3 vertTint;
in vec3 vertNormal;
in vec3 vertColor;

out vec4 fragColor;

void main() {
    vec3 color = vec3(1.0, 1.0, 1.0);
    if (vertTint.x >= 0.0) {
        color = vertTint;
    } else {
        color = vertColor;
    }
    fragColor = vec4(color, 1.0);
}
