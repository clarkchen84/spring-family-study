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


