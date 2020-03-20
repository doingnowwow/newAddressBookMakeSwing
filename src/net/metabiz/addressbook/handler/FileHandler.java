package net.metabiz.addressbook.handler;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import net.metabiz.addressbook.data.DataConstants;
import net.metabiz.addressbook.data.GroupData;
import net.metabiz.addressbook.data.UserData;
import net.metabiz.addressbook.data.tableData.UserTableData;
import net.metabiz.addressbook.net.file.FileConnect;
import net.metabiz.addressbook.util.AbUtil;

public class FileHandler extends AbHandler {

	private static FileHandler instance;
	private FileConnect fileconnect;
	private Workbook workbook;
	private String GROUP_SHEET_NAME = "그룹";
	private String USER_SHEET_NAME = "사용자";

	public FileHandler() {
		this.fileconnect = new FileConnect();
	}

	public static FileHandler getInstance() {

		if (instance == null) {
			instance = new FileHandler();
		}
		return instance;
	}

	@Override
	public void closeProgram() {

		ArrayList<GroupData> groupList = this.readGroup();
		ArrayList<UserData> userList = this.readUser();

		DBHandler.getInstance().syncUserData(userList);
		DBHandler.getInstance().syncGroupData(groupList);

		this.fileconnect.deleteFile(DataConstants.FILE_PATH_TEMP);
	}

	private Workbook newworkbook;

	@Override
	public boolean syncUserData(ArrayList<UserData> userDataList) {

		fileconnect.deleteFile(DataConstants.FILE_PATH);
		newworkbook = new XSSFWorkbook();
		Sheet sheet = newworkbook.createSheet(USER_SHEET_NAME);
		// 번호 이름 핸드폰번호 이메일 회사 부서 직책 그룹 메모
		Row rowHeader = sheet.createRow(0);
		rowHeader.createCell(0).setCellValue("번호");
		rowHeader.createCell(1).setCellValue("이름");
		rowHeader.createCell(2).setCellValue("핸드폰번호");
		rowHeader.createCell(3).setCellValue("이메일");
		rowHeader.createCell(4).setCellValue("회사");
		rowHeader.createCell(5).setCellValue("부서");
		rowHeader.createCell(6).setCellValue("직책");
		rowHeader.createCell(7).setCellValue("그룹");
		rowHeader.createCell(8).setCellValue("메모");

		for (UserData userData : userDataList) {

			Row row = sheet.createRow(sheet.getLastRowNum() + 1);

			Cell cell_0 = row.createCell(0);
			cell_0.setCellValue(userData.getAd_no() + "");

			Cell cell_1 = row.createCell(1);
			cell_1.setCellValue(userData.getAd_name());

			Cell cell_2 = row.createCell(2);
			if (userData.getAd_hp() != null)
				cell_2.setCellValue(userData.getAd_hp());

			Cell cell_3 = row.createCell(3);
			if (userData.getAd_mail() != null)
				cell_3.setCellValue(userData.getAd_mail());

			Cell cell_4 = row.createCell(4);
			if (userData.getAd_com() != null)
				cell_4.setCellValue(userData.getAd_com());

			Cell cell_5 = row.createCell(5);
			if (userData.getAd_department() != null)
				cell_5.setCellValue(userData.getAd_department());

			Cell cell_6 = row.createCell(6);
			if (userData.getAd_position() != null)
				cell_6.setCellValue(userData.getAd_position());

			Cell cell_7 = row.createCell(7);
			if (userData.getGroup_no() != null)
				cell_7.setCellValue(userData.getGroup_no());

			Cell cell_8 = row.createCell(8);
			if (userData.getAd_memo() != null)
				cell_8.setCellValue(userData.getAd_memo());
		}

		fileconnect.wirteFile(DataConstants.FILE_PATH, newworkbook);
		return true;
	}

