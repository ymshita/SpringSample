package jp.ymshita.demo.login.domain.repository.jdbc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowCallbackHandler;

public class UserRowCallbackHandler implements RowCallbackHandler {

	@Override
	public void processRow(ResultSet rs) throws SQLException {
		File file = new File("sample.csv");
		System.out.println("filepath: "+file.getPath());
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			do {
				String str = rs.getString("user_id") + "," 
						+ rs.getString("password") + "," 
						+ rs.getString("user_name") + "," 
						+ rs.getDate("birthday") + "," 
						+ rs.getInt("age") + "," 
						+ rs.getBoolean("marriage") + ","
						+ rs.getString("role"); 
				System.out.println("write line: "+str);
				bw.write(str);
				bw.newLine();
			}while(rs.next());
			bw.flush();
		}catch(Exception e) {
			e.printStackTrace();
			throw new SQLException();
		}finally {
			if(bw != null) {
				try {
					bw.close();
				}catch(Exception e2) {
					e2.printStackTrace();
				}	
			}
		}
		
	}

}
