#version 330 core
in vec2 texCoords;

out vec4 FragColor;

uniform vec4 tlColor;
uniform vec4 trColor;
uniform vec4 blColor;
uniform vec4 brColor;
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

	vec4 topColor = mix(tlColor, trColor, texCoords.x);
	vec4 bottomColor = mix(blColor, brColor, texCoords.x);
	FragColor = vec4(mix(topColor, bottomColor, texCoords.y).rgb, alpha);
}
