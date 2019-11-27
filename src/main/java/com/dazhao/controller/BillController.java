package com.dazhao.controller;

import com.dazhao.common.result.CommonResult;
import com.dazhao.common.result.ResponseUtil;
import com.dazhao.common.utils.IpAdrressUtil;
import com.dazhao.common.utils.JWTUtil;
import com.dazhao.pojo.dao.Bill;
import com.dazhao.service.BillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import java.io.File;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description = "单据通用接口")
@RequestMapping("/bill")
public class BillController {

    @Autowired
    private BillService billService;

    private static final String PICTURE_PATH_SEPARATOR = "images/";

    @Value("${web.upload-path}")
    private String uplodPath;


    @GetMapping("/signature_bill")
    @ApiOperation(value = "获取需要签名的单据")
    public CommonResult needSignatureBill() {
        List<Bill> bills = billService.needSignatureBill();
        return ResponseUtil.okList(bills);
    }

    @PutMapping("/image")
    @ApiOperation(value = "单据补传单据更新单据地址", notes = "只需要这两个字段{\"id\":1,\"voucherPicturePath\":\"xxxx\"}")
    public CommonResult updateBillUploadPath(@RequestBody Bill bill, HttpServletRequest request) {
        int userId = JWTUtil.getUserId(request);
        String ipAddress = IpAdrressUtil.getIpAdrress(request);
        if (bill.getId() == null || bill.getVoucherPicturePath() == null) {
            return ResponseUtil.badArgument();
        }
        CommonResult commonResult = billService.updateBillUploadPath(bill, userId, ipAddress);
        if (!commonResult.isSuccess()) {
            String picturePath =
                    uplodPath + bill.getVoucherPicturePath().substring(bill.getVoucherPicturePath().indexOf(PICTURE_PATH_SEPARATOR))
                            .replace(PICTURE_PATH_SEPARATOR, "");
            File file = new File(picturePath);
            if (file.exists()) {
                file.delete();
            }
        }
        return commonResult;
    }

    @DeleteMapping("/image")
    @ApiOperation(value = "删除单据图片及图片信息", notes = "只需要这两个字段{\"id\":1,\"voucherPicturePath\":\"xxxx\"}")
    public CommonResult deleteBillUploadPath(@RequestBody Bill bill, HttpServletRequest request) {
        int userId = JWTUtil.getUserId(request);
        String ipAddress = IpAdrressUtil.getIpAdrress(request);
        if (bill.getId() == null || bill.getVoucherPicturePath() == null) {
            return ResponseUtil.badArgument();
        }
        String voucherPicturePath = bill.getVoucherPicturePath();
        boolean isSuccess = billService.deleteBillUploadPath(bill, userId, ipAddress);
        if (!isSuccess) {
            return ResponseUtil.fail("-1", "删除失败");
        }
        if (!voucherPicturePath.contains(PICTURE_PATH_SEPARATOR)) {
            return ResponseUtil.fail("-1", "上传图片地址错误");
        }
        String picturePath =
                uplodPath + voucherPicturePath.substring(voucherPicturePath.indexOf(PICTURE_PATH_SEPARATOR)).replace(PICTURE_PATH_SEPARATOR, "");
        File file = new File(picturePath);
        if (file.exists()) {
            if (file.delete()) {
                return ResponseUtil.ok("删除成功");
            } else {
                return ResponseUtil.fail("-1", "删除失败");
            }
        } else {
            return ResponseUtil.fail("-1", "文件不存在！");
        }

    }
}
