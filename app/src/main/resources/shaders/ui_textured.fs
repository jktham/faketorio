#version 330 core

in vec2 faTexCoords;

in vec3 fuColor;
in vec3 fuTint;

uniform sampler2D texture0;

out vec4 fragColor;

void main() {
    vec4 color = texture(texture0, faTexCoords);
    if (fuColor.x >= 0.0) {
        color = color * vec4(fuColor, 1.0);
    }
    if (fuTint.x >= 0.0) {
        color = mix(color, vec4(fuTint, 1.0), 0.5);
    }
    fragColor = color;
}
