# DHMP-CONTEXT

spring的bean相关操作。依赖dhmp-core。

使用`@ProvinceComponent`标记组件可以让

## 使用方式

- 安装依赖到本地仓库
  `mvn clean install`
- 引入依赖
    ```xml
    <dependency>
        <groupId>com.zznode.dhmp</groupId>
        <artifactId>dhmp-context</artifactId>
        <version>${version}</version>
    </dependency>
    ```

### 方式1

springboot项目中。

- 引入依赖
    ```xml
    <dependency>
        <groupId>com.zznode.dhmp.boot</groupId>
        <artifactId>dhmp-spring-boot</artifactId>
        <version>${version}</version>
    </dependency>
    ```

- 将`@ProvinceComponentScan`标记在启动类上。

  如果标记在有`@Configuration`注解的类上则必须指定basePackages属性。
- 不使用`@ProvinceComponentScan`注解标记，会自动扫描自动配置类下的所有组件

注意
- `@ProvinceComponent`注解适用于扫描bean。
- 如果是在配置类中定义一些bean时需要省份个性化，则需要使用到`@ConditionalOnProvince`


