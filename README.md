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
       public PlatformTransactionManager barTxManager(@Qualifier("barDataSource") DataSource barDataSource) {
           return new DataSourceTransactionManager(barDataSource);
       }
   
       @Bean
       public PlatformTransactionManager fooTxManager(@Qualifier("fooDataSource")DataSource fooDataSource) {
           return new DataSourceTransactionManager(fooDataSource);
       }
   }

   ```



