# DHMP-DATA-COMMONS
效仿spring-data-commons，该组件涵盖整个从请求到数据返回的流程。
- 统一结果返回
- 统一异常处理
- 分页请求封装（基于PageHelper）
- 统一分页结果返回

## 使用方式
- 安装依赖到本地仓库
`mvn clean install`
- 引入依赖
    ```xml
    <dependency>
        <groupId>com.zznode.dhmp</groupId>
        <artifactId>dhmp-data-commons</artifactId>
        <version>${version}</version>
    </dependency>
    ```
  如果是springboot项目
    ```xml
    <dependency>
        <groupId>com.zznode.dhmp.boot</groupId>
        <artifactId>dhmp-spring-boot-starter-data-commons</artifactId>
        <version>${version}</version>
    </dependency>
    ```

### 统一结果返回
- controller层直接使用`org.springframework.http.ResponseEntity.ok(result)`返回结果
- 任何请求返回到前端状态码都是200
### 统一异常处理
- `GlobalExceptionHandler`会拦截所有从`Controller`接收到的请求处理中抛出的异常，并根据不同的异常返回特定的错误信息。
- 如果有错误信息需要返回前端,请throw new CommonException(ERROR_CODE);
  
  ERROR_CODE是消息编码，定义在`resources/messages/messages.properties`的resources bundle文件。
  也可以是直接的错误消息。

  示例：

  messages.properties
    ```properties
    error.name_empty=名字不能为空
    ```
  constant
    ```java
    interface ErrorCode{
       String NAME_NOT_EMPTY = "error.name_empty";
    }
    ```
  service:
    ```java
     class MyService{
      void test(String name){
          if(name.isEmpty()){
            throw new CommonException(ErrorCode.NAME_NOT_EMPTY);
            //throw new CommonException("名字不能为空");
          }
      }      
    }
    ```
  此时请求会返回
  ```json
    {
      "failed":  true,
      "message":  "名字不能为空"
    }
  ```
### 分页请求


示例

```java
class Controller{
  @GetMapping("/page")
  public ResponseEntity<?> stcsTable(Parameter parameter, PageRequest pageRequest) {
    return ResponseEntity.ok(pageRequest.startPage(() -> service.selectList(parameter)));
  }
}
```
  
  


  