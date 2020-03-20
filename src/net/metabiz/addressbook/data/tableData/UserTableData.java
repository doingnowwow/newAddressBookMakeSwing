package net.metabiz.addressbook.data.tableData;

import net.mbiz.edt.barcode.table.data.TableData;
import net.metabiz.addressbook.data.UserData;
import net.metabiz.addressbook.util.AbUtil;

public class UserTableData extends UserData implements TableData {

	public void setUserData(UserData userData) {
		
		this.setChecked(userData.isChecked());
		this.setAd_no(userData.getAd_no());
		this.setAd_name(userData.getAd_name());
		this.setAd_hp(userData.getAd_hp());
		this.setAd_mail(userData.getAd_mail());
		this.setAd_com(userData.getAd_com());
		this.setAd_department(userData.getAd_department());
		this.setAd_position(userData.getAd_position());
		this.setGroup_no(userData.getGroup_no());
		this.setAd_memo(userData.getAd_memo());

	}

	@Override
	public Object getValueByColumIndex(int col) {
		switch (col) {
		case 0:
			return this.isChecked();
		case 1:
			return this.getAd_name();
		case 2:
			return this.getAd_hp();
		case 3:
			return this.getAd_mail();
		case 4:
			return this.getAd_com();
		case 5:
			return this.getAd_department();
		case 6:
			return this.getAd_position();
		case 7:
			return AbUtil.getGroupName(this.getGroup_no(), true, null);
		case 8:
			return this.getAd_memo();
		

		}
		return "";
	}

	@Override
	public void setValueByColumIndex(int col, Object obj) {
		this.setChecked((boolean) obj);
	}

}
