#version 330 core

in vec3 vertTint;
in vec3 vertNormal;
in vec3 vertColor;

out vec4 fragColor;

void main() {
    vec3 normal = normalize(vertNormal);
    vec3 lightDir = normalize(vec3(-1.0, -2.0, 3.0));
    vec3 lightColor = vec3(1.0, 1.0, 1.0);

    vec3 ambient = lightColor * 0.4;
    vec3 diffuse = lightColor * max(dot(normal, lightDir), 0.0) * 0.5;

    vec3 baseColor = vec3(1.0, 1.0, 1.0);
    if (vertTint.x > -1.0) {
        baseColor = vertTint;
    } else {
        baseColor = vertColor;
    }
    vec3 color = baseColor * (ambient + diffuse);
    fragColor = vec4(color, 1.0);
}
