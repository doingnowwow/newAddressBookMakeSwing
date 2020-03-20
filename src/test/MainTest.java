package test;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import net.metabiz.addressbook.net.db.DbConnect;

public class MainTest {

	public static void main(String[] args) {

		DbConnect connect = new DbConnect();
		SqlSession session = connect.getSqlSession();

		List<Test> testList = session.selectList("TestXml.selectUserAll", "d");

		System.out.println(testList);
	}

}
