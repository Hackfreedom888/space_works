package com.Bivin.r.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data   // 简化实体类的 getter 、setter、toString方法注解
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String name;

    @TableField(fill = FieldFill.INSERT)
    private String password;

    private String phone;

    private String sex;

    private String idNumber;

    private Integer status;


    @TableField(fill = FieldFill.INSERT)  // 配上FieldFill.INSERT 意味着 当客户端访问添加功能并且客户端没有为该属性传数据的时候，就用mybatis-plus自动填充该数据
    private LocalDateTime createTime; // 该属性字段对应的数据库中的create_time 创建时间字段（创建时间在添加的时候填充一次就可以了，修改的时候不用再填充了）

    @TableField(fill = FieldFill.INSERT_UPDATE) // 配成FieldFill.INSERT_UPDATE 意思是：当客户端访问添加功能或者修改功能的时候，都会为该对应的数据库中的字段填充值
                                                // （当是修改功能的时候，代表的就不是赋值了，代表的是修改值，也就是说把以前数据库中对应的该字段的数据覆盖掉重新赋上我们定义的值）。
    private LocalDateTime updateTime; // 对应的是数据库表中修改时间字段

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;    // 对应的是数据库表中的创建人字段

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;    // 对应的是数据库表中的修改用户字段

}
