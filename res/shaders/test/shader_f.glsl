[Fragment_Shader]
#version 420

uniform sampler2D tex0;
uniform sampler2D tex1;



in vec2 texcoords;
in vec3 normal;
in vec3 position;

out vec4 out_color;

void main()
{
	vec4 frag = texture2D(tex0,texcoords.st);
	vec3 light_pos = vec3(50, 50, 30);
	vec3 diffuse = vec3(0.1, 0.1, 0.1);
	vec3 lightcolor = vec3(0.8, 1, 0);
	float lightpower = 1000;
	
	
	vec3 bump = vec3(texture2D(tex1,texcoords.st))*2 - vec3(1,1,1);
	float lightForce = max(lightpower - length(light_pos - position), 0)/lightpower;
	float perpendicularness = max(dot(normalize(light_pos - position), normalize(normal+bump)), 0);
	
	
	
	out_color = vec4(frag.rgb * (lightcolor.rgb * lightForce * perpendicularness + diffuse.rgb), 1);

}
