# DHMP-EXPORT

数据导出组件，使用依赖spring-aop、apache-poi。

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
  
  }
  ```
- service层
  ```java
  
  public interface ProductService {
      List<Product> list();
  }
  
  @Service
  public class ProductServiceImpl implements ProductService {
  
      /**
       * 多层调用链都使用了@Export注解，不影响功能正常使用
       */
      @Export(Product.class)
      @Overried
      public List<Product> list() {
        return Collections.emptyList();
      }
  }
  
  ```


- 导出接口使用
    ```java
    @Controller
    @RequestMapping("/product")
    public class ProductController {
        private final ProductService productService;
        
        public ProductController(ProductService productService) {
            this.productService = productService;
        }
  
        @GetMapping("/export")
        @Export(Product.class)
        public ResponseEntity<List<Product>> export1() {
            return ResponseEntity.ok(productService.list());
        }
  
        @GetMapping("/export2")
        public void export2() {
            // 此处service层标记了导出注解，也可实现导出
            productService.list();
        }
    }
    ```
  * 接口方法使用com.zznode.dhmp.export.annotation.Export注解标注,value值为导出的实体类。
  * Export注解也可标记在下层组件方法上，被Export注解标记的方法返回类型必须是Iterable<T>，T为导出的实体类。此时controller层方法可以返回void。
- 前端请求
  ```text
  localhost:8080/product/export?type=CSV&columns=goodsName,priceUnit,createdDate
  ```
  

