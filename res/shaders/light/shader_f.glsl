[Fragment_Shader]
#version 420

uniform sampler2D tex0;
uniform sampler2D tex1;
uniform sampler2D tex2;
uniform sampler2D tex3;
uniform sampler2D tex4;
uniform sampler2D tex5;
uniform sampler2D tex6;
uniform sampler2D tex7;

in float coef;
in vec2 texcoords;
in vec3 normal;

out vec4 out_color;

void main()
{
	//vec3 bump = vec3(texture2D(tex1,texcoords.st))*2 - vec3(1,1,1);
	float light = 0.3 + max(dot(normalize(vec3(1,1,1)), normalize(normal)), 0);
	
	vec4 final = texture2D(tex0,texcoords.st)*coef + texture2D(tex1,texcoords.st)*(1-coef);
	
	
	
   out_color = final * vec4(light, light, light, 1);
}
