### git 常用命令
1. git remote set-head origin master 设置远程git仓库的head
   1. git remote add origin git@github.com:*****
   将本地工程挂载到远程仓库 

### 关于使用maven 打jar 包的命令
1. 使用 mvn clean install -Dmaven.test.skip
   1. 如果springboot 项目通过上面的命令打包需要注意下面的事项
       * 如果项目是通过 parent的形式追加springboot的依赖， 那么需要追加下面的build才能让打处的jar包通过 java -jar 命令执行
           ``` xml
           <parent>
               <artifactId>spring-boot-starter-parent</artifactId>
               <groupId>org.springframework.boot</groupId>
               <version>2.4.4</version>
           </parent>
           ```
           ``` xml
           <build>
               <plugins>
                   <plugin>
                       <groupId>org.springframework.boot</groupId>
                       <artifactId>spring-boot-maven-plugin</artifactId>
    
                       <version>2.4.4</version>
                   </plugin>
               </plugins>
           </build>
           ```
       * 如果项目是通过 dependencyManagement的形式追加springboot的依赖， 那么需要追加下面的build才能让打处的jar包通过 java -jar 命令执行
           ``` xml
           <dependencyManagement>
               <dependencies>
                   <dependency>
                       <groupId>org.springframework.boot</groupId>
                       <artifactId>spring-boot-dependencies</artifactId>
                       <type>pom</type>
                       <scope>import</scope>
                       <version>2.4.4</version>
                   </dependency>
    
               </dependencies>
           </dependencyManagement>
           ```
           ``` xml
           <build>
               <plugins>
                   <plugin>
                       <groupId>org.springframework.boot</groupId>
                       <artifactId>spring-boot-maven-plugin</artifactId>
                       <executions>
                           <execution>
                               <goals>
                                   <goal>repackage</goal>
                               </goals>
                           </execution>
                       </executions>
                       <version>2.4.4</version>
                   </plugin>
               </plugins>
           </build>
           ```
   2. maven 手动 下载 source 文件mvn dependency:resolve -Dclassifier=sources
### Spring boot jdbc
#### spring boot 为jdbc 做了哪些配置
1. DataSourceAutoConfiguration
   * 配置DataSource
   1. DataSourceTransactionManagerAutoConfiguration
      * 配置DataSourceTransactionManager
   2. JdbcTemplateAutoConfiguration
      * 配置JdbcTemplate
#### 数据源的通用配置
   * spring.datasource.url=jdbc:mysql://localhost/test
     * spring.datasource.username=XXXX
     * spring.datasource.password=XXXX
     * spring.datasource.driver-class-name=com.mysql.jdbc.Driver
#### 初始化内嵌数据库
     * spring.datasource.initialization-mode=embedded|always|never
* spring.datasource.schema 与spring.datasource.data确定初始化sql文件
* spring.datasource.platform=hsqldb|h2|oracle|mysql
#### 多数据源手动配置
1. 通过primary 配置最优先的Datasource
2. 手动配置多数据源，排除`DataSourceAutoConfiguration`，
   `DataSourceTransactionManagerAutoConfiguration`，`JdbcTemplateAutoConfiguration`
   并配置里面的Datasource
   ``` java
   @SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class, JdbcTemplateAutoConfiguration.class})
   @Slf4j
   public class MultiDatasourceBootStrap {
       public static void main(String[] args) {
           SpringApplication.run(MultiDatasourceBootStrap.class,args);
       }
   
       @Autowired
       @Qualifier("fooDataSource")
       private DataSource foo;
       @Autowired
       @Qualifier("barDataSource")
       private DataSource bar;
   
       @Bean
       @ConfigurationProperties("foo.datasource")
       public DataSourceProperties fooDataSourceProperties(){
           return  new DataSourceProperties();
       }
       @Bean
       @ConfigurationProperties("bar.datasource")
       public DataSourceProperties barDataSourceProperties(){
           return  new DataSourceProperties();
       }
   
       @Bean
       public DataSource fooDataSource(@Qualifier("fooDataSourceProperties") DataSourceProperties fooDataSourceProperties){
   
           log.info("foo datasource: {}", fooDataSourceProperties.getUrl());
           return fooDataSourceProperties.initializeDataSourceBuilder().build();
       }
   
       @Bean
       public DataSource barDataSource(@Qualifier("barDataSourceProperties") DataSourceProperties barDataSourceProperties){
   
           log.info("foo datasource: {}", barDataSourceProperties.getUrl());
           return barDataSourceProperties.initializeDataSourceBuilder().build();
       }
   
       @Bean
       public PlatformTransactionManager barTxManager(@Qualifier("barDataSource") DataSource barDataSource) 		 {
           return new DataSourceTransactionManager(barDataSource);
       }
   
       @Bean
       public PlatformTransactionManager fooTxManager(@Qualifier("fooDataSource")DataSource fooDataSource) 		{
           return new DataSourceTransactionManager(fooDataSource);
       }
   }

   ```



#### spring JdbcTemplate 的基本操作

* query

  ``` java
  List<Foo> fooList = jdbcTemplate.query("SELECT * FROM FOO ", new RowMapper<Foo>() {
    @Override
    public Foo mapRow(ResultSet rs, int rowNum) throws SQLException {
      return Foo.builder().id(rs.getLong(1)).bar(rs.getString(2)).build();
    }
  });
  
  list.forEach(foo -> log.info("foo :{}" , foo));
  ```

  

* queryForList

  ``` java
  List<String> list = jdbcTemplate.queryForList("SELECT BAR FROM FOO", String.class);
  ```

* queryForObject

  ```java
  jdbcTemplate.queryForObject("SELECT COUNT(*) FROM FOO ",Long.class)
  ```

* update(可以实现插入删除修改 )

  ``` java
  INSERT INTO FOO (BAR) VALUES ('aaa');
  ```

* execute

  ```java
  @Autowired
  private SimpleJdbcInsert simpleJdbcInsert;
  @Bean
  @Autowired
  public SimpleJdbcInsert simpleJdbcInsert(JdbcTemplate jdbcTemplate){
    return new SimpleJdbcInsert(jdbcTemplate).withTableName("FOO").usingGeneratedKeyColumns("ID");
  }
  
  HashMap<String,String> map = new HashMap<>();
  map.put("bar", "d");
  Number id = simpleJdbcInsert.executeAndReturnKey(map);
  ```

* batchUpdate

  ```java
  jdbcTemplate.batchUpdate("INSERT INTO FOO (BAR) VALUES (?)", new BatchPreparedStatementSetter(){
  
    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
      ps.setString(1, "b-" + i);
    }
  
    @Override
    public int getBatchSize() {
      return 2;
    }
  });
  ```

  

### 具名JdbcTemplate（NamedParameterJdbcTemplate）

