


-- 创建表
CREATE TABLE `sequence` (
`seq_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
`current_val` int(11) NOT NULL,
`increment_val` int(11) NOT NULL DEFAULT '1',
PRIMARY KEY (`seq_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




-- 创建函数
CREATE FUNCTION `currval`(v_seq_name VARCHAR(50)) RETURNS int(11)
begin
declare value integer;
set value = 0;
select current_val into value from sequence where seq_name = v_seq_name;
return value;
end


CREATE FUNCTION `nextval`(v_seq_name VARCHAR(50)) RETURNS int(11)
begin
update sequence set current_val = current_val + increment_val where seq_name = v_seq_name;
return currval(v_seq_name);
end


-- 表mapper insert添加


<selectKey order="BEFORE" keyProperty="yhbh" resultType="java.lang.String">
SELECT concat('${prefix}',lpad(nextval('sys_user'),4,'0')) as yhbh
</selectKey>