	@Override
	public boolean syncGroupData(ArrayList<GroupData> groupDataList) {

		Sheet sheet = newworkbook.createSheet(GROUP_SHEET_NAME);
		Row rowHeader = sheet.createRow(0);
		rowHeader.createCell(0).setCellValue("그룹번호");
		rowHeader.createCell(1).setCellValue("그룹이름");

		for (GroupData groupData : groupDataList) {
			Row row = sheet.createRow(sheet.getLastRowNum() + 1);
			Cell cell_0 = row.createCell(0);
			cell_0.setCellValue(groupData.getGroup_no() + "");
			Cell cell_1 = row.createCell(1);
			cell_1.setCellValue(groupData.getGroup_name());
		}
		fileconnect.wirteFile(DataConstants.FILE_PATH, newworkbook);

		return true;
	}

	public boolean insertUser(UserData userData) {
		boolean success = false;

		this.workbook = fileconnect.readFile(DataConstants.FILE_PATH);
		Sheet sheet = workbook.getSheet(USER_SHEET_NAME);

		Row row = sheet.createRow(sheet.getLastRowNum() + 1);

		// 번호 이름 핸드폰번호 이메일 회사 부서 직책 그룹 메모

		Cell cell_0 = row.createCell(0);
		cell_0.setCellValue(userData.getAd_no() + "");

		Cell cell_1 = row.createCell(1);
		cell_1.setCellValue(userData.getAd_name());

		Cell cell_2 = row.createCell(2);
		if (userData.getAd_hp() != null)
			cell_2.setCellValue(userData.getAd_hp());

		Cell cell_3 = row.createCell(3);
		if (userData.getAd_mail() != null)
			cell_3.setCellValue(userData.getAd_mail());

		Cell cell_4 = row.createCell(4);
		if (userData.getAd_com() != null)
			cell_4.setCellValue(userData.getAd_com());

		Cell cell_5 = row.createCell(5);
		if (userData.getAd_department() != null)
			cell_5.setCellValue(userData.getAd_department());

		Cell cell_6 = row.createCell(6);
		if (userData.getAd_position() != null)
			cell_6.setCellValue(userData.getAd_position());

		Cell cell_7 = row.createCell(7);
		if (userData.getGroup_no() != null)
			cell_7.setCellValue(userData.getGroup_no());

		Cell cell_8 = row.createCell(8);
		if (userData.getAd_memo() != null)
			cell_8.setCellValue(userData.getAd_memo());

		success = fileconnect.wirteFile(DataConstants.FILE_PATH_TEMP, workbook);

		if (success) {
			try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			success = fileconnect.updateFileName(DataConstants.FILE_PATH_TEMP, DataConstants.FILE_PATH);
		}
		return success;

	}

	/**
	 * 사용자 수정
	 * 
	 * @param userDataList
	 * @return boolean
	 */
	public boolean updateUser(ArrayList<UserData> userDataList) {

		boolean success = false;

		this.workbook = fileconnect.readFile(DataConstants.FILE_PATH);
		Sheet sheet = workbook.getSheet(USER_SHEET_NAME);

		for (UserData userData : userDataList) {

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {

				Row row = sheet.getRow(i);

				row.getCell(0).setCellType(row.getCell(0).CELL_TYPE_STRING);

				String fileUserNum = row.getCell(0).getStringCellValue();
				String userNo = userData.getAd_no() + "";

				if (userNo.equals(fileUserNum)) {
					if (userData.getAd_name() != null) {
						row.getCell(1).setCellValue(userData.getAd_name());
					}
					if (userData.getAd_hp() != null) {
						row.getCell(2).setCellValue(userData.getAd_hp());
					}
					if (userData.getAd_mail() != null) {
						row.getCell(3).setCellValue(userData.getAd_mail());
					}
					if (userData.getAd_com() != null) {
						row.getCell(4).setCellValue(userData.getAd_com());
					}
					if (userData.getAd_department() != null) {
						row.getCell(5).setCellValue(userData.getAd_department());
					}
					if (userData.getAd_position() != null) {
						row.getCell(6).setCellValue(userData.getAd_position());
					}
					if (userData.getGroup_no() != null) {
						row.getCell(7).setCellValue(userData.getGroup_no());
					}
					if (userData.getAd_memo() != null) {
						row.getCell(8).setCellValue(userData.getAd_memo());
					}
				}

			}
		}

		success = fileconnect.wirteFile(DataConstants.FILE_PATH_TEMP, workbook);

		if (success) {
			try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			success = fileconnect.updateFileName(DataConstants.FILE_PATH_TEMP, DataConstants.FILE_PATH);
		}

		return success;

	}

