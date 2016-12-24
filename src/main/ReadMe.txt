# 手动添加外键约束
alter table exam
 add constraint fk_student foreign key(student_id) references student(id),
 add constraint fk_paper foreign key(paper_id) references paper(id);