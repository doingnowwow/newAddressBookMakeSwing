package net.metabiz.addressbook.util;

import java.util.ArrayList;

import net.metabiz.addressbook.data.GroupData;

public class AbUtil {

	public static ArrayList<GroupData> recodeGroupList ;
	/**
	 * 그룹번호를 받아서 그룹 이름 찾기
	 * 그룹이름으로 그룹번호찾기
	 * 
	 * 
	 * @param strInputGroupData : 들어갈 그룹 데이터  ex) 1,2,3  /  그룹1,그룹2,그룹3 
	 * @param isInputNumber : 숫자or 문자 인지 판별, 숫자 =true  / 문자 = false
	 * @param baseGroupList : 변경할 기준 그룹데이터 리스트 
	 * 
	 * @return String ex) 그룹1,그룹2,그룹3 /  1,2,3
	 */
	public static String getGroupName(String strInputGroupData, boolean isInputNumber, ArrayList<GroupData> baseGroupList) {
		
		if(baseGroupList!=null) {
			AbUtil.recodeGroupList = baseGroupList;
		}

		String resultStr = "";

		if (strInputGroupData != null) {
			if (!strInputGroupData.equals("")) {
				if (strInputGroupData.contains(",")) {
					String[] splitTxt = strInputGroupData.split(",");
					for (int i = 0; i < splitTxt.length; i++) {
						for (int j = 0; j < recodeGroupList.size(); j++) {

							if (isInputNumber) {
								// 번호 -> 이름
								if (splitTxt[i].equals(recodeGroupList.get(j).getGroup_no() + "")) {
									resultStr += recodeGroupList.get(j).getGroup_name() + ",";
									break;
								}
							} else {
								// 이름-> 번호
								if (splitTxt[i].equals(recodeGroupList.get(j).getGroup_name() + "")) {
									resultStr += recodeGroupList.get(j).getGroup_no() + ",";
									break;
								}
							}
						}

					}
					resultStr = resultStr.substring(0, resultStr.length() - 1);
				} else {
					for (int i = 0; i < recodeGroupList.size(); i++) {

						if (isInputNumber) {
							if (recodeGroupList.get(i).getGroup_no() == Integer.parseInt(strInputGroupData)) {
								resultStr = recodeGroupList.get(i).getGroup_name();
								break;
							}
						} else {
							if (recodeGroupList.get(i).getGroup_name().equals(strInputGroupData)) {
								resultStr = recodeGroupList.get(i).getGroup_no() + "";
								break;
							}
						}
					}
				}
			}
		}
		return resultStr;
	}

}
