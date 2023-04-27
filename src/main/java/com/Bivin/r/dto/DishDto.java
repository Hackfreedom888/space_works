package com.Bivin.r.dto;

import com.Bivin.r.pojo.Dish;
import com.Bivin.r.pojo.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;


@Data   // lombok
public class DishDto extends Dish {


    private String categoryName;


    private List<DishFlavor> flavors = new ArrayList<>();

    private Integer copies;

}
