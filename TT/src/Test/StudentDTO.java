package Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 自定义排序 方法一： model类实现Comparable接口， 重写int compareTo(Object o)方法
 * 
 * @author zhangwenchao
 * 
 */
public class StudentDTO implements Comparable {
	private String name;

	private Integer age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public int compareTo(Object o) {
		StudentDTO sdto = (StudentDTO) o;
		int otherAge = sdto.getAge();
		return this.age.compareTo(otherAge);

	}

	public static void main(String[] args) {
		List<StudentDTO> studentList = new ArrayList<>();

		StudentDTO s = new StudentDTO();

		s.setName("yuanyuan");

		s.setAge(22);

		studentList.add(s);

		StudentDTO s1 = new StudentDTO();

		s1.setName("lily");

		s1.setAge(23);

		studentList.add(s1);

		Collections.sort(studentList);
		for (StudentDTO sd : studentList) {
			System.out.print(sd.getAge() + " ");
		}
		System.out.println();

		Collections.reverse(studentList);
		for (StudentDTO sd : studentList) {
			System.out.print(sd.getAge() + " ");
		}
		System.out.println();
	}
}
