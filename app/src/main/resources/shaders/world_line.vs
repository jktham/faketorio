#version 330 core

layout (location = 0) in vec3 aPosition;
layout (location = 1) in vec3 aColor;

uniform mat4 uModel;
uniform mat4 uView;
uniform mat4 uProjection;

out vec3 faColor;

void main() {
    faColor = aColor;

    mat4 mvp = uProjection * uView * uModel;
    gl_Position = mvp * vec4(aPosition, 1.0);
}
