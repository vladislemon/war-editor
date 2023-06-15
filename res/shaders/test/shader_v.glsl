[Vertex_Shader]
#version 420

uniform mat4 in_modelview;
uniform mat4 in_projection;

in vec3 in_position;
in vec3 in_normal;
in vec2 in_tex_coord;
in float in_blend;

out vec3 normal;
out vec3 position;
out vec2 texcoords;

void main() {
	texcoords = in_tex_coord;
	normal = in_normal;
	position = in_position;
	gl_Position = vec4(in_position, 1) * in_modelview * in_projection ;
}

