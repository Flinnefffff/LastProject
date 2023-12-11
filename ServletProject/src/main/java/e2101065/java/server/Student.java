package e2101065.java.server;
public class Student {
	private int student_id;
	private String Name;
	private String username;

	public Student() {
	}

	public Student(int id, String Name, String username) {
		super();
		this.student_id = id;
		this.Name = Name;
		this.username = username;
	}

	public int getStudent_id() {
		return student_id;
	}

	public void setStudent_id(int student_id) {
		this.student_id = student_id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	public String getUsername() {
		return username;
	}

	public void setEmail(String username) {
		this.username = username;
	}

}