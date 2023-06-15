package src;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.nio.FloatBuffer;

import lib.ShaderLoader;
import lib.Util;



public class Chunk {
	private int VBOId = 0;
	private int VBOBlend = 0;
	public int x = 0;
	public int y = 0;
	
	public Chunk(int cx, int cy) {
		x = cx*64;
		y = cy*64;
	}
	
	public void destroy() {
		glDeleteBuffers(VBOId);
		glDeleteBuffers(VBOBlend);
	}
	
	public void build() {
		VBOId = Util.genGLBufferID();
		VBOBlend = Util.genGLBufferID();
		
		FloatBuffer VBOData = Util.emptyFloatBuffer((18+12+18)*64*64);
		FloatBuffer blendData = Util.emptyFloatBuffer(6*8*64*64);
		
		for(int i=x; i<64+x; i++) {
			for(int k=y; k<64+y; k++) {

				
				float[] tx = new float[]{(x+i)/4F, (x+i+1)/4F};
				float[] ty = new float[]{(y+k+1)/4F, (y+k)/4F};

				float nx=0, ny=0, nz=0;
				float z0=Terrain.instance.get(i, k+1), z1=Terrain.instance.get(i+1, k+1), z2=Terrain.instance.get(i, k), z3=Terrain.instance.get(i+1, k);
				float[] normals = Util.CalculateTriangleNormal(z0,z3,z1);
				float[] normals1 = Util.CalculateTriangleNormal(Terrain.instance.get(i-1, k+2), z0, Terrain.instance.get(i, k+2));
				float[] normals2 = Util.CalculateTriangleNormal(Terrain.instance.get(i-1, k+1), z2, z0);
				float[] normals3 = Util.CalculateTriangleNormal(Terrain.instance.get(i-1, k), Terrain.instance.get(i, k-1), z2);
				float[] normals4 = Util.CalculateTriangleNormal(z2, Terrain.instance.get(i+1, k-1), z3);
				float[] normals5 = Util.CalculateTriangleNormal(z3, Terrain.instance.get(i+2, k-1), Terrain.instance.get(i+2, k));
				float[] normals6 = Util.CalculateTriangleNormal(z1, Terrain.instance.get(i+2, k), Terrain.instance.get(i+2, k+1));
				float[] normals7 = Util.CalculateTriangleNormal(Terrain.instance.get(i+1, k+2), Terrain.instance.get(i+2, k+1), Terrain.instance.get(i+2, k+2));
				float[] normals8 = Util.CalculateTriangleNormal(Terrain.instance.get(i, k+2), z1, Terrain.instance.get(i+1, k+2));
				
			    nx = (normals[0] + normals1[0] + normals2[0] + normals8[0])/4;
			    nx = (normals[0] + normals1[0] + normals2[0] + normals8[0])/4;
			    ny = (normals[1] + normals1[1] + normals2[1] + normals8[1])/4;
			    nz = (normals[2] + normals1[2] + normals2[2] + normals8[2])/4;
			
				VBOData.put(i);
				VBOData.put(k+1);
				VBOData.put(z0);
				VBOData.put(nx);
				VBOData.put(ny);
				VBOData.put(nz);
			    VBOData.put(tx[0]);
				VBOData.put(ty[0]);
				blendData.put(Terrain.instance.getBlend(i, k+1));

				
				
				nx = (normals[0] + normals4[0] + normals5[0] + normals6[0])/4;
			    ny = (normals[1] + normals4[1] + normals5[1] + normals6[1])/4;
			    nz = (normals[2] + normals4[2] + normals5[2] + normals6[2])/4;
				
			    VBOData.put(i+1);
				VBOData.put(k);
				VBOData.put(z3);
				VBOData.put(nx);
				VBOData.put(ny);
				VBOData.put(nz);
				VBOData.put(tx[1]);
				VBOData.put(ty[1]);
				blendData.put(Terrain.instance.getBlend(i+1, k));
				
				
				nx = (normals[0] + normals6[0] + normals7[0] + normals8[0])/4;
			    ny = (normals[1] + normals6[1] + normals7[1] + normals8[1])/4;
			    nz = (normals[2] + normals6[2] + normals7[2] + normals8[2])/4;
				
				VBOData.put(i+1);
				VBOData.put(k+1);
				VBOData.put(z1);
				VBOData.put(nx);
				VBOData.put(ny);
				VBOData.put(nz);
				VBOData.put(tx[1]);
				VBOData.put(ty[0]);
				blendData.put(Terrain.instance.getBlend(i+1, k+1));
				
				
				
				nx = (normals[0] + normals1[0] + normals2[0] + normals8[0])/4;
				ny = (normals[1] + normals1[1] + normals2[1] + normals8[1])/4;
				nz = (normals[2] + normals1[2] + normals2[2] + normals8[2])/4;
				
				VBOData.put(i);
				VBOData.put(k+1);
				VBOData.put(z0);
				VBOData.put(nx);
				VBOData.put(ny);
				VBOData.put(nz);
				VBOData.put(tx[0]);
				VBOData.put(ty[0]);
				blendData.put(Terrain.instance.getBlend(i, k+1));
				

				
				nx = (normals[0] + normals2[0] + normals3[0] + normals4[0])/4;
				ny = (normals[1] + normals2[1] + normals3[1] + normals4[1])/4;
				nz = (normals[2] + normals2[2] + normals3[2] + normals4[2])/4;
				
				VBOData.put(i);
				VBOData.put(k);
				VBOData.put(z2);
				VBOData.put(nx);
				VBOData.put(ny);
				VBOData.put(nz);
				VBOData.put(tx[0]);
				VBOData.put(ty[1]);
				blendData.put(Terrain.instance.getBlend(i, k));

				
				nx = (normals[0] + normals4[0] + normals5[0] + normals6[0])/4;
			    ny = (normals[1] + normals4[1] + normals5[1] + normals6[1])/4;
			    nz = (normals[2] + normals4[2] + normals5[2] + normals6[2])/4;
				
				VBOData.put(i+1);
				VBOData.put(k);
				VBOData.put(z3);
				VBOData.put(nx);
				VBOData.put(ny);
				VBOData.put(nz);
				VBOData.put(tx[1]);
				VBOData.put(ty[1]);
				blendData.put(Terrain.instance.getBlend(i+1, k));

			}
		}
		
		VBOData.flip();
		blendData.flip();
		
		
		Util.writeToVBO(VBOId, VBOData);
		Util.writeToVBO(VBOBlend, blendData);
	}
	