``` java
@Bean
@Autowired
public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource){
  return new NamedParameterJdbcTemplate(dataSource);
}

List<Foo> fooList =new ArrayList<>();
fooList.add(Foo.builder().bar("b-100").build());
fooList.add(Foo.builder().bar("b-101").build());
namedParameterJdbcTemplate.batchUpdate("INSERT INTO FOO (BAR) VALUES (:bar)", SqlParameterSourceUtils.createBatch(fooList));

```
### 事物的传播特性
| 传播性                    | 值   | 描述                                                         | 备注                                                         |
| ------------------------- | ---- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| PROPERGATION_REQUIRED     | 0    | 当前有事物就用当前的，没有就用新的                           | 默认的传播特性，如果没有，就开启一个事务；如果有，就加入当前事务（方法B看到自己已经运行在 方法A的事务内部，就不再起新的事务，直接加入方法A） |
| PROPERGATION_SUPPORTS     | 1    | 事物可有可无不是必须的                                       |                                                              |
| PROPERGATION_MANDARY      | 2    | 当前一定要有事物，没有就抛出异常                             |                                                              |
| PROPERGATION_REQUIRES_NEW | 3    | 无论有无事物，都要起一个新的事物                             | 如果没有，就开启一个事务；如果有，就将当前事务挂起。（方法A所在的事务就会挂起，方法B会起一个新的事务，等待方法B的事务完成以后，方法A才继续执行） |
| PROPERGATION_NOT_SUPPORT  | 4    | 不支持事物，按没有事物的方式运行                             |                                                              |
| PROPERGATION_NEVER        | 5    | 不支持事物，如果有事物就抛出异常                             |                                                              |
| PROPERGATION_NESTED       | 6    | 当前有事物，就在当前事物里再起一个新的事物\|里面事物的回滚，不会影响外面的事物 |                                                              |


1. serviceA 和 serviceB 都声明了事务，默认情况下，propagation=PROPAGATION_REQUIRED，整个service调用过程中，只存在一个共享的事务，当有任何异常发生的时候，所有操作回滚。
    ``` java
    @Transactional

    public void service(){

    serviceA();

    serviceB();

    }

    @Transactional

    serviceA();

    @Transactional

    serviceB();
    ```
2. PROPAGATION_SUPPORTS,由于serviceA运行时没有事务，这时候，如果底层数据源defaultAutoCommit=true，那么sql1是生效的，如果defaultAutoCommit=false，那么sql1无效，如果service有@Transactional标签，serviceA共用service的事务(不再依赖defaultAutoCommit)，此时，serviceA全部被回滚。
    ``` java
        public void service(){

            serviceA();

            throw new RunTimeException();

        }

        @Transactional(propagation=Propagation.SUPPORTS)

        serviceA();

        serviceA执行时当前没有事务，所以service中抛出的异常不会导致 serviceA回滚。

        再看一个小例子，代码如下：

        public void service(){

        serviceA();

        }

        @Transactional(propagation=Propagation.SUPPORTS)

        serviceA(){

        do sql 1

        1/0;

        do sql 2

        }
    ```
3. PROPAGATION_MANDATORY,这种情况执行 service会抛出异常，如果defaultAutoCommit=true，则serviceB是不会回滚的，defaultAutoCommit=false，则serviceB执行无效。
    ``` java 
    public void service(){

    serviceB();

    serviceA();

    }

    serviceB(){

    do sql

    }

    @Transactional(propagation=Propagation.MANDATORY)

    serviceA(){

    do sql

    }

    ```
4. PROPAGATN_REQUIRES_NEW 说明：如果当前存在事务，先把当前事务相关内容封装到一个实体，然后重新创建一个新事务，接受这个实体为参数，用于事务的恢复。更直白的说法就是暂停当前事务(当前无事务则不需要)，创建一个新事务。 针对这种情况，两个事务没有依赖关系，可以实现新事务回滚了，但外部事务继续执行。
    * 当调用service接口时，由于serviceA使用的是REQUIRES_NEW，它会创建一个新的事务，但由于serviceA抛出了运行时异常，导致serviceA整个被回滚了，而在service方法中，捕获了异常，所以serviceB是正常提交的。 注意，service中的try … catch 代码是必须的，否则service也会抛出异常，导致serviceB也被回滚。
    ``` java
    @Transactional

    public void service(){

    serviceB();

    try{

    serviceA();

    }catch(Exception e){

    }

    }

    serviceB(){

    do sql

    }

    @Transactional(propagation=Propagation.REQUIRES_NEW)

    serviceA(){

    do sql 1

    1/0;

    do sql 2

    }
    ```
5. Propagation.NOT_SUPPORTED 说明：如果当前存在事务，挂起当前事务，然后新的方法在没有事务的环境中执行，没有spring事务的环境下，sql的提交完全依赖于 defaultAutoCommit属性值 。
    ``` java
       @Transactional
       // 当调用service方法的时候，执行到serviceA方法中的1/0代码时，抛出了异常，
       //由于.serviceA处于无事务环境下，所以 sql1是否生效取决于defaultAutoCommit的值，
       //当defaultAutoCommit=true时，sql1是生效的，但是service由于抛出了异常，
       //所以serviceB会被回滚。
        public void service(){
    
        serviceB();
    
        serviceA();
    
        }
    
        serviceB(){
    
        do sql
    
        }
    
        @Transactional(propagation=Propagation.NOT_SUPPORTED)
    
        serviceA(){
    
        do sql 1
    
        1/0;
    
        do sql 2
    
        }
    
    ```
6.  PROPAGATION_NEVER说明： 如果当前存在事务，则抛出异常，否则在无事务环境上执行代码。

    ``` java 

    public void service(){

    serviceB();

    serviceA();

    }

    serviceB(){

    do sql

    }

    @Transactional(propagation=Propagation.NEVER)

    serviceA(){

    do sql 1

    1/0;

    do sql 2

    }

    //上面的示例调用service后，若defaultAutoCommit=true，
    //则serviceB方法及serviceA中的sql1都会生效。
    ```
7. PROPAGATION_NESTED说明： 如果当前存在事务，则使用 SavePoint 技术把当前事务状态进行保存，然后底层共用一个连接，当NESTED内部出错的时候，自行回滚到 SavePoint这个状态，只要外部捕获到了异常，就可以继续进行外部的事务提交，而不会受到内嵌业务的干扰，但是，如果外部事务抛出了异常，整个大事务都会回滚。注意： spring配置事务管理器要主动指定 nestedTransactionAllowed=true，如下所示：
    ``` xml
    <bean id=“dataTransactionManager”

    class=“org.springframework.jdbc.datasource.DataSourceTransactionManager”>

    <property name=“dataSource” ref=“dataDataSource” />

    <property name=“nestedTransactionAllowed” value=“true” />

    </bean>
    ```
    ``` java

    @Transactional

    public void service(){

    serviceA();

    try{

    serviceB();

    }catch(Exception e){

    }

    }

    serviceA(){

    do sql

    }

    @Transactional(propagation=Propagation.NESTED)

    serviceB(){

    do sql1

    1/0;

    do sql2

    }
    //sserviceB是一个内嵌的业务，内部抛出了运行时异常，
    //所以serviceB整个被回滚了，由于service捕获了异常，
    //所以serviceA是可以正常提交的。
    ```
   ``` java
    @Transactional
    
    public void service(){
    
    serviceA();
    
    serviceB();
    
    1/0;
    
    }
    
    @Transactional(propagation=Propagation.NESTED)
    
    serviceA(){
    
    do sql
    
    }
    
    serviceB(){
    
    do sql
    
    }
    //由于service抛出了异常，所以会导致整个service方法被回滚。
    //（这就是跟PROPAGATION_REQUIRES_NEW不一样的地方了，
    //NESTED方式下的内嵌业务会受到外部事务的异常而回滚	。）
   ```

#### 数据库事物的隔离特性

| 隔离性                  | 值   | 脏读 | 不可重复读 | 幻读 |
| ----------------------- | ---- | ---- | ---------- | ---- |
| ISOLATION_READ_UNCOMMIT | 1    | T    | T          | T    |
| ISOLATION_READ_COMMIT   | 2    | F    | T          | T    |
| ISOLATION_REPEATED_READ | 3    | F    | F          | T    |
| ISOLATION_SERIALIZABLE  | 4    | F    | F          | F    |

#### 编程式事物

1. TransactionTemplate 
   * TransactionCallBack
   * TransactionCallBackWithOutResult
   ``` java
     transactionTemplate.execute(new TransactionCallbackWithoutResult() {
        @Override
        protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
            jdbcTemplate.execute("insert into foo(bar) values ('aaa')");
        }
    });
   ```
2. PlatformTransactionManager
   * 可以传入TransactionDefinition进行定义

### 基于注解的事物
1. @EnableTransactionManager
2. 在需要事物处理的地方追加@Transacitonal
### 基于xml的事物
<tx:annotation-driven>
### spring 同过SQLErrorCodeSqlExceptionTranslator 解析错误妈
1. ErrorCode的定义
    org/springframework/jdbc/support/sql-error-codes.xml
2. 在classpath下创建自己的sql-error-codes.xml 可以实现自定一的sqlcode
### 常用的JPA注解
1. @Entity：注明这个类是一个实体类
2. @MappedSupperClass: 由多个实体类，多个实体类公用一个父类 ，在父类标注一个MappedSupperClass 
3. @Table注解， 将Table和实体类关联起来
4. @Id 主键
    * @GeneratedValue(strategy,generate)
    * @SequenceGenerator(name, sequenceName)
5. 映射
    * Column(name, nullble,length,insertable,updatable)
    * JoinTable(name),@JoinColumn(name)
6. 关系
    1. @OneToOne, @OneToMany , @ManyToOne ,@ManyToMany
    2. @OrderBy
``` java
@Table(name = "T_MENU")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coffee implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    @Column
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyAmount",
        parameters = {@org.hibernate.annotations.Parameter(name="currencyCode",value="CNY")})
    private Money price;

    @Column(updatable = false)
    private Date createDateTime;

    @UpdateTimestamp
    private Date updateDateTime;
}

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "T_ORDER")
public class CoffeeOrder {
    @Id
    @GeneratedValue
    private Long id;

    private String customer;
    @ManyToMany
    @JoinTable(name = "T_ORDER_COFFEE")
    private List<Coffee> item;

    @Column(nullable = false)
    private Integer state;

    @Column(updatable = false)
    private Date createDateTime;

    @UpdateTimestamp
    private Date updateDateTime;


}
```
``` properties
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.properties.hibernate.show_sql=true
spring.jpa.properties.hibernate.format_sql=true
```
#### Repository Bean 如何创建
* JapRepsitoriesRegisitory
    * 激活了@EnableJpaRepositories
    * 返回了JpaRepositoryConfigExtention
* RepositoryBeanDefinitionRegistaSupport.registorBeanDefinition
    * 注册RegistoryBean（类型是JpaRepositoryFactoryBean）
* RepositoryBeanDefinitionRegistaSupport.getRepositoryConfigurations
    * 取得Repository的Bean的配置
* JpaRepositoryFactory.getTargetRepository
    * 创建Repository
### Mybatis generator
java -jar mybatis-generator-core-xxxx.jar -configFile generatorConfig.xml

#### maven plugin(mybatis-maven-generator-plugin)
* mvn maven-generator:generate
* ${baseDir}/src/main/resources/generatorConfig.xml
``` xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">

<generatorConfiguration>
    <context id="H2Tables" targetRuntime="MyBatis3">
        <plugin type="org.mybatis.generator.plugins.FluentBuilderMethodsPlugin" />
        <plugin type="org.mybatis.generator.plugins.ToStringPlugin" />
        <plugin type="org.mybatis.generator.plugins.SerializablePlugin" />
        <plugin type="org.mybatis.generator.plugins.RowBoundsPlugin" />

        <jdbcConnection driverClass="com.mysql.cj.jdbc.Driver"
                        connectionURL="jdbc:mysql://localhost:3306/springbucks?useSSL=false"
                        userId="spring"
                        password="root">
        </jdbcConnection>

        <javaModelGenerator targetPackage="sizhe.chen.mybatis.model"
                            targetProject="./mybatis-generator/src/main/java">
            <property name="enableSubPackages" value="true" />
            <property name="trimStrings" value="true" />
        </javaModelGenerator>

        <sqlMapGenerator targetPackage="sizhe.chen.mybatis.mapper"
                         targetProject="./mybatis-generator/src/main/java">
            <property name="enableSubPackages" value="true" />
        </sqlMapGenerator>

        <javaClientGenerator type="MIXEDMAPPER"
                             targetPackage="sizhe.chen.mybatis.mapper"
                             targetProject="./mybatis-generator/src/main/java">
            <property name="enableSubPackages" value="true" />
        </javaClientGenerator>

        <table tableName="t_coffee" domainObjectName="Coffee" >
            <generatedKey column="id" sqlStatement="SELECT LAST_INSERT_ID()" identity="true" />
            <columnOverride column="price" javaType="org.joda.money.Money" jdbcType="BIGINT"
                            typeHandler="sizhe.chen.mybatis.handler.MoneyTypeHandler"/>
        </table>
    </context>
</generatorConfiguration>
```
``` properties
mybatis.type-aliases-package=sizhe.chen.mybatis.model
mybatis.type-handlers-package=sizhe.chen.mybatis.handler
mybatis.configuration.map-underscore-to-camel-case=true
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/springbucks?useSSL=false
spring.datasource.username=spring
spring.datasource.password=root
```
#### mage helper 使用的基本方法
1. maven依赖
    ``` xml
    <dependency>
        <groupId>com.github.pagehelper</groupId>
        <artifactId>pagehelper-spring-boot-starter</artifactId>
    </dependency> 
   ```
