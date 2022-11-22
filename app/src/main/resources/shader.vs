#version 330 core

in vec3 position;
in vec3 normal;
in vec3 color;

out vec3 vertNormal;
out vec3 vertColor;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main() {
    vertNormal = mat3(transpose(inverse(model))) * normal;
    vertColor = color;
    mat4 mvp = projection * view * model;
    gl_Position = mvp * vec4(position, 1.0);
}
