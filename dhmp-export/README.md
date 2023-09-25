# 通用导出组件

## 使用方式

- 创建导出实体类
  ```java
  @EqualsAndHashCode(callSuper = true)
  @TableName("demo_goods")
  @Data
  @ReportSheet(value = "测试", suffix = @ReportSheet.Suffix(suffixType = ReportSheet.SuffixType.DATE))
  public class Product extends BaseEntity {
      @ReportColumn(value = "id", pattern = "##")
      private Long id;
  
      @ReportColumn(value = "产品名称")
      private String goodsName;
  
      @ReportColumn(value = "单价")
      private Double price;
  
      @TableField("produce_province")
      @ReportColumn(value = "产地",
              enumConvert = @ReportColumn.EnumConvert(enumClass = Province.class, enumGetName = "provinceCode", enumValueName = "provinceName"))
      private String produceProvince;
  
      @ReportColumn(value = "单位", lovCode = "PRICE_UNIT")
      private Integer priceUnit;
  
      @ReportColumn(value = "创建时间", pattern = BaseConstants.Pattern.DATETIME)
      @TableField(FIELD_CREATION_DATE)
      private Date createdDate;
  
      @ReportColumn(value = "上架时间", pattern = BaseConstants.Pattern.DATETIME)
      @TableField(FIELD_LAST_UPDATE_DATE)
      private LocalDateTime lastUpdate;
      /**
       * 模拟报错
       */
      @ReportColumn(value = "上架时间2", pattern = BaseConstants.Pattern.DATETIME)
      @TableField(FIELD_LAST_UPDATE_DATE)
      private LocalDate lastUpdatedDate;
  
  }
  ```
- 导出接口使用
    ```java
    @Controller
    class Controller {
        @GetMapping("/export")
        @Export(Product.class)
        public ResponseEntity<List<Product>> exportContext(ExportContext exportContext) {
            return ResponseEntity.ok(productMapper.selectList(Wrappers.emptyWrapper()));
        }
    }
    ```
  * 接口方法使用com.zznode.dhmp.export.annotation.Export注解标注,value值为导出的实体类
  * 方法参数必需有一个com.zznode.dhmp.export.ExportContext类型的参数(即使业务上不会使用)
- 前端请求
  ```text
  /export-context?requestType=DATA&type=CSV&ids=1,2,3,4,5,6,7
  ```
  