2. mapper的做成方法
   ``` Java
    @Mapper
    public interface CoffeeMapper {
    @Select("select * from t_coffee order by id")
    List<Coffee> findAllWithRowBounds(RowBounds rowBounds);
    
        @Select("select * from t_coffee order by id")
        List<Coffee> findAllWithParams(@Param("pageNum") int pageNum,
                                          @Param("pageSize") int pageSize);
    }
   ```
3. 配置
   ```  properties
    mybatis.type-handlers-package=sizhe.chen.mybatis.handler
    mybatis.configuration.map-underscore-to-camel-case=true
    
    pagehelper.offset-as-page-num=true
    pagehelper.reasonable=true
    pagehelper.page-size-zero=true
    pagehelper.support-methods-arguments=true
    ```
4. 调用
    ``` Java
        // 使用RowBounds的方式， 没有页信息
        coffeeMapper.findAllWithRowBounds(new RowBounds(1, 0))
                .forEach(c -> log.info("Page(1) Coffee {}", c));
        // 使用pageHelper的方式，可以携带页信息
        coffeeMapper.findAllWithParams(1, 3)
                .forEach(c -> log.info("Page(1) Coffee {}", c));
        List<Coffee> list = coffeeMapper.findAllWithParams(2, 3);
        PageInfo page = new PageInfo(list);
    ```
### Docker
#### Docker 的基本命令
1. 镜像相关
    * docker pull <image>
    * docker search <image>
2. 容器相关
    * docker run
    * docker start / stop 容器名
    * docker ps <容器名>
    * docker logs <容器名 >
3. docker run [options] image [command] [arg .... ]
    * 选项说明
        * -d 后台运行
        * -e 设置环境变量
        * --expose /-p 宿主端口:容器端口
        * --name,指定容器名
        * --link 连接不同容器
        * -v 宿主目录：容器目录 挂载磁盘卷
#### docker 的镜像配置
1. dockerhub
    * https://hub.docker.com
2. 官方镜像
    * https://www.docker-cn.com/registory-mirror
    * https://www.docker-cn.com/get-docker
3. 阿里云镜像
    * https://dev.aliyun.com
#### docker 设置镜像代理
``` json
{
  "experimental": false,
  "features": {
    "buildkit": true
  },
  "registry-mirrors": [
    "https://docker.mirrors.ustc.edu.cn",
    "https://registry.docker-cn.com",
    "http://hub-mirror.c.163.com"
  ]
}
```
#### docker mysql
* 官方指引
    * https://hub.docker.com/_/mysql
* 获取镜像
    * docker pull mysql
* 运行Mysql 镜像
    * docker run  --name mysql -d -p 3306:3306 -v ~/docker-data/mysql:/var/lib/mysql -e MYSQL_DATABASE=springbucks -e MYSQL_USER=springbucks -e MYSQL_PASSOWRD=springbucks -e MYSQL_ROOT_PASSWORD=root mysql 
    * docker exec -it mysql bash  
#### docker mongo
1. docker pull mongo
2. docker run --name mongo -p 27017:27017 -v ~/docker-data/mongo:/data/db -e MONGO_INITDB_ROOT_USERNAME=admin  -e MONGO_INITDB_ROOT_PASSWORD=admin -d mongo 
3. 登陆到MogonDb 容器中
    * docker exec -it mongo bash
4. 通过shell连接mongodb
    * mongo -u admin -p admin
#### spring 对MongoDB 的支持
1. Spring Data MongoDB
   1. MongoTemplate
   2. Repository的支持
2. 注解
   1. @Document
   2. @Id
3. MongoTemplate
   1. save/remove
   2. criteral/Query/update
4. Mongo DB 创建 数据库
   1. `use springbucks;`
   2. 创建User
      ``` json 
      db.createUser(
       {
        user: "springbucks",
        pwd: "springbucks",
        roles: [
          {role: "readWrite",db: "springbucks"}  
        ]
       }
      )   
      ```
   3. 查看数据
      ``` 
         dshow collecitons;
         db.coffee.find();
         db.coffee.remove({"name":"espresso"})
   4. ```
#### 搭建一个简单的MongoDb工程
   1. maven 依赖
      ``` xml 
      <dependency>
          <groupId>org.springframework.boot</groupId>
               <artifactId>spring-boot-starter-data-mongodb</artifactId>
      </dependency>
      ```
   2. 配置application.properties
      ``` java
         spring.data.mongodb.uri=mongodb://springbucks:springbucks@localhost:27017/springbucks
      ```
   3. 对于特殊类型配置converter
      ``` java 
      public class MoneyReadConverter implements Converter<Document, Money> {
       @Override
       public Money convert(Document source) {
           Document money = (Document) source.get("money");
           double amount = Double.parseDouble(money.getString("amount"));
           String currency = ((Document) money.get("currency")).getString("code");
           return Money.of(CurrencyUnit.of(currency), amount);
       }
      }
      @Bean
       public MongoCustomConversions mongoCustomConversions() {
           return new MongoCustomConversions(Arrays.asList(new MoneyReadConverter()));
       }
      ```
   4. MongoDbTemplate 如何使用
      ``` java 
         Coffee espresso = Coffee.builder()
                   .name("espresso")
                   .price(Money.of(CurrencyUnit.of("CNY"), 20.0))
                   .createTime(new Date())
                   .updateTime(new Date()).build();
           Coffee saved = mongoTemplate.save(espresso);
           log.info("Coffee {}", saved);

           List<Coffee> list = mongoTemplate.find(
                   Query.query(Criteria.where("name").is("espresso")), Coffee.class);
           log.info("Find {} Coffee", list.size());
           list.forEach(c -> log.info("Coffee {}", c));

           Thread.sleep(1000); // 为了看更新时间
           UpdateResult result = mongoTemplate.updateFirst(query(where("name").is("espresso")),
                   new Update().set("price", Money.ofMajor(CurrencyUnit.of("CNY"), 30))
                           .currentDate("updateTime"),
                   Coffee.class);
           log.info("Update Result: {}", result.getModifiedCount());
           Coffee updateOne = mongoTemplate.findById(saved.getId(), Coffee.class);
           log.info("Update Result: {}", updateOne);

           mongoTemplate.remove(updateOne);
   
      ```
   5. 定义Model
      ``` java 
      @Document
      @Data
      @NoArgsConstructor
      @AllArgsConstructor
      @Builder
      public class Coffee {
      @Id
      private String id;
      private String name;
      private Money price;
      private Date createTime;
      private Date updateTime;
      }
      ```
#### Spring data mongodb repository
1. 启动
   1. @EnableMongoRepositories
2. 对应接口
   * MongoRepository<T,ID>
      ``` java 
       public interface CoffeeRepository extends MongoRepository<Coffee,String> {

           List<Coffee> findByName(String name);
       }
      ```
   * PagingAndSortingRepository<T,ID>
   * CurdRepository<T,ID>