	public boolean deleteUser(ArrayList<UserTableData> deleteUserList) {

		boolean success = false;

		this.workbook = fileconnect.readFile(DataConstants.FILE_PATH);
		Sheet sheet = workbook.getSheet(USER_SHEET_NAME);

		for (UserData userData : deleteUserList) {

			for (int i = 1; i <= sheet.getLastRowNum(); i++) {

				Row row = sheet.getRow(i);

				row.getCell(0).setCellType(row.getCell(0).CELL_TYPE_STRING);
				String cellValue = row.getCell(0).getStringCellValue();

				String userNum = userData.getAd_no() + "";

				if (userNum.equals(cellValue)) {
					sheet.removeRow(row);
					if (i >= 0 && i < sheet.getLastRowNum()) {
						sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);
					}
					break;
				}
			}
		}

		success = fileconnect.wirteFile(DataConstants.FILE_PATH_TEMP, workbook);

		if (success) {
			try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			success = fileconnect.updateFileName(DataConstants.FILE_PATH_TEMP, DataConstants.FILE_PATH);

		}

		return success;
	}

	public ArrayList<UserData> selectUserListBySearch(String code, String keyword) {
		ArrayList<UserData> totalList = this.readUser();

		ArrayList<UserData> resultList = new ArrayList<UserData>();

		for (UserData userData : totalList) {

			if (code == null) {
				if (userData.getAd_name().contains(keyword)) {
					resultList.add(userData);
					continue;
				}
				if (userData.getAd_hp().contains(keyword)) {
					resultList.add(userData);
					continue;
				}
				if (userData.getAd_mail().contains(keyword)) {
					resultList.add(userData);
					continue;
				}
				if (userData.getAd_com().contains(keyword)) {
					resultList.add(userData);
					continue;
				}
				if (userData.getAd_department().contains(keyword)) {
					resultList.add(userData);
					continue;
				}
				if (userData.getAd_position().contains(keyword)) {
					resultList.add(userData);
					continue;
				}
				if (AbUtil.getGroupName(userData.getGroup_no(), true, AbUtil.recodeGroupList).contains(keyword)) {
					resultList.add(userData);
					continue;
				}
				if (userData.getAd_memo().contains(keyword)) {
					resultList.add(userData);
					continue;
				}
			} else if (code.equals("hp")) {
				if (userData.getAd_hp().contains(keyword)) {
					resultList.add(userData);
					continue;
				}
			} else if (code.equals("mail")) {
				if (userData.getAd_mail().contains(keyword)) {
					resultList.add(userData);
					continue;
				}
			}
		}

		return resultList;
	}

	/**
	 * 그룹 별 사용자 리스트
	 * 
	 * @param groupData
	 * @return
	 */
	public ArrayList<UserData> selectUserListByGroup(GroupData groupData) {
		ArrayList<UserData> totalList = this.readUser();
		ArrayList<UserData> userList = new ArrayList<UserData>();

		if (groupData == null) {
			userList = totalList;
		} else {
			String groupNo = groupData.getGroup_no() + "";

			for (int i = 0; i < totalList.size(); i++) {
				UserData user = totalList.get(i);

				if (user.getGroup_no() != null) {

					if (user.getGroup_no().contains(",")) {
						String[] userGroup = user.getGroup_no().split(",");
						for (int j = 0; j < userGroup.length; j++) {
							if (userGroup[j].equals(groupNo)) {
								userList.add(user);
							}
						}

					} else {
						if (user.getGroup_no().equals(groupNo)) {
							userList.add(user);
						}
					}
				}

			}
		}

		return userList;
	}

	/**
	 * 그룹 목록
	 * 
	 * @return ArrayList<GroupData>
	 */
	public ArrayList<GroupData> selectGroup() {
		return this.readGroup();
	}

	/**
	 * 그룹 추가
	 * 
	 * @param groupData
	 * @return
	 */
	public boolean insertGroup(GroupData groupData) {

		boolean success = false;

		this.workbook = fileconnect.readFile(DataConstants.FILE_PATH);
		Sheet sheet = workbook.getSheet(GROUP_SHEET_NAME);
		Row row = sheet.createRow(sheet.getLastRowNum() + 1);
		Cell cell_0 = row.createCell(0);
		cell_0.setCellValue(groupData.getGroup_no() + "");
		Cell cell_1 = row.createCell(1);
		cell_1.setCellValue(groupData.getGroup_name());

		success = fileconnect.wirteFile(DataConstants.FILE_PATH_TEMP, workbook);

		if (success) {
			try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			success = fileconnect.updateFileName(DataConstants.FILE_PATH_TEMP, DataConstants.FILE_PATH);
		}
		return success;

	}

	/**
	 * 그룹명 변경
	 * 
	 * @param groupData
	 * @return boolean
	 */
	public boolean updateGroup(GroupData groupData) {

		boolean success = false;

		this.workbook = fileconnect.readFile(DataConstants.FILE_PATH);
		Sheet sheet = workbook.getSheet(GROUP_SHEET_NAME);

		for (int i = 1; i <= sheet.getLastRowNum(); i++) {

			Row row = sheet.getRow(i);

			row.getCell(0).setCellType(row.getCell(0).CELL_TYPE_STRING);
			String cellValue = row.getCell(0).getStringCellValue();
			String groupNum = groupData.getGroup_no() + "";

			if (groupNum.equals(cellValue)) {
				row.getCell(1).setCellValue(groupData.getGroup_name());
			}
		}

		success = fileconnect.wirteFile(DataConstants.FILE_PATH_TEMP, workbook);

		if (success) {
			try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			success = fileconnect.updateFileName(DataConstants.FILE_PATH_TEMP, DataConstants.FILE_PATH);

		}

		return success;

	}

	/**
	 * group 삭제 group 번호를 찾아서 삭제함.
	 * 
	 * @param groupData
	 * @return boolean
	 */
	public boolean deleteGroup(GroupData groupData) {
		boolean success = false;

		this.workbook = fileconnect.readFile(DataConstants.FILE_PATH);
		Sheet sheet = workbook.getSheet(GROUP_SHEET_NAME);

		for (int i = 1; i <= sheet.getLastRowNum(); i++) {

			Row row = sheet.getRow(i);

			row.getCell(0).setCellType(row.getCell(0).CELL_TYPE_STRING);
			String cellValue = row.getCell(0).getStringCellValue();
			String groupNum = groupData.getGroup_no() + "";

			if (groupNum.equals(cellValue)) {
				sheet.removeRow(row);
				if (i >= 0 && i < sheet.getLastRowNum()) {
					sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);
				}
				break;
			}
		}

		success = fileconnect.wirteFile(DataConstants.FILE_PATH_TEMP, workbook);

		if (success) {
			try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			success = fileconnect.updateFileName(DataConstants.FILE_PATH_TEMP, DataConstants.FILE_PATH);

		}
		// 사용자 리스트 갱신
		success = this.updateUser(this.updateUserDataByGroup(groupData));

		return success;

	}

	/**
	 * 그룹삭제 시 해당 그룹을 가지고있는 사용자에서 그룹을 빼줌
	 * 
	 * @param groupData
	 * @return ArrayList<UserData>
	 */
	private ArrayList<UserData> updateUserDataByGroup(GroupData groupData) {

		ArrayList<UserData> allUserList = this.selectUserListByGroup(null);

		for (int i = 0; i < allUserList.size(); i++) {

			String groupNum = groupData.getGroup_no() + "";

			// 그룹이 없는 사람들
			if (allUserList.get(i).getGroup_no() != null) {

				if (!allUserList.get(i).getGroup_no().equals("")) {

					// 그룹이 여러개인사람들
					if (allUserList.get(i).getGroup_no().contains(",")) {

						String[] userGroupl = allUserList.get(i).getGroup_no().split(",");

						for (int j = 0; j < userGroupl.length; j++) {
							if (userGroupl[j].equals(groupNum)) {
								userGroupl[j] = "*";
							}
						}
						String newUserGroup = "";

						for (int j = 0; j < userGroupl.length; j++) {
							if (!userGroupl[j].equals("*")) {
								newUserGroup = newUserGroup + userGroupl[j] + ",";
							}
						}
						newUserGroup = newUserGroup.substring(0, newUserGroup.length() - 1);
						allUserList.get(i).setGroup_no(newUserGroup);

					} else {
						// 그룹이 1개인 사람들
						if (allUserList.get(i).getGroup_no().equals(groupNum)) {
							String userGroup = allUserList.get(i).getGroup_no().replace(groupNum, "");
							allUserList.get(i).setGroup_no(userGroup);
						}
					}
				}
			}
		}

		return allUserList;

	}

	/**
	 * 사용자 전체 목록불러오기
	 * 
	 * @return
	 */
	private ArrayList<UserData> readUser() {

		ArrayList<UserData> userList = new ArrayList<UserData>();

		this.workbook = fileconnect.readFile(DataConstants.FILE_PATH);

		if (workbook != null) {

			// 첫번재 sheet 내용 읽기
			for (Row row : workbook.getSheet(USER_SHEET_NAME)) {
				// 2번째 줄부터 읽을꺼임
				if (row.getRowNum() < 1) {
					continue;
				}

				// 콘솔 출력
				UserData user = new UserData();
				// 번호
				if (row.getCell(0) != null) {
					row.getCell(0).setCellType(row.getCell(0).CELL_TYPE_STRING);
					user.setAd_no(Integer.parseInt(row.getCell(0).getStringCellValue()));
				}
				// 이름
				if (row.getCell(1) != null) {
					user.setAd_name(row.getCell(1).getStringCellValue());
				}
				// 핸드폰번호
				if (row.getCell(2) != null) {
					user.setAd_hp(row.getCell(2).getStringCellValue());
				}
				// 이메일
				if (row.getCell(3) != null) {
					user.setAd_mail(row.getCell(3).getStringCellValue());
				}
				// 회사
				if (row.getCell(4) != null) {
					user.setAd_com(row.getCell(4).getStringCellValue());
				}
				// 부서
				if (row.getCell(5) != null) {
					user.setAd_department(row.getCell(5).getStringCellValue());
				}
				// 직책
				if (row.getCell(6) != null) {
					user.setAd_position(row.getCell(6).getStringCellValue());
				}
				// 그룹
				if (row.getCell(7) != null) {
					row.getCell(7).setCellType(row.getCell(7).CELL_TYPE_STRING);
					user.setGroup_no(row.getCell(7).getStringCellValue());
				}
				// 메모
				if (row.getCell(8) != null) {
					user.setAd_memo(row.getCell(8).getStringCellValue());
				}

				userList.add(user);

			}
			try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return userList;

	}

	/**
	 * 그룹 전체 목록 불러오기
	 * 
	 * @return
	 */
	public ArrayList<GroupData> readGroup() {

		ArrayList<GroupData> groupList = new ArrayList<GroupData>();

		this.workbook = fileconnect.readFile(DataConstants.FILE_PATH);

		if (workbook != null) {

			for (Row row : workbook.getSheet(GROUP_SHEET_NAME)) {

				if (row.getRowNum() < 1) {
					continue;
				}

				GroupData groupData = new GroupData();

				if (row.getCell(0) != null) {
					row.getCell(0).setCellType(row.getCell(0).CELL_TYPE_STRING);
					groupData.setGroup_no(Integer.parseInt(row.getCell(0).getStringCellValue()));
				}
				if (row.getCell(1) != null) {
					groupData.setGroup_name(row.getCell(1).getStringCellValue());
				}
				groupList.add(groupData);
			}
			try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return groupList;

	}

}
