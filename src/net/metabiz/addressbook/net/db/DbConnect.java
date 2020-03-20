package net.metabiz.addressbook.net.db;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import org.apache.ibatis.session.SqlSessionFactory;

public class DbConnect {

	private static SqlSessionFactory session;

	private static DbConnect instance;

	public DbConnect() {
	}

	public static DbConnect getInstance() {
		if (instance == null) {
			instance = new DbConnect();

		}
		return instance;
	}

	/** XML에 명시된 접속 정보를 읽어들인다. */
	// 클래스 초기화 블럭 : 클래스 변수의 복잡한 초기화에 사용된다.
	// 클래스가 처음 로딩될 때 한번만 수행된다.
	static {
		// 접속 정보를 명시하고 있는 XML의 경로 읽기
		// --> import java.io.Reader;
		// --> import org.apache.ibatis.io.Resources;
		try {
			// mybatis_config.xml 파일의 경로 지정
			Reader reader = Resources.getResourceAsReader("net/metabiz/addressbook/net/db/SqlMapConfig.xml");

			// sqlSessionFactory가 존재하지 않는다면 생성한다.
			if (session == null) {
				session = new SqlSessionFactoryBuilder().build(reader);
				System.out.println("========DB_session 성공======");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/** 데이터베이스 접속 객체를 통해 DATABASE에 접속한 세션를 리턴한다. */
	// --> import org.apache.ibatis.session.SqlSession;
	public static SqlSession getSqlSession() {
		return session.openSession();
	}

}
