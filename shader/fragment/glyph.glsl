#version 330 core
in vec2 texCoords;

out vec4 FragColor;

uniform vec3 color;
uniform sampler2D textureInput;

void main()
{
	vec4 textureSample = vec4(color, texture(textureInput, texCoords).a);

	FragColor = textureSample;
}
