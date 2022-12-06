#version 330 core

layout (location = 0) in vec3 aPosition;
layout (location = 1) in vec2 aTexCoords;

uniform vec3 uColor;
uniform vec3 uTint;
uniform mat4 uModel;
uniform mat4 uView;
uniform mat4 uProjection;

out vec2 faTexCoords;

out vec3 fuColor;
out vec3 fuTint;

void main() {
    faTexCoords = aTexCoords;

    fuColor = uColor;
    fuTint = uTint;
    mat4 mvp = uProjection * uView * uModel;
    gl_Position = mvp * vec4(aPosition, 1.0);
}
