package com.Bivin.r.dto;

import com.Bivin.r.pojo.Setmeal;
import com.Bivin.r.pojo.SetmealDish;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {


    private List<SetmealDish> setmealDishes =new ArrayList<>();

    private String categoryName;
}
