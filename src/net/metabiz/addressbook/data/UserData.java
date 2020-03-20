package net.metabiz.addressbook.data;

public class UserData {

	private boolean isChecked = false;
	private int ad_no;
	private String ad_name;
	private String ad_hp;
	private String ad_mail;
	private String ad_com;
	private String ad_department;
	private String ad_position;
	private String ad_memo;
	private String group_no;

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public int getAd_no() {
		return ad_no;
	}

	public void setAd_no(int ad_no) {
		this.ad_no = ad_no;
	}

	public String getAd_name() {
		return ad_name;
	}

	public void setAd_name(String ad_name) {
		this.ad_name = ad_name;
	}

	public String getAd_hp() {
		return ad_hp;
	}

	public void setAd_hp(String ad_hp) {
		this.ad_hp = ad_hp;
	}

	public String getAd_mail() {
		return ad_mail;
	}

	public void setAd_mail(String ad_mail) {
		this.ad_mail = ad_mail;
	}

	public String getAd_com() {
		return ad_com;
	}

	public void setAd_com(String ad_com) {
		this.ad_com = ad_com;
	}

	public String getAd_department() {
		return ad_department;
	}

	public void setAd_department(String ad_department) {
		this.ad_department = ad_department;
	}

	public String getAd_position() {
		return ad_position;
	}

	public void setAd_position(String ad_position) {
		this.ad_position = ad_position;
	}

	public String getAd_memo() {
		return ad_memo;
	}

	public void setAd_memo(String ad_memo) {
		this.ad_memo = ad_memo;
	}

	public String getGroup_no() {
		return group_no;
	}

	public void setGroup_no(String group_no) {
		this.group_no = group_no;
	}

	@Override
	public String toString() {
		return "UserData [isChecked=" + isChecked + ", ad_no=" + ad_no + ", ad_name=" + ad_name + ", ad_hp=" + ad_hp + ", ad_mail=" + ad_mail + ", ad_com=" + ad_com + ", ad_department=" + ad_department + ", ad_position=" + ad_position + ", ad_memo=" + ad_memo + ", group_no=" + group_no + "]";
	}

}
