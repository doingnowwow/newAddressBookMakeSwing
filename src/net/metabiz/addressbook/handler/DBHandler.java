package net.metabiz.addressbook.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.poi.ss.usermodel.Workbook;

import net.metabiz.addressbook.data.GroupData;
import net.metabiz.addressbook.data.UserData;
import net.metabiz.addressbook.data.tableData.UserTableData;
import net.metabiz.addressbook.net.db.DbConnect;
import net.metabiz.addressbook.util.AbUtil;

public class DBHandler extends AbHandler {

	private static DBHandler instance;

	public DBHandler() {
	}

	public static DBHandler getInstance() {

		if (instance == null) {
			instance = new DBHandler();
		}
		return instance;
	}

	@Override
	public void closeProgram() {
		ArrayList<GroupData> groupList = this.readGroup();
		ArrayList<UserData> userList = this.readUser();

		if (FileHandler.getInstance().syncUserData(userList)) {
			FileHandler.getInstance().syncGroupData(groupList);
		}

	}

	@Override
	public boolean syncUserData(ArrayList<UserData> userDataList) {
		boolean success = true;
		SqlSession session = DbConnect.getInstance().getSqlSession();

		int result = session.delete("user.delteTable");

		for (UserData userData : userDataList) {
			session.insert("user.insertUser", userData);
		}

		session.commit();

		if (session != null) {
			session.close();
		}
		return success;
	}

	@Override
	public boolean syncGroupData(ArrayList<GroupData> groupDataList) {
		boolean success = true;
		SqlSession session = DbConnect.getInstance().getSqlSession();

		int result = session.delete("group.delteTable");

		for (GroupData groupData : groupDataList) {
			session.insert("group.insertGroup", groupData);
		}
		session.commit();

		if (session != null) {
			session.close();
		}
		return success;
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

	public boolean insertUser(UserData userData) {
		// TODO 사용자 추가
		boolean success = false;
		SqlSession session = DbConnect.getInstance().getSqlSession();

		int result = session.insert("user.insertUser", userData);
		if (result == 1) {
			session.commit();
			success = true;
		}
		if (session != null) {
			session.close();
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
		// TODO 사용자 수정
		boolean success = false;
		SqlSession session = DbConnect.getInstance().getSqlSession();

		for (UserData userData : userDataList) {
			int result = session.update("user.updateUser", userData);
			if (result == 1) {
				session.commit();
				success = true;
			}
		}
		if (session != null) {
			session.close();
		}
		return success;

	}

	public boolean deleteUser(ArrayList<UserTableData> deleteUserList) {
		// TODO 사용자 삭제
		boolean success = false;
		SqlSession session = DbConnect.getInstance().getSqlSession();

		for (UserData user : deleteUserList) {
			int result = session.delete("user.deleteUser", user);
			if (result == 1) {
				session.commit();
				success = true;
			}
		}

		if (session != null) {
			session.close();
		}
		return success;
	}

	/**
	 * 그룹 목록
	 * 
	 * @return ArrayList<GroupData>
	 */
	public ArrayList<GroupData> selectGroup() {
		// TODO 그룹 전체목록
		return this.readGroup();
	}

	/**
	 * 그룹 추가
	 * 
	 * @param groupData
	 * @return
	 */
	public boolean insertGroup(GroupData groupData) {
		// TODO 그룹 추가
		boolean success = false;
		SqlSession session = DbConnect.getInstance().getSqlSession();

		int result = session.insert("group.insertGroup", groupData);
		if (result == 1) {
			session.commit();
			success = true;
		}

		if (session != null) {
			session.close();
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
		// TODO 그룹명 업데이트
		boolean success = false;

		SqlSession session = DbConnect.getInstance().getSqlSession();

		int result = session.update("group.updateGroup", groupData);
		if (result == 1) {
			session.commit();
			success = true;
		}
		if (session != null) {
			session.close();
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
		// TODO 그룹 삭제
		boolean success = false;
		SqlSession session = DbConnect.getInstance().getSqlSession();

		int result = session.delete("group.deleteGroup", groupData);
		if (result == 1) {
			session.commit();
			success = true;
		}
		// 사용자 리스트 갱신
		success = this.updateUser(this.updateUserDataByGroup(groupData));
		if (session != null) {
			session.close();
		}
		return success;
	}

	/**
	 * 사용자 전체 목록불러오기
	 * 
	 * @return
	 */
	private ArrayList<UserData> readUser() {
		// TODO 사용자 전체목록
		SqlSession session = DbConnect.getInstance().getSqlSession();

		List<UserData> userList = session.selectList("user.selectUserAll");
		ArrayList<UserData> arrayUserList = (ArrayList<UserData>) userList;

		return arrayUserList;

	}

	/**
	 * 그룹 전체 목록 불러오기
	 * 
	 * @return
	 */
	public ArrayList<GroupData> readGroup() {
		// TODO 그룹 전체목록
		SqlSession session = DbConnect.getInstance().getSqlSession();

		List<GroupData> groupList = session.selectList("group.selectGroupAll");
		ArrayList<GroupData> arrayGroupList = (ArrayList<GroupData>) groupList;

		return arrayGroupList;

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

}
