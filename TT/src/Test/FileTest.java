package Test;

import java.io.File;
import java.io.IOException;

public class FileTest {

	public static void main(String[] args) {
		File file = new File("C:\\Users\\Administrator\\Desktop\\status2.jpg");
		System.out.println(file.toPath());
		System.out.println(file.getName());
		System.out.println(file.getTotalSpace());
		File files = new File("C:\\Users\\Administrator\\Desktop");
		System.out.println(files.list().length);
		File file2 = new File("C:/Users/Administrator/Desktop/status2.j");
		System.out.println(file2.toPath());
		File file3 = new File("D:/work/Test/ad.txt");
		System.out.println(file3.toPath());
//		if (!file3.exists()) {
//			try {
//				file3.createNewFile();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		System.out.println(file3.toPath());
		File file4 = new File("D:/aaa/a.txt");
		if (!file4.exists()){
			try {
				file4.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("file4.exists ----> " + file4.exists());
	}
	
	
}
