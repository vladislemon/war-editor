package src;


import lib.ITextureLoader;

import org.lwjgl.opengl.GL11;


public class Terrain {
	private float[][] heightMap;
	private float[][] blend;
	private Chunk[][] chunks; 
	
	
	public float getBlend(int x, int y) {
		if(x<0) {
			x = 0;
		}
		if(x>=heightMap[0].length) {
			x = heightMap[0].length-1;
		}
		if(y<0) {
			y = 0;
		}
		if(y>=heightMap[0].length) {
			y = heightMap[0].length-1;
		}
		return blend[x][y];
	}
	
	public void setBlend(int x, int y, float blend) {
		if(x>=0&&y>=0&&x<heightMap.length&&y<heightMap[0].length) {
			this.blend[x][y] = blend;
			if(this.blend[x][y] > 1) {
				this.blend[x][y] = 1;
			}
			if(this.blend[x][y] < 0) {
				this.blend[x][y] = 0;
			}
		}
	}
	
	public void addBlend(int x, int y, float blend) {
		setBlend(x, y, getBlend(x,y) + blend);
	}
	
	
	
	public void init(float[][] height) {
		heightMap = height;
		buildChunks();
	}
	
	public void empty(int sizeX, int sizeY) {
		heightMap = new float[sizeX][sizeY];
		blend = new float[sizeX][sizeY];
		for(int i=0; i<sizeX; i++) {
			for(int k=0; k<sizeY; k++) {
				heightMap[i][k] = 10;
				blend[i][k] = 0;
			}
		}
		buildChunks();
	}
	
	public void render() {
		
		GL11.glColor3f(1,1,1);
		
		ITextureLoader.bindTexture(0, "res/textures/terrain/grass_nice.png");
		ITextureLoader.bindTexture(1, "res/textures/terrain/sand_nice.png");

		
		for(int x=0; x<chunks.length; x++) {   
			for(int y=0; y<chunks[0].length; y++) {
					chunks[x][y].render();
			}
		}

	}
	
	public Chunk[][] getChunks() {
		return chunks;
	}
	
	public float[][] getHeightMap() {
		return heightMap;
	}
	
	private void buildChunks() {
		chunks = new Chunk[(int) Math.ceil(heightMap.length/64)][(int) Math.ceil(heightMap[0].length/64)];
		
		for(int x=0; x<chunks.length; x++) {
			for(int y=0; y<chunks[0].length; y++) {
				chunks[x][y] = new Chunk(x, y);
				chunks[x][y].build();
			}
		}
	}
	
	public void destroy() {
		for(int i=0; i<chunks.length; i++) {
			for(int k=0; k<chunks[0].length; k++) {
				chunks[i][k].destroy();
			}
		}
	}
	
	public int getTexture(int id) {
		return id == 0 ? 1:2;
	}
	
	

	
	public float get(int x, int y) {
		if(x<0) {
			x = 0;
		}
		if(x>=heightMap[0].length) {
			x = heightMap[0].length-1;
		}
		if(y<0) {
			y = 0;
		}
		if(y>=heightMap[0].length) {
			y = heightMap[0].length-1;
		}
		return heightMap[x][y];
	}
	
	public void set(int x, int y, float z) {
		if(x>=0&&y>=0&&x<heightMap.length&&y<heightMap[0].length) {
			heightMap[x][y] = z;
		}
	}
	
	public void add(int x, int y, float dif) {
		if(get(x, y) + dif >= 0) {
			set(x, y, get(x, y) + dif);
		} else {
			set(x, y, 0);
		}
	}
	
	public float getInterpolated(float x, float y) {
		if(x<0) {
			x = 0;
		}
		if(x>=heightMap[0].length) {
			x = heightMap[0].length-1;
		}
		if(y<0) {
			y = 0;
		}
		if(y>=heightMap[0].length) {
			y = heightMap[0].length-1;
		}
		
		float kx = x - (int)x;
		float ky = y - (int)y;
		float h1 = get((int)x,   (int)y);
		float h2 = get((int)x,   (int)y+1);
		float h3 = get((int)x+1, (int)y+1);
		float h4 = get((int)x+1, (int)y);
		
		float i1 = h2*kx + h3*(1F-kx);
		float i2 = h1*kx + h4*(1F-kx);
		return i1*ky + i2*(1F-ky);
	}
	
	
	public int getWidth() {
		return heightMap.length;
	}
	
	public int getHeight() {
		return heightMap[0].length;
	}
	
	public static Terrain instance = null;
}
