一、性能优化
	1、性能优化思路
		1）获取慢查询sql
		2）执行explain计划
		3）使用show profile 查看有问题得sql得性能使用情况
	2、	mysql查询执行顺序
		1）from对查询指定得表计算笛卡尔积
		2）on按照join_condition过滤数据
		3）join添加关联外部表数据
		4）where按照where_condition过滤数据
		5）group by 进行分组操作
		6）having按照having_condition过滤数据
		7）select选择指定得列
		8）distinct指定列去重
		9）order by 按照 order_by_condition 排序
		10）limit 取出指定记录量
	3、mysql性能优化细节
		1）合理得创建及使用索引（考虑数据的增删情况）
		2）合理得冗余字段（尽量建一些大表，考虑数据库的三范式和业务设计的取舍）
		3）使用sql要注意一些细节
				select语句中尽量不要使用 * 、count(*)
				where 语句中尽量不要使用1=1 、in（建议使用exists）
				注意组合索引得创建顺序按照顺序组查询条件
				尽量查询粒度大得sql放到最左边
				尽量建立组合索引
		4）合理利用慢查询日志、explain执行计划查询、show profile 查看sql执行时得资源使用情况
		5）表关联查询时务必遵循小表驱动大表原则
		6）使用查询语句where条件时，不允许出现函数，否则索引会失效
		7）使用单表查询时，相同字段尽量不要用or，因为可能导致索引失效，比如 where name='手机' or name ='电脑'，可以使用union代替
		8）like语句不允许使用%开头，否则索引会失效；
		9）创建组合索引一定要遵循从左到右原则，否则索引会失效；比如：where name='张三' and age=18 ，那么该组合索引必须是name，age形式；
		10）索引不宜过多，根据实际情况决定，尽量不要超过10个；
		11）每张表都必须有主键，达到加快查询效率得目的；
	4、为什么要使用联合索引？
		减少开销：建立一个联合索引（col1,col2,col3），相当于建立（col1）、（col1,col2）、（col1,col2,col3）三个索引。每多一个索引，都会增加写操作得开销和磁盘空间得开销。对于大量数据得表，使用联合索引会大大的减少开销
		覆盖索引：查询列为联合索引得列，遍历索引就能得到数据，而无需回表，减少了很多随机io操作
		效率高：普通索引查1000 万数据先查第一个索引得数据集 然后第二个，第三个；联合所以一次性查询条件得结果集

二、分库分表，id重复问题如何解决

		1、定义函数+数据库sequence表管理自增长度，在不同主机上 起始步数不一样，自增长度为分得库表，在同一主机可以通过数据库名。表名获取统一 得主键自增管理
		2、通过uuid生成主键，，不好之处太长，占用空间大，不是有序得，会导致B+树索引在写得时候有过多得随机写操作，作为主键性能太差
		3、获取当前系统时间  并发高会有重复情况，一般加上当前数据义务唯一得值作为主键
		4、snowflake算法；

三、mysql的主从复制是如何实现的
		借助binlog日志文件里面的sql命令实现的主从复制；
		mysql的主从赋值是一个异步的复制过程，数据库从一个master复制到slave数据库，在master与slave之间实现整个主从复制的过程是由三个线程参与完成的，其中有两个线程（sql线程和io线程）在slave端，另一个线程（io线程）在master端

四、模糊查询中需要查询'_' 字符串怎么查？
        %    替代一个或多个
        _    替代一个
        [charlist] 字符列中的任何单一字符  以单个char开头得结果集
        [^charlist]或者[!charlist] 不在字符列中的任何单一字符 不以单个char开头得结果集

        在模糊查询中'_'代表通配符，需要查询'_' 字符串的数据需要加上 转义字符 并且指定转义字符
        例：last_name ='我_是刘辉'
        select last_name from employees where last_name like '_$_%'  escape '$';
          第一个_ 指第一个位置任意字符，$_ 指第二个位置为_ 的数据

五、mysql连表查询有哪些？
    inner join 取两个表都有得数据
    left join 取左表的数据
    right join 取右表的数据
    full join   全表查询；Oracle数据库支持full join，mysql是不支持full join的，但仍然可以同过左外连接+ union+右外连接实现
    union  两个查询结果得数据合并

六、 explain 得性能指标参数有哪些
    id ：说明每个对象（表）的执行顺序，id越大执行的越早，id越小执行执行越晚，id值一样按照顺序从前到后执行。
    select_type:查询类型
        simple：简单得sql查询，不包含子查询或者union
        primary：最外面得select查询
        subquery：子查询中得第一个select
        derived：在from列表中包含得子查询被标记为derived（衍生）mysql会递归执行这些子查询，把结果放在临时表里
        union： 若第二个select出现在union之后，则被标记为union；若union包含在from子句得子查询中，外层select将被标记为：derived
        union result：从union表获取结果得select
    table：当前查询得表名
    type：查询所使用得索引类型：级别：system>CONST>eq_ref>ref>range>index>all
        system: 表只有一行记录（等于系统表），这是const类型得特列
        const：查询表中得一个常量
        eq_ref: 唯一索引扫描 常用于主键或唯一索引扫描
        ref： 非唯一索引扫描，返回匹配莫个单独值得所有行
        range： 检索指定范围得行，查询一个返回内得=---------数据
        index： 只变量索引树（index和all都是全表读取，但index是从索引中读取，二all是从硬盘中读取）
        all: 将遍历全表以找到匹配得行
        备注：一般来说，得保证查询至少达到range级别，对号能达到ref；
    prossible_keys: 显示可能应用在这张表中得索引，一个或多个
    key: 实际使用得索引，如果为null，则没有使用索引；
    key_len: 表示索引中使用得字节数，长度越短越好；
    ref ：显示索引得哪一列被使用了，如果可能得话，是一个常数，哪些列或常量被用于查找索引列上得值
    rows：查找得数据行数
    Extra：包含不合适在其他列中显示但十分重要得额外信息
        Usingfileort: 得到所需结果集，需要对所有记录进行文件排序。
                在一个没有建立索引的列上进行了order by，就会触发filesort，常见的优化方案是，在order by的列上添加索引，避免每次查询都全量排序
        Usingtemporary: 这说明需要建立临时表(temporary table)来暂存中间结果
                典型的，group by和order by同时存在，且作用于不同的字段时，就会建立临时表，以便计算出最终的结果集。这类SQL语句性能较低，往往也需要进行优化。
                explain select * from user group by name order by sex;
        Using join buffer【block nested loop】: 需要嵌套循环计算
                例子：explain select * from user where id in(select id from user where sex='no');
                关联字段均未建立索引，就会出现这种情况。常见的优化方案是，在关联字段上添加索引，避免每次嵌套循环计算。


        上面都需要优化，还有不用优化得 Using index、 Using index condition 、Using where





