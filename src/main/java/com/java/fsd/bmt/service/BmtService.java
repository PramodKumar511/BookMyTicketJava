package com.java.fsd.bmt.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import com.java.fsd.bmt.customException.BookMyTicketException;
import com.java.fsd.bmt.entity.UserEntity;
import com.java.fsd.bmt.model.Login;
import com.java.fsd.bmt.model.SignUp;

@Service
public class BMTService {

	@Autowired
	DataSource dataSource;

	public UserEntity verifyUserCred(Login login) throws SQLException, BookMyTicketException {

		String query = "select uid,emailId,pwd" + " from bmtuser WHERE emailId = '" + login.getEmailId()
				+ "' AND pwd = '" + login.getPassword() + "'";
		UserEntity user = new UserEntity();
		try (Connection sqlConn = DataSourceUtils.getConnection(dataSource);
				PreparedStatement pstmt = sqlConn.prepareStatement(query);
				Statement creatStmt = sqlConn.createStatement();) {

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				user.setUid(Integer.parseInt(rs.getString("uid")));
				user.setEmailId(rs.getString("emailId"));
				user.setPwd(rs.getString("pwd"));
			}

		} catch (Exception e) {

			throw new BookMyTicketException("Unable to validate user");
		}

		return user;
	}

	public boolean isUserExist(String emailId) throws BookMyTicketException {

		String query = "select uid from dbo.bmtuser WHERE emailId = '" + emailId + "'";

		try (Connection sqlConn = DataSourceUtils.getConnection(dataSource);
				PreparedStatement pstmt = sqlConn.prepareStatement(query);
				Statement creatStmt = sqlConn.createStatement();) {
			ResultSet rs = pstmt.executeQuery();
			return rs.next();
		} catch (Exception e) {
			throw new BookMyTicketException("Unable to fetch user");
		}
	}

	public boolean userSignUp(SignUp signUpDetails) throws BookMyTicketException {

		if (isUserExist(signUpDetails.getEmail())) {
			return false;
		} else {

			String saveQuery = "insert into dbo.bmtuser(emailId, pwd) values ('" + signUpDetails.getEmail() + "','"
					+ signUpDetails.getPassword() + "')";

			try (Connection sqlConn = DataSourceUtils.getConnection(dataSource);
					PreparedStatement pstmtSave = sqlConn.prepareStatement(saveQuery);) {
				pstmtSave.executeUpdate();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				throw new BookMyTicketException("Unable to save user");
			}
		}
	}

}
