package lib;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Util {
	public static int genGLBufferID() {
		return glGenBuffers();
	}
	
	public static void writeToVBO(int VBOHandle, FloatBuffer data) {
		glBindBuffer(GL_ARRAY_BUFFER,VBOHandle);
		glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	
	
	public static FloatBuffer emptyFloatBuffer(int size) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(size);
		return buffer;
	}
	
	public static FloatBuffer toFloatBuffer(float[] f) {
		FloatBuffer buffer = emptyFloatBuffer(f.length);
		buffer.put(f);
		buffer.flip();
		return buffer;
	}
	
	public static float[] CalculateTriangleNormal(float z1, float z2, float z3) {
		float ax, ay, az, bx, by, bz;
		ax = 0;
		ay = -1;
		az = z2 - z1;
		bx = 1;
		by = 0;
		bz = z3 - z1;
		
		float nx, ny, nz;
		nx = ay*bz - az*by;
		ny = az*bx - ax*bz;
		nz = ax*by - ay*bx;
		
		return new float[]{nx, ny, nz};
	}
	
	public static float sin(float angle) {
		return (float)Math.sin(Math.toRadians(angle));
	}
	public static float cos(float angle) {
		return (float)Math.cos(Math.toRadians(angle));
	}
	public static float tan(float angle) {
		return (float)Math.tan(Math.toRadians(angle));
	}
	
	public static float arcsin(float sin) {
		return (float)Math.toDegrees(Math.asin(sin));
	}
	public static float arccos(float cos) {
		return (float)Math.toDegrees(Math.acos(cos));
	}
	public static float arctan(float tan) {
		return (float)Math.toDegrees(Math.atan(tan));
	}
	public static float arctan2(float f1, float f2) {
		return (float)Math.toDegrees(Math.atan2(f1, f2));
	}
	public static float distance(float x1, float y1, float x2, float y2) {
		return (float) Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
	}
	public static float abs(float f1) {
		if(f1>0) {
			return f1;
		} else {
			return -f1;
		}
	}
	public static float sign(float a) {
		return a>0?1:a<0?-1:0;
	}
	
	public static float vectorLength(float x, float y) {
		return (float) Math.sqrt(x*x+y*y);
	}
	public static float average(float[] floats) {
		float total = 0;
		for(int i=0; i<floats.length; i++) {
			total += floats[i];
		}
		return total/floats.length;
	}
	
	
	public static String pow(String s, int pow) {
		String ret="";
		
		for(int i = 0; i < pow; i++) {
			ret += s;
		}
		
		return ret;
	}
	
	public static String getWord(String s, int num) {
		char[] ch = s.toCharArray();
		char[] temp;
		int i1 = 0, i2 = 0, k = 0;
		for(int n = 0; n < ch.length; n++) {
			if(ch[n] != ' ' && ch[n] != '	') {
				if(n == 0 || (n > 0 && ch[n-1] == ' ' || ch[n-1] == '	')) {
					k++;
					if(k == num) {
						i1 = n;
						break;
					}
				}
			}
		}
		if(k < num)
			return "";
		for(int n = i1; n < ch.length; n++) {
			if(ch[n] == ' ' || ch[n] == '	') {
				i2 = n;
				break;
			}
		}
		if(i2 == 0)
			i2 = ch.length;
		temp = new char[i2-i1];
		for(int n = 0; n < i2-i1; n++) {
			temp[n] = ch[n + i1];
		}
		return String.copyValueOf(temp);
	}
}