	public void render() {
		glColor4f(1, 1, 1, 1);
		

		glEnableVertexAttribArray(ShaderLoader.getAttributeLocation("in_blend"));
		glEnableVertexAttribArray(ShaderLoader.getAttributeLocation("in_position"));
		glEnableVertexAttribArray(ShaderLoader.getAttributeLocation("in_normal"));
		glEnableVertexAttribArray(ShaderLoader.getAttributeLocation("in_tex_coord"));
		
		
		
		glBindBuffer(GL_ARRAY_BUFFER, VBOId);
		glVertexAttribPointer(ShaderLoader.getAttributeLocation("in_position"), 3, GL_FLOAT, false, 8*4, 0);
		glVertexAttribPointer(ShaderLoader.getAttributeLocation("in_normal"), 3, GL_FLOAT, false, 8*4, 3*4);
		glVertexAttribPointer(ShaderLoader.getAttributeLocation("in_tex_coord"), 2, GL_FLOAT, false, 8*4, 6*4);
	
		glBindBuffer(GL_ARRAY_BUFFER, VBOBlend);
		glVertexAttribPointer(ShaderLoader.getAttributeLocation("in_blend"), 1, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		
		glDrawArrays(GL_TRIANGLES, 0, 64*64*6);
		

		glDisableVertexAttribArray(ShaderLoader.getAttributeLocation("in_blend"));
		glDisableVertexAttribArray(ShaderLoader.getAttributeLocation("in_position"));
		glDisableVertexAttribArray(ShaderLoader.getAttributeLocation("in_normal"));
		glDisableVertexAttribArray(ShaderLoader.getAttributeLocation("in_tex_coord"));

	}
}
