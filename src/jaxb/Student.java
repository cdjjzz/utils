package jaxb;
import java.util.List;  
public class Student {  
    private String name;  
    private String number;  
    private String gender;  
    private String age;  
    private List<Course> courses;  
  
    public static class Course {  
        private String name;  
        private String grade;  
        private String remark;        
                // Getter and Setter Methods ...  
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getGrade() {
			return grade;
		}
		public void setGrade(String grade) {
			this.grade = grade;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
        
    }         
        // Getter and Setter Methods ...

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}
    
}  