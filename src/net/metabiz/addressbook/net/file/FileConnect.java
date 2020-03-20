package net.metabiz.addressbook.net.file;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class FileConnect {

	public Workbook readFile(String path) {
		System.out.println(" readFile...Start    " + path);

		Workbook wb = null;

		// 엑셀파일
		File file = new File(path);

		// 엑셀 파일 오픈
		try {

			wb = WorkbookFactory.create(file);
		} catch (Exception e) {


			if (e.toString().contains("FileNotFoundException")) {

				System.err.println("D:\\test2.xlsx (다른 프로세스가 파일을 사용 중이기 때문에 프로세스가 액세스 할 수 없습니다");
			}
		}
		System.out.println(" readFile...End   ");
		return wb;

	}

	public boolean wirteFile(String path, Workbook workbook) {
		System.out.println(" wirteFile...Start    " + path);
		boolean success = false;
		try {

			FileOutputStream fileoutputstream = new FileOutputStream(path);
			workbook.write(fileoutputstream);

			fileoutputstream.close();
			success = true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(" wirteFile...End   " + success);
		return success;
	}

	/**
	 * 파일삭제
	 * 
	 * @param path
	 * @return boolean
	 */
	public boolean deleteFile(String path) {
		System.out.println("deleteFile...Start    " + path);

		boolean success = false;

		File file = new File(path);
		if (file.exists()) {
			file.delete();

			success = true;

		}
		System.out.println(" deleteFile...End   " + success);
		return success;
	}

	/**
	 * 파일이름 변경
	 * 
	 * @param name
	 * @param newName
	 * @return boolean
	 */
	public boolean updateFileName(String name, String newName) {
		System.out.println("updateFileName...Start /" + name + " / " + newName);

		boolean success = false;

		File file = new File(name);
		File newFile = new File(newName);

		if (file.exists()) {
			file.renameTo(newFile);
			success = true;
		}
		System.out.println(" updateFileName...End   " + success);
		return success;

	}

}