3. 使用方法
   ``` java 
      Coffee espresso = Coffee.
                builder().name("espresso").price(Money.of(CurrencyUnit.of("CNY"),20))
                .createTime(new Date()).updateTime(new Date()).build();
         Coffee latte = Coffee.
                builder().name("latte").price(Money.of(CurrencyUnit.of("CNY"),30))
                .createTime(new Date()).updateTime(new Date()).build();

        coffeeRepository.insert(Arrays.asList(latte,espresso));
        coffeeRepository.findAll(Sort.by("name")).forEach(
                c -> log.info("coffee: {}", c)
        );

        Thread.sleep(1000);
        latte.setPrice(Money.of(CurrencyUnit.of("CNY"),35));
        latte.setUpdateTime(new Date());
        coffeeRepository.save(latte);

        coffeeRepository.insert(Arrays.asList(latte,espresso));
        coffeeRepository.findByName("latte").forEach(
                c -> log.info("coffee: {}", c)
        );

        coffeeRepository.deleteAll();
   ```
### Redis
Redis 是一款开源的内存KV存储，支持多种数据结构
#### Spring 对Redis 的支持
* Spring Data Redis
  * 支持的客户端 jedis/lettuce
  * RedisTemplate
  * Repository 支持

#### jedis客户端的简单使用
* jedis 不是线程安全的。
* 通过JedisPool获得jedis实例
* 直接使用jedis中的方法
#### 通过Docker启动Redis
1. 官方指引：https://hub.docker.com/_redis
2. 获取镜像： `docker pull redis`
3. 启动reids
   ```
   docker run --name redis -d -p 6379:6379 redis
   ```
4. 查看进程 `docker ps -a`
#### spring jedis 工程
1. maven 依赖
   ```xml
   <dependency>
        <groupId>redis.clients</groupId>
        <artifactId>jedis</artifactId>
   </dependency>
   ```
2. 配置application.properties
   ```  properties
   redis.host=localhost
   redis.maxTotal=5
   redis.maxIdle=5
   redis.testOnBorrow=true
   ```
3. 引入Jedis 的java 配置
   ```java 
   @Bean
    @ConfigurationProperties("redis")
    public  JedisPoolConfig jedisPoolConfig(){
        return  new JedisPoolConfig();
    }
    @Bean(destroyMethod = "close")
    public JedisPool jedisPool(@Qualifier("jedisPoolConfig") JedisPoolConfig jedisPoolConfig
            ,@Value("${redis.host}") String host){
        return new JedisPool(jedisPoolConfig,host);
    }
   ```
4. jedis 的基本用法
   ``` java 
   try (Jedis jedis = jedisPool.getResource()) {
            coffeeService.findAllCoffee().forEach(c -> {
                jedis.hset("springbucks-menu",
                        c.getName(),
                        Long.toString(c.getPrice().getAmountMinorLong()));
            });

            Map<String, String> menu = jedis.hgetAll("springbucks-menu");
            log.info("Menu: {}", menu);

            String price = jedis.hget("springbucks-menu", "espresso");
            log.info("espresso - {}",
                    Money.ofMinor(CurrencyUnit.of("CNY"), Long.parseLong(price)));
        }    
   ```
