# Movie_Discussion
Base on SSM

### 操作记录

##### 2021/10/28      搭建SSM框架

##### 2021/10/29     用户注册和登录实现
今日遗留问题：Ajax 传输到后台的内容，如何自动装配到Controller的实体类参数中？
其余的还没有需要问题，都是比较基础的CRUD

##### 2021/10/30   （ 周六一整天 ）实现大部分前台&后台CRUD内容 存在的错误如下，需要巩固的内容如下
内容1：初始值1含义
Map<String, String> map=new HashMap<>(1);

```
/**
     * Constructs an empty <tt>HashMap</tt> with the specified initial
     * capacity and the default load factor (0.75).
     *
     * @param  initialCapacity the initial capacity.
     * @throws IllegalArgumentException if the initial capacity is negative.
     * 这个1被称为初始容量也叫加载因子|    |加载因子越高   空间利用率提高了  但是查询时间 和添加时间增加
     */
    public HashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }
```

内容2：JSP 500错误
````
org.apache.jasper.JasperException: Unable to compile class for JSP: 

An error occurred at line: [16] in the generated java file:xxxxx
````
原因：pom中jsp的jar包冲突导致，也有可能是jsp引入java文件路径失败

内容3：RequestMapping中用了$符号 500错误（大意失荆州！！）
```$xslt
java.lang.IllegalStateException: Invalid mapping on handler class [com.suki.controller.AdminCotroller]: 
public org.springframework.web.servlet.ModelAndView com.suki.controller.AdminCotroller.editCategoryView(java.lang.Integer)
```

内容3：拦截器使用练习
```xml
<mvc:interceptors>
        <mvc:interceptor>
            <!--初始页面的拦截器   加载初始页面的所需资源-->
            <mvc:mapping path="/**/"/>
            <bean class="com.suki.interceptor.StartInterceptor"/>
        </mvc:interceptor>
</mvc:interceptors>
```

<span style='color:red'>明天周日完成前端修改，文件上传，分页操作</span>

<span style='color:red'>以及研究生阶段之前完成的软著推荐系统前端页面连入！</span>

<span style='color:blue'>以及研究生阶段之前完成的软著推荐系统前端页面</span>

#### 研究生阶段的算法后台，java实现推荐后台以及前台不上传代码，因为还没毕业，不公开



##### 2021/10/31  文件上传复习
后台：
```java
@RestController
public class FileIOController {
    public final String rootPath = "E:\\MySSMProject\\MyMovieSystem\\MovieSys\\web\\resource\\assets\\img\\avatar";
    public final String allowSuffix = ".bmp.jpg.jpeg.png.gif.pdf.doc.zip.rar.gz";
    @RequestMapping(value = "/admin/upload/img", method = RequestMethod.POST)
    public JsonResult uploadFile(@RequestParam("file") MultipartFile file) {
        //1.文件后缀过滤，只允许部分后缀
        //文件的完整名称,如spring.jpeg
        String filename = file.getOriginalFilename();
        //文件名,如spring
        String name = filename.substring(0, filename.indexOf("."));
        //文件后缀,如.jpeg
        String suffix = filename.substring(filename.lastIndexOf("."));

        if (allowSuffix.indexOf(suffix) == -1) {
            return new JsonResult().fail("不允许上传该后缀的文件！");
        }
        //2.创建文件目录
        //创建年月文件夹
        Calendar date = Calendar.getInstance();
        File dateDirs = new File(date.get(Calendar.YEAR)
                + File.separator + (date.get(Calendar.MONTH) + 1));
        //目标文件
        File descFile = new File(rootPath + File.separator + dateDirs + File.separator + filename);
        int i = 1;
        //若文件存在重命名
        String newFilename = filename;
        while (descFile.exists()) {
            newFilename = name + "(" + i + ")" + suffix;
            String parentPath = descFile.getParent();
            descFile = new File(parentPath + File.separator + newFilename);
            i++;
        }
        //判断目标文件所在的目录是否存在
        if (!descFile.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            descFile.getParentFile().mkdirs();
        }
        //3.存储文件
        //将内存中的数据写入磁盘
        try {
            file.transferTo(descFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //完整的url
        String fileUrl = "/img/avatar/" + dateDirs + "/" + newFilename;
        //4.返回URL
        UploadFileVO uploadFileVO = new UploadFileVO();
        uploadFileVO.setTitle(filename);
        uploadFileVO.setSrc(fileUrl);
        return new JsonResult().ok(uploadFileVO);
    }
}
```
前台：
```js
  <script>
        //上传图片
        layui.use('upload', function () {
            var $ = layui.jquery,
                upload = layui.upload;
            var uploadInst = upload.render({
                elem: '#test1',
                url: '/admin/upload/img',
                before: function (obj) {
                    obj.preview(function (index, file, result) {
                        $('#demo1').attr('src', result);
                    });
                },
                done: function (res) {
                    $("#userAvatar").attr("value", res.data.src);
                    if (res.code > 0) {
                        return layer.msg('上传失败');
                    }
                },
                error: function () {
                    var demoText = $('#demoText');
                    demoText.html('' +
                        '<span style="color: #FF5722;">上传失败</span>' +
                        ' <a class="layui-btn layui-btn-mini demo-reload">重试</a>');
                    demoText.find('.demo-reload').on('click', function () {
                        uploadInst.upload();
                    });
                }
            });

        });
    </script>
```
mvc配置文件：
```xml
 <bean id="multipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver">
        <property name="maxUploadSizePerFile" value="52428800"/>
        <property name="defaultEncoding" value="UTF-8"/>
        <property name="resolveLazily" value="true"/>
 </bean>
```
## 遇到的问题：单纯上传文件是不会立即生效的，需要重启服务器才能
## 解决方法： 修改Tomcat的虚拟路径 ---> IDEA在tomcat配置中把上传的路径加入到项目中即可
