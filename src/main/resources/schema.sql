create table if not exists employee
(
   employee_id integer primary key,
   employee_name varchar (50),
   age integer
);

create table if not exists m_user
(
   user_id varchar (50) primary key,
   password varchar (100),
   user_name varchar (50),
   birthday date,
   age integer,
   marriage boolean,
   role varchar (50)
);