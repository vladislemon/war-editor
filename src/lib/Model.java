package lib;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_NORMAL_ARRAY;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_COORD_ARRAY;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glNormalPointer;
import static org.lwjgl.opengl.GL11.glTexCoordPointer;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

public class Model {
	private int vboVertex = 0;
	private int vboNormal = 0;
	private int vboTexCoords = 0;
	private int sizeX = 0;
	private int sizeY = 0;
	private int sizeZ = 0;
	private int polyCount = 0;
	private int vertexCount = 0;
	
	
	public Model(int vboVertex, int vboNormal, int vboTexCoords, int sizeX, int sizeY, int sizeZ, int polyCount, int vertexCount) {
		this.vboVertex = vboVertex;
		this.vboNormal = vboNormal;
		this.vboTexCoords = vboTexCoords;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.sizeZ = sizeZ;
		this.polyCount = polyCount;
		this.vertexCount = vertexCount;
	}
	
	public void draw() {
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		glEnableClientState(GL_NORMAL_ARRAY);
		
		glBindBuffer(GL_ARRAY_BUFFER, vboTexCoords);
		glTexCoordPointer(2, GL_FLOAT, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, vboNormal);
		glNormalPointer(GL_FLOAT, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, vboVertex);
		glVertexPointer(3, GL_FLOAT, 0, 0);
		
		glDrawArrays(GL_TRIANGLES, 0, vertexCount);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		glDisableClientState(GL_NORMAL_ARRAY);
		glDisableClientState(GL_TEXTURE_COORD_ARRAY);
		glDisableClientState(GL_VERTEX_ARRAY);
	}
	
	
	public int getSizeX() {
		return sizeX;
	}
	public int getSizeY() {
		return sizeY;
	}
	public int getSizeZ() {
		return sizeZ;
	}
	public int getPolyCount() {
		return polyCount;
	}
	public int getVertexCount() {
		return vertexCount;
	}
	
	
	
}