#### redis 的部署
1. redis的哨兵模式
   1. Redis 的Sentinel（哨兵）是redis的一种高可用方式
      * 监控,通知,自动故障转移，服务发现
      ```
                     +------+
                     ｜ M1  |
                     ｜ S1  |
                     +------+
                         |
        +------+         |        +------+ 
        ｜ R2  |         |        ｜ R3   | 
        ｜ S2  | --------+--------｜ S3   |
        +------+                  +------+
   2. Jedis 通过JedisSentinelPool 实现哨兵模式
2. redis的集群模式
   1. RedisCluster 数据自动分片（分成16384个Hash pot）
   2. 在一部分节点失效时有一定的可用性
   3. JedisCluster
      1. Jedis只从Master取数据，如果想要自动读写分离需要定制
#### Spring  的缓存抽象
1. 为不同的缓存提供一个抽象层
   1. 为Java 方法增加缓存，缓存执行结果
   2. 支持ConcurrentMap，EhCache，Caffeine,Jcache
   3. 接口
      * org.springframework.cache.Cache
      * org.springframework.cache.CacheManager
2. 基于注解的缓存
   * @EnableCache
     * @Cacheable : 执行方法，如果方法的结果已经在缓存里面了，就直接取，如果没有，就存入缓存
     * @CacheEvict： 清理缓存
     * @CachePut： 不管执行结果是否在缓存里， 都将结果放入缓存
     * @Caching： 可以进行多个操作，多上面的执行功能进行打包
     * @CacheConfig ：  对缓存做一个设置 ，设置缓存的名字等 
#### spring 使用java 的内存进行缓存的方法
1. 开启缓存
   ``` java 
   @EnableCaching(proxyTargetClass = true)
   public class SpringBucksApplication implements ApplicationRunner {
   ```
2. 对使用缓存的方法进行配置
   ``` java 
   @Slf4j
   @Service
   @CacheConfig(cacheNames = "coffee") // 对缓存进行配置，这个例子没有使用，但是在redis作为缓存容器的时候使用了
   public class CoffeeService {
   @Autowired
   private CoffeeRepository coffeeRepository;

    @Cacheable //追加缓存
    public List<Coffee> findAllCoffee() {
        return coffeeRepository.findAll();
    }

    @CacheEvict //清除缓存
    public void reloadCoffee() {
    }
   ```
#### Spring 使用Redis 的方式进行缓存
1. 具体使用缓存的注解参考那个上面使用java进行缓存的例子，是一样的。 
2. maven 依赖
   ``` xml 
   <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-cache</artifactId>
   </dependency>
   <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
   </dependency>
   ```
3. Spring boot 配置
   ``` properties 
   spring.cache.type=redis
   spring.cache.cache-names=coffee
   spring.cache.redis.time-to-live=5000
   spring.cache.redis.cache-null-values=false
   
   spring.redis.host=localhost
   ```
####  与Redis 建立连接
1. 配置连接工厂
   1. LettuceConnectionFactory 和 JedisConnectionFactory
   2. RedisStandaloneConfiguration(单例)
   3. RedisSentinelConfiguration(哨兵)
   4. RedisClusterConfiguration（集群）
#### 使用redis 的几种方法
1. 使用jedis （jedisPool.getResource）
2. 使用redis 当作缓存容器
3. 使用RedisTemplate（像数据库一样，相当与jdbcTemplate）【StringRedisTemplate】
   1. opsForXXXX()
4. 使用RedisRepository （相当于JPA）
#### Redis 命令行的简单查询
1. docker exec -it redis bash
2. keys * 
3. HGETALL "XXXXXXXXXXXXXX"
#### Redis Template 的用发
1. 加入Maven 依赖
    ``` xml 
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-pool2</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    ```
2. 配置SpringBoot Rdeis
    ``` java 
    spring.jpa.hibernate.ddl-auto=none
    spring.jpa.properties.hibernate.show_sql=true
    spring.jpa.properties.hibernate.format_sql=true
    
    spring.redis.host=localhost
    spring.redis.lettuce.pool.maxActive=5
    spring.redis.lettuce.pool.maxIdle=5
    ```
3. 配置bean
    ``` java
     @Bean
    public RedisTemplate<String , Coffee> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,Coffee> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

       return redisTemplate;
    }

    @Bean
    public LettuceClientConfigurationBuilderCustomizer clientConfigurationBuilderCustomizer(){
        return  builder -> builder.readFrom(ReadFrom.MASTER);
    }
    ```
4. RedisTemplate 的使用方法
    ``` Java
    //设置缓存名称
    private static final String CACHE="SpringBucks-coffee";
    public Optional<Coffee> findOneCoffee(String name){
        HashOperations<String,String,Coffee> hashOperations
                = redisTemplate.opsForHash();
        if(redisTemplate.hasKey(CACHE) && hashOperations.hasKey(CACHE,name)){
            log.info("Get coffee: {} fromm redis ", name);
            return Optional.of(hashOperations.get(CACHE,name)) ;
        }
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("name", exact().ignoreCase());
        Optional<Coffee> coffee = repository.findOne(Example.of(
                Coffee.builder().name(name).build(),exampleMatcher
        ));
        log.info("Coffee find {}" ,coffee);

        if(coffee.isPresent()){
            log.info("put coffee {} to redis" ,coffee);
            hashOperations.put(CACHE,name,coffee.get());
            redisTemplate.expire(CACHE,1, TimeUnit.MINUTES);
        }
        return coffee;
    }
   ```
#### Redis Repository
* 实体注解
    * @RedisBash
    * @Id
    * @Indexed
* 如何区分不同数据源的Repository
    * 根据实体的注解
    * 根据继承的接口类型
    * 扫描不同的包 
* RedisRepository 的用法
    1. 加入Maven 依赖
        ``` xml 
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-pool2</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        ```
    2. 配置SpringBoot Rdeis
        ``` java 
        spring.jpa.hibernate.ddl-auto=none
        spring.jpa.properties.hibernate.show_sql=true
        spring.jpa.properties.hibernate.format_sql=true
        
        spring.redis.host=localhost
        spring.redis.lettuce.pool.maxActive=5
        spring.redis.lettuce.pool.maxIdle=5
        ```
     3. 创建Coverter
        ``` java 
        @ReadingConverter
        public class BytesToMoneyConverter implements Converter<byte[], Money> {
            @Override
            public Money convert(byte[] bytes) {
                String value = new String(bytes, StandardCharsets.UTF_8);
                return Money.ofMinor(CurrencyUnit.of("CNY"),Long.parseLong(value));
            }
        }
        @WritingConverter
        public class MoneyToBytesConverter implements Converter<Money,byte[]> {
            @Override
            public byte[] convert(Money money) {
                String value = Long.toString(money.getAmountMinorLong());
                return value.getBytes(StandardCharsets.UTF_8);
            }
        }
        ```
    4. 配置Bean
        ``` java
          @Bean
        public RedisCustomConversions redisCustomConversions(){
            return new RedisCustomConversions(
                    Arrays.asList(new MoneyToBytesConverter(),new BytesToMoneyConverter()));
        }
        @Bean
        public LettuceClientConfigurationBuilderCustomizer lettuceClientConfigurationBuilderCustomizer(){
            return clientConfigurationBuilder -> clientConfigurationBuilder.readFrom(ReadFrom.MASTER_PREFERRED);
        }
        ```
   5. 创建Cache使用的Model
        ``` java 
        @RedisHash(value = "springbucks-coffee",timeToLive = 60)
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public class CoffeeCache {
            @Id
            private Long id;
            @Indexed
            private String name;
            private Money price;
        }
        ```
  6. 使用方法
    ``` java
    public Optional<Coffee> findSimpleCoffeeFromCache(String name) {
        Optional<CoffeeCache> cached = cacheRepository.findOneByName(name);
        if (cached.isPresent()) {
            CoffeeCache coffeeCache = cached.get();
            Coffee coffee = Coffee.builder()
                    .name(coffeeCache.getName())
                    .price(coffeeCache.getPrice())
                    .build();
            log.info("Coffee {} found in cache.", coffeeCache);
            return Optional.of(coffee);
        } else {
            Optional<Coffee> raw = findOneCoffee(name);
            raw.ifPresent(c -> {
                CoffeeCache coffeeCache = CoffeeCache.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .price(c.getPrice())
                        .build();
                log.info("Save Coffee {} to cache.", coffeeCache);
                cacheRepository.save(coffeeCache);
            });
            return raw;
        }
    ```
### Project Reactor
1. Operators 
    1. Publisher & subscribe
        * 在调用subscribe()函数之前，不会触发任何处理（类似java 8的
          stream 操作没有调用终止处理）
        * Flex[0...n] -onNext(),onError(),onComplete()
        * Mono[0...1]-onNext(),onError(),onComplete()
    2. Backpressure（背压）
        * subscription
        * onRequest(), onCancel(), onDispose()  
            1. 下游可以控制上有， 接受多少个参数， 取消，终止 阅等
2. 线程调度（schedulers）
    1. immediate()/single()/newSingle()
    2. elastic()/parallel()/newParallel()
3. 错误处理
    1. onError()/onErrorReturn()/onErrorResume()
    2. doOnError()/doFinally()
4. 基本例子
    ``` Java
    Flux.range(1,6)
               // .publishOn(Schedulers.elastic())
                .doOnRequest(n -> log.info("Request {} number",n))
                .doOnComplete(() -> log.info("Publisher Complete 1"))
                .publishOn(Schedulers.elastic())
                .map(n -> {
                        log.info("publish {},{}",Thread.currentThread(),n);
                        //异常处理
                      // return  10/(n-3);
                        return  n;
                })
                .doOnComplete(() -> log.info("Publisher Complete 2"))
                .subscribeOn(Schedulers.single())
                .onErrorResume(e -> {
                    log.error("Exception : {} " ,e.toString());
                    return Mono.just(-1);
                })
                //.onErrorReturn(-1)
                .subscribe(i -> log.info("Subscribe {} : {}", Thread.currentThread(),i),
                        e->log.error("errors {} ", e,toString()),
                        ()-> log.info("Subscriber Complete")
                        ,s-> s.request(4)
                        );
        Thread.sleep(1000);
    ```
#### Reactive redis
1. Lettuce 支持 Reactive 的方式
2. Spring Data Redis 中主要支持
    1. ReactiveRedisConnection
    2. ReactiveRedisConnectionFactory
    3. ReactiveRedisTemplate
       1. OpsForXXXX()
3. maven 依赖
    ``` xml 
     <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis-reactive</artifactId>
     </dependency>
     ```
4. spring 工程配置文件
    ``` properties 
    spring.redis.host=localhost
    ```
5. 实现
    ``` java
     @Autowired
    private ReactiveStringRedisTemplate redisTemplate;

    @Bean
    private ReactiveStringRedisTemplate reactiveStringRedisTemplate(ReactiveRedisConnectionFactory redisConnectionFactory){
        return new ReactiveStringRedisTemplate(redisConnectionFactory);
    }
    @Override
    public void run(ApplicationArguments args) throws Exception {
        ReactiveHashOperations<String,String,String> reactiveHashOperations = redisTemplate.opsForHash();
        CountDownLatch count = new CountDownLatch(1);

        List<Coffee> coffeeList = jdbcTemplate.query("Select * from t_coffee" ,
                (rs,i ) ->{
                    return Coffee.builder().id(rs.getLong("id")).name(rs.getString("name")).price(rs.getLong("price"))
                            .createTime(rs.getDate("create_Time"))
                            .updateTime(rs.getDate("update_Time")).build();
                });
        Flux.fromIterable(coffeeList)
                .publishOn(Schedulers.single())
                .doOnComplete(() -> log.info("list ok"))
                .flatMap(c -> {
                    log.info("try to put {}:{}",c.getName(),c.getPrice() );
                    return reactiveHashOperations.put(KEY,c.getName(),c.getPrice().toString());
                })
                .doOnComplete(() -> log.info("set ok"))
                .concatWith(redisTemplate.expire(KEY, Duration.ofMinutes(1)))
                .doOnComplete(() -> log.info("expire ok"))
                .onErrorResume(e -> {
                    log.error("exception {}", e.getMessage());
                    return Mono.just(false);
                })
                .subscribe( b -> log.info("boolean : {}", b)
                ,e -> log.error("Exception {}" ,e.getMessage())
                        ,() -> count.countDown()
                );
        log.info("waiting-----");
        count.await();
    }
    ```
#### Reactive Mongo
1. MongoDB 的官网提供了支持reactive的驱动
    `mongod-driver-reactivestreams`
2. Spring Data MongoDB 中的主要支持
    * ReactiveMongoClientClientFactoryBean
    * ReactiveMongoDatabaseFactory
    * ReactiveMongoTemplate
3. maven 依赖
    ``` xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-mongodb-reactive</artifactId>
    </dependency>
    ```
4. 使用方法
    ``` java 
    @Autowired
    private ReactiveMongoTemplate template;
    private CountDownLatch countDownLatch = new CountDownLatch(2);

    @Bean
    public MongoCustomConversions mongoCustomConversions(){
        return new MongoCustomConversions(List.of(new MoneyReadConverter(),new MoneyWriteConverter()));
    }
    @Override
    public void run(ApplicationArguments args) throws Exception {
        startFromInsertion(() -> {
            decreaseHighPrice();
        });

        log.info("after starting ");

        countDownLatch.await();
    }

    private void startFromInsertion(Runnable runnable){
        template.insertAll(initCoffee())
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(c -> log.info("Next: {} ",c))
                .doOnComplete(runnable)
                .doFinally(s -> {
                    countDownLatch.countDown();
                    log.info("Finally 1, {} ", s);
                })
                .count()
                .subscribe(c -> log.info("insert {} records", c));
    }

    private void decreaseHighPrice(){
        template.updateMulti(
                Query.query(where("price").gte(3000L)),
                new Update().inc("price", -500L)
                .currentDate("updateTime"),Coffee.class)
                .doFinally(
                        s -> {
                            countDownLatch.countDown();
                            log.info("Finally 2 {}" , s);
                        }
                )
                .subscribe(r -> log.info("Result is {}", r));


    }

    private List<Coffee> initCoffee(){
        Coffee espresso = Coffee.builder()
                .name("espresso")
                .price(Money.of(CurrencyUnit.of("CNY"),20.0))
                .createTime(new Date())
                .updateTime(new Date())
                .build();
        Coffee latte  = Coffee.builder()
                .name("latte")
                .price(Money.of(CurrencyUnit.of("CNY"),35.0))
                .createTime(new Date())
                .updateTime(new Date())
                .build();

        return Arrays.asList(espresso,latte);

    }
    ```
#### 使用Aop访问数据访问层的摘要
1. Aop的核心概念
    * Aspect : 切面
    * Join Point： 连接点，Spring Aop里面总是代表一次方法执行。
    * advice：通知，在连接点执行的动作
    * pointCut：切入点，说明如何匹配连接点
    * Introduction：为现有函数声明额外的方法和属性
    * Target Object：目标对象
    * AopProxy：Aop代理对象，可以是动态代理，也可以是CGLIB代理
    * Weaving：织入，连接切面与目标对象或类型创建代理的过程

2. 常用注解
    * @EnableAspectJAutoProxy : 开启了对AspectJ的支持，
    * @Aspect
    * @PointCut
    * @Before
    * @After/@AfterReturning/@AfterThrowing
    * @Around
    * @Order
3. 如何打印SQL 通过Aspect
    1. 针对HikariCP 需要引入jar P6SPY
### Spring mvc

#### 认识Spring Mvc
1. Dispatch Servlet
    1. Controller
    2. XXXResolver
        1. ViewResolver
        2. HandlerExceptionResolver
        3. MultipartResolver
    4. HandlerMapping : 请求的映射处理逻辑 
2. SpringMvc中的常用注解
    * @Controller  
        * @RestController 
    * @RequestMapping
        *  @GetMapping @PostMapping @DeleteMapping @PutMapping
    * @RequestBody @ResponseBody @ResponseStatus
### Application Context
![Application context](./image/applicationContainer.png)
* Spring 应用上下文
    * BeanFactory
        * DefaultListableBeanFactory
    * ApplicationContext
        * ClassPathXmlApplicationContext
        * FileSystemXmlApplicationContext
        * AnnotationConfigAppliationContext
    * WebApplicationContext
* 应用程序上下文的继承层次
  ![继承层次](./image/ApplicationContextHirachy.png)
#### spring mvc 的处理流程
![springmvc 处理流程](./image/spring%20mvc%20flows.png)
* 绑定一些Attribute
    * WebApplicationContext/LocalResolver/ThemeResolver
* 处理Multiply
    * 如果是将装换为`MultiplyHttpServletRequest`
* Handler处理
    * 如果找到对应的handler，执行controller 的前后置处理器
* 返回model 呈现视图
#### Spring Mvc 的视图处理机制
1. ViewResolver 与 View 接口
    * AbstractCachingViewResolver
    * UrlBasedViewResolver
    * FreeMarkerViewResolver
    * ContentNegotiationViewResolver
    * InterResourceViewResolver
2. DispatchServlet中的视图解析逻辑
    * initStrategy()
        * initViewResolvers()初始化了对应ViewResolver
    * doDispatch()
        * processDispatchResult()
            * 没有视图的话，尝试RequestToViewNameTransfer
            * resolveViewName()解析View对象
3. 使用@ResponseBody的情况
    1. 在HandlerAdapter.handle()中完成了Response的输出
        * RequestMappingHandlerAdapter.invokeHandlerMethod()
            * HandlerMethodReturnValueHandlerComposite.handlerReturnValue()
                * RequestResponseBodyMethodProcessor.handleReturnValue()
4. 视图的重定向
    1. 两种不同的重定向前缀
        * redirect
        * forward
#### Thymeleaf 
1. starter:`spring-boot-starter-thymeleaf`
2. Springboot自动配置类：ThymeleafAutoConfiguration
    * ThymeleafViewResolver 
3. SpringBoot Thymeleaf 常用配置
    1. spring.thymeleaf.cache=true
    2. spring.thymeleaf.check-template-location=true
    3. spring.thymeleaf.enabled=true
    4. spring.thymeleaf.encoding=UTF-8
    5. spring.thymeleaf.mode=HTML
    6. spring.thymeleaf.servlet.content-type=text/html
    7. spring.thymeleaf.prefix=classpath:/templates/
    8. spring.thymeleaf.suffix=.html 
#### 静态资源与缓存
1. 核心逻辑
    * WebMvcConfigurer.addResourceHandlers()
2. 常用配置
    * spring.mvc.static-path-pattern=/**
    * spring.resource.static-locations=classpath:/META-INF/resources/,classpath:/resources/,classpath:/static/,classpath:/public/
3. spring boot 缓存中的常用配置(默认时间都是秒 )
    * ResourceProperties.Cache
    * spring.resource.cache.cachecontrol.max-age=时间
    * spring.resource.cache.cachecontrol.no-cache=true/false
    * spring.resource.cache.cachecontrol.s-max-age=时间
#### Spring 异常处理机制
1. 核心接口
    * HandlerExceptionResolver
2. 实现类
    * SimpleMappingExceptionResolver
    * DefaultHandlerExceptionResolver
    * ResponseStatusExceptionResolver
    * ExceptionHandlerExceptionResolver
3. 处理方法
    * @ExceptionHandler
4. 添加位置
    * @Controller /@RestController
    * @ControllerAdvice/ @RestControllerAdvice
#### Spring mvc 的拦截器
1. 核心接口
    * HandlerIntercetper
        * boolean preHandler()
        * void postHandler()
        * void afterComplete()
2. 针对@ResponseBody, @ResponseEntity
    * ResponseBodyAdvice
3. 针对异步请求的接口
    * AsyncHandlerInterceptor
        * void afterConcurrentHandlingStarted() 
4. 拦截器的设置方式
    1. 常规方法 
        * WebMvcConfigurer.addInterceptor()
    2. SpringBoot 中的配置
        1. 创建一个 当有@Configuration的WebMvcConfigurer配置类
        2. 不能带@EnableWebMvc（自己配置WebMvc）除外 
### 访问web资源
#### Spring boot 中的RestTemplate
1. Spring boot 中没有自动配置的RestTemplate
2. Spring boot 提供了RestTemplateBuilder
   1. RestTemplateBuilder.build() 
3. RestTemplate 中的常用方法
   1. Get请求
      1. getForObject() 取得对象  /getForEntity() 取得Http响应
      2. `String  result =restTemplate.getForObject("http://xxxx/{hotel}/xxxx/{booking}",String.class,"42","21");` 
   2. Post请求
      1. postForObject()/postForEntity()
   3. Put请求
      1. put()
   4. Delete请求
      1. delete()
4. 构造URI
   1. UriComponentsBuilder
       ``` java 
       Uri uri = UriCompomentBuilder.fromUriString("http://XXXXX/{hotel}").queryParam("q","{q}").encode()
                .buildAndExpand("Westin","123").toUri(); 
       Uri uri = UriCompomentBuilder.fromUriString("http://XXXXX/{hotel}?q={q}").
                .build("Westin","123"); 
       ```
   2. 构造相当与当前求的URI
      1. ServletUriComponentsBuilder
   3. 构造指向Controller的URI
      1. MvcUriComponentsBuilder 
          ```` java 
          UriComponents comp = MvcUriComponentsBuilder.fromMethodCall(on(XXXXController.class).getBooking(21)).buildAndExpand(42)
            URI uri = comp.encode().toUri();
          ````
#### RestTemplate 的高阶用法
1.  传递HttpHeader
    * RestTemplate.exchange()
    * RequestEntity<T>/Response<Entity> 
    ``` java 
        URI uri = UriComponentsBuilder.fromUriString("http://localhost:8080/coffee/?name={name}")
                .build("mocha");
        RequestEntity<Void> requestEntity = RequestEntity.get(uri).accept(MediaType.APPLICATION_XML).build();

        ResponseEntity<String> resp = template.exchange(requestEntity,String.class);
        log.info("Response Status: {}, Response Headers: {}", resp.getStatusCode(), resp.getHeaders().toString());
        log.info("Coffee: {}", resp.getBody());   
    ```
2. 类型转换
    * JsonSerializer/JsonDeserializer
    * @JsonComponent
3. 解析范型
    * RestTemplate.exchange()
    * ParameterizedTypeReference<T>
#### RestTemplate 支持的Http库
* 通用接口
    * ClientHttpRestFactory
* 默认实现
    * SimpleClientRestFactory， 默认使用的都是JDK内嵌的 这种URLConnection之类的一些支持，
* Apache HttpComponents
    * HttpComponentsClientHttpRequestFactory
    ``` java 
    @Bean
    public HttpComponentsClientHttpRequestFactory factory(){
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        manager.setMaxTotal(200);
        manager.setDefaultMaxPerRoute(20);

        CloseableHttpClient httpClient = HttpClients.custom()
                .evictIdleConnections(30, TimeUnit.SECONDS)
                .setConnectionManager(manager)
                .disableAutomaticRetries()
                .setKeepAliveStrategy(new CustomConnectionKeepAliveStrategy())
                .build();
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return factory;

    }
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
       return  builder.setConnectTimeout(Duration.ofMillis(100)).setReadTimeout(Duration.ofMillis(500))
                .requestFactory(this::factory).build();
    }
    ```
* Netty
    * Netty4ClientHttpRequestFactory -> 建议使用HttpClient
* OkHttp
    * OkHttp3ClientHttpRequestFactory
* 优化底层策略
    * 连接管理
        * PoolingHttpClientConnectionManager
        * KeepAlive策略
            ```` java 
            public class CustomConnectionKeepAliveStrategy implements ConnectionKeepAliveStrategy {
                  private final long KEEP_ALIVE_SECONDS = 30;
                
                  @Override
                  public long getKeepAliveDuration(HttpResponse httpResponse, HttpContext httpContext) {
                  return Arrays.asList(httpResponse.getHeaders(HTTP.CONN_KEEP_ALIVE))
                  .stream().filter(h -> StringUtils.equalsIgnoreCase(h.getName(),"timeout"))
                  .findFirst().map(h->NumberUtils.toLong(h.getValue(),KEEP_ALIVE_SECONDS))
                  .orElse(KEEP_ALIVE_SECONDS) * 1000;
                
                  }
            }
            ````
    * 超时设置
        * connectTimeout/readTimeout
    * SSL 校验
        * 证书检查策略

    