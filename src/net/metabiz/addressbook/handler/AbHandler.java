package net.metabiz.addressbook.handler;

import java.util.ArrayList;

import net.metabiz.addressbook.data.GroupData;
import net.metabiz.addressbook.data.UserData;
import net.metabiz.addressbook.data.tableData.UserTableData;

public abstract class AbHandler {
	
	public void closeProgram() {
		
	}
	
	
	public boolean syncUserData(ArrayList<UserData> userDataList) {
		return false;
	}
	public boolean syncGroupData(ArrayList<GroupData> groupDataList) {
		return false;
	}
	
	public boolean insertUser(UserData userData) {
		return false;
	}

	public boolean updateUser(ArrayList<UserData> userDataList) {
		return false;
	}

	public boolean deleteUser(ArrayList<UserTableData> deleteUserList) {
		return false;
	}

	public boolean insertGroup(GroupData groupData) {
		return false;
	}

	public boolean updateGroup(GroupData groupData) {
		return false;
	}

	public boolean deleteGroup(GroupData groupData) {
		return false;
	}

	public ArrayList<GroupData> selectGroup() {
		return null;
	}

	public ArrayList<UserData> selectUserListByGroup(GroupData groupData) {
		return null;
	}

	private ArrayList<UserData> readUser() {
		return null;
	}

	public ArrayList<GroupData> readGroup() {
		return null;
	}
	
	public ArrayList<UserData> selectUserListBySearch(String code, String keyword) {
		return null;
	}

}
