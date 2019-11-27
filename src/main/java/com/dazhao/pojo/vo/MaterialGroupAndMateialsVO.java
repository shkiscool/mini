package com.dazhao.pojo.vo;

import java.util.List;
import lombok.Data;

@Data
public class MaterialGroupAndMateialsVO extends MaterialGroupVO {

    private List<BorrowMaterialVO> materials;
}
