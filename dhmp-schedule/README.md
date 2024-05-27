# DHMP-SCHEDULE
定时任务组件，基于quartz开发。
灵活管理定时任务。

使用jdbc管理quartz,在项目

### 开始使用


* 使用前,请将/org/quartz/impl/jdbcjobstore/tables_xx.sql拿出来在数据库中执行一次
* 在配置类中添加`@EnableDhmpScheduling`注解
* 创建jobHandler

    ```java
    @JobHandler(value = "TEST1", name = "测试任务")
    //可以配合ProvinceComponent使用
    @ProvinceComponent(provinces = Province.JI_LIN)
    public class TestTask extends AbstractJobHandler {
        @Override
        protected void runInternal(JobExecutionContext context) {
            logger.info("running test1");
        }
    }
    ```
  注意：需要将这个类注册到spring容器中。

* 在页面中新建任务并配置执行器为这个类。

  或者scheduler_task表中建数据(不推荐)