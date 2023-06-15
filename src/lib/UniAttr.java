package lib;

public class UniAttr {
	
	private int id, program;
	private String name;
	
	public UniAttr(int id, int program, String name) {
		this.id = id;
		this.program = program;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	
	public int getProgram() {
		return program;
	}
	
	public String getName() {
		return name;
	}
}
