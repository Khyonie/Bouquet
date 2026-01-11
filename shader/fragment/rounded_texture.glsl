#version 330 core
in vec2 texCoords;
out vec4 FragColor;

uniform sampler2D textureInput;
uniform vec2 rectSize;
uniform float radius;

void main()
{
	vec2 localPosition = texCoords * rectSize;
	vec2 cornerDistance = min(localPosition, rectSize - localPosition);
	float distance = length(cornerDistance);
	float alpha = 1.0;
	if (cornerDistance.x < radius && cornerDistance.y < radius) 
	{
		float circleDistance = length(cornerDistance - vec2(radius));
		alpha = smoothstep(radius, radius - fwidth(circleDistance), circleDistance);
	}

	vec4 textureSample = texture(textureInput, texCoords);

	FragColor = vec4(textureSample.rgb, textureSample.a * alpha);
}
