package com.app.service;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.app.model.Person;
import com.app.repository.PersonRepository;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

@Service
public class PersonService{
	
	@Autowired
	public PersonService(
			@Value("${spring.datasource.username}") String user, 
			@Value("${spring.datasource.password}") String pass,
			@Value("${spring.datasource.url}") String url) {
		this.db_username = user;
		this.db_password = pass;
		this.db_url = url;
	};

	@Autowired
	PersonRepository personRepository;
	
	@PersistenceContext
	private EntityManager em;
	
	//static final String DB_URL = "jdbc:mysql://localhost:3306/hospital_v1?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";

	//  Database credentials
	private String db_username;
	private String db_password;
	private String db_url;

	public Person findForUsernameAndPassword(
			String username,String password) {
		Person p = new Person();
		java.sql.Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("USER: " + db_username + " PASS: " + db_password);
			conn = DriverManager.getConnection(db_url, db_username, db_password);
			String query = "SELECT * FROM person WHERE ( username ='"+ username + " ' AND password='"+password+"')";  //' or '1'='1
			java.sql.Statement stmt = conn.createStatement();						
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()){
				p.setId(rs.getInt("person_id"));
				p.setEmail(rs.getString("email"));
				p.setPassword(rs.getString("password"));
				p.setPersonalID(rs.getLong("personalid"));
				p.setUsername(rs.getString("username"));
				return p;
		      }
		      rs.close();
		      stmt.close();
		      conn.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return p;
	}
	
	
	public Person findByPersonalID(long id){
		return personRepository.findByPersonalID(id);
	}
	
	public Person findOne(int id){
		return personRepository.findOne(id);
	}
	
	public Person save(Person person){
		return personRepository.save(person);
	}

	public Boolean emailUnique(String email) {
		Person p = personRepository.findByEmail(email);
		if (p != null)
			return false;
		else
			return true;
	}

	public Boolean usernameUnique(String username) {
		Person p = personRepository.findByUsername(username);
		if (p != null)
			return false;
		else
			return true;
	}

	public Boolean personalIDUnique(String personalID) {
		Person p = personRepository.findByPersonalID(Long.parseLong(personalID));
		if (p != null)
			return false;
		else
			return true;
	}

	
}
