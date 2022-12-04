#version 330 core

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 uv;

out vec3 vertTint;
out vec2 vertUv;

uniform vec3 tint;
uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main() {
    vertTint = tint;
    vertUv = uv;
    mat4 mvp = projection * view * model;
    gl_Position = mvp * vec4(position.xy, 0.0, 1.0);
}
