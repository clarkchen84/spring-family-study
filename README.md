### git 常用命令
1. git remote set-head origin master 设置远程git仓库的head
2. git remote add origin git@github.com:*****
将本地工程挂载到远程仓库 

### 关于使用maven 打jar 包的命令
1. 使用 mvn clean install -Dmaven.test.skip
2. 如果springboot 项目通过上面的命令打包需要注意下面的事项
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
### Spring boot jdbc
#### spring boot 为jdbc 做了哪些配置
1. DataSourceAutoConfiguration
   * 配置DataSource
2. DataSourceTransactionManagerAutoConfiguration
   * 配置DataSourceTransactionManager
3. JdbcTemplateAutoConfiguration
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

        <jdbcConnection driverClass="org.h2.Driver"
                        connectionURL="jdbc:h2:mem:testdb"
                        userId="sa"
                        password="">
        </jdbcConnection>

        <javaModelGenerator targetPackage="sizhe.chen.spring.data.mybatis.demo.model"
                            targetProject="./src/main/java">
            <property name="enableSubPackages" value="true" />
            <property name="trimStrings" value="true" />
        </javaModelGenerator>

        <sqlMapGenerator targetPackage="sizhe.chen.spring.data.mybatis.demo.mapper"
                         targetProject="./src/main/java">
            <property name="enableSubPackages" value="true" />
        </sqlMapGenerator>

        <javaClientGenerator type="MIXEDMAPPER"
                             targetPackage="sizhe.chen.spring.data.mybatis.demo.mapper"
                             targetProject="./src/main/java">
            <property name="enableSubPackages" value="true" />
        </javaClientGenerator>

        <table tableName="t_coffee" domainObjectName="Coffee" >
            <generatedKey column="id" sqlStatement="CALL IDENTITY()" identity="true" />
            <columnOverride column="price" javaType="org.joda.money.Money" jdbcType="BIGINT"
                            typeHandler="MoneyTypeHandler"/>
        </table>
    </context>
</generatorConfiguration>
//http://www.mybatis.org/generator/configreference/generatedKey.html
```