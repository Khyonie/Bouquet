#version 330 core
in vec2 texCoords;
out vec4 FragColor;

uniform vec4 color;
uniform vec3 borderColor;
uniform float borderSize;
uniform vec2 rectSize;
uniform float radius;

void main()
{
	vec2 localPosition = texCoords * rectSize;
	vec2 cornerDistance = min(localPosition, rectSize - localPosition);

	vec3 finalColor = color.rgb;
	if (min(localPosition.x, rectSize.x - localPosition.x) < borderSize || min(localPosition.y, rectSize.y - localPosition.y) < borderSize)
	{
		finalColor = borderColor;
	}

	float distance = length(cornerDistance);

	float alpha = 1.0;
	if (cornerDistance.x < radius && cornerDistance.y < radius) 
	{
		float circleDistance = length(cornerDistance - vec2(radius));
		float edgeWidth = fwidth(circleDistance);

		alpha = smoothstep(radius + 0.5 * edgeWidth, radius - 0.5 * edgeWidth, circleDistance);

		float innerRadius = radius - borderSize;
		float borderMask = smoothstep(innerRadius - 0.5 * edgeWidth, innerRadius + 0.5 * edgeWidth, circleDistance);

		finalColor = mix(color.rgb, borderColor, borderMask);
		//float circleDistance = length(cornerDistance - vec2(radius));

		//alpha = smoothstep(radius, radius - fwidth(circleDistance), circleDistance);
		//if (circleDistance >= radius - borderSize)
		//{
			//finalColor = borderColor;
		//}
	}

	FragColor = vec4(finalColor, color.a * alpha);
}
