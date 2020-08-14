insert into employee
(
   employee_id,
   employee_name,
   age
)
values
(
   1,
   '山田太郎',
   30
);


insert into m_user
(
   user_id,
   password,
   user_name,
   birthday,
   age,
   marriage,
   role
)
values
(
   'yamada@msms.co.jp',
   '$2a$10$xRTXvpMWly0oGiu65WZlm.3YL95LGVV2ASFjDhe6WF4.Qji1huIPa',
   'yamada taro',
   '1991-01-01',
   28,
   false,
   'ROLE_ADMIN'
);

insert into m_user(
user_id,
   password,
   user_name,
   birthday,
   age,
   marriage,
   role)
   values(
   'tamura@ksks.co.jp',
   '$2a$10$xRTXvpMWly0oGiu65WZlm.3YL95LGVV2ASFjDhe6WF4.Qji1huIPa',
   '京極 わかかい',
   '1987-10-21',
   38,
   false,
   'ROLE_GENERAL'
   );