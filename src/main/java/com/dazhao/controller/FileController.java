package com.dazhao.controller;

import static org.openxmlformats.schemas.spreadsheetml.x2006.main.STBorderStyle.type;

import com.dazhao.common.result.CommonResult;
import com.dazhao.common.result.ErrorCodeEnum;
import com.dazhao.common.result.ResponseUtil;
import com.dazhao.common.utils.AccessNumberUtil;
import com.dazhao.common.utils.Sha256Util;
import com.dazhao.pojo.bo.SignatureBO;
import com.dazhao.service.BillService;
import com.dazhao.service.StocktakingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.util.RequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

@RestController
@RequestMapping(value = "/file_upload")
@Api(description = "文件相关")
@Slf4j
public class FileController {

    @Value("${web.upload-path}")
    private String uplodPath;

    @Autowired
    private BillService billService;
    @Autowired
    private StocktakingService stocktakingService;

    private static final String PICTURE_PATH_SEPARATOR = "images/";
    // 允许上传的格式
    private static final String[] IMAGE_TYPE = new String[]{".bmp", ".jpg", ".jpeg", ".gif", ".png"};

    private static final int IMAGE_MAX = 3 * 1024 * 1024;

    private static Integer STOCKTAKING_FLAG = 2;
    private static Integer Bill_FLAG = 1;
    private static String BASE_PREFFIX = "data:";
    private static String SINGTURE_DEFAULT_PATH = "xx.png";

    @PostMapping(value = "/image")
    @ApiOperation(value = "图片上传")
    public CommonResult uploadImg(@RequestParam("file_img") MultipartFile file, HttpServletRequest request) {
        if (file == null) {
            return ResponseUtil.badArgument();
        }
        if (file.getSize() >= IMAGE_MAX) {
            return ResponseUtil.fail("-1", "图片大小不能超过3M");
        }
        boolean isLegal = isImage(file);
        if (!isLegal) {
            return ResponseUtil.fail(ErrorCodeEnum.IMAGE_UPLOAD_TYPE.getErrorCode(), ErrorCodeEnum.IMAGE_UPLOAD_TYPE.getErrorMsg());
        }

        String filename = getFilePath(file.getOriginalFilename());
        String urlPath = getUrlPath(filename, request);
        File dest = new File(filename);
        try {
            file.transferTo(dest);
            return ResponseUtil.ok(urlPath);
        } catch (IOException e) {
            log.error(ErrorCodeEnum.IMAGE_UPLOAD_DEFEATED.getErrorMsg(), e);
        }
        return ResponseUtil.fail(ErrorCodeEnum.IMAGE_UPLOAD_DEFEATED.getErrorCode(), ErrorCodeEnum.IMAGE_UPLOAD_DEFEATED.getErrorMsg());

    }

    @DeleteMapping("/image")
    @ApiOperation(value = "删除单据图片及图片信息", notes = "图片路径")
    public CommonResult deleteBillUploadPath(@RequestParam(value = "image_url") String imageUrl) {

        String picturePath =
                uplodPath + imageUrl.substring(imageUrl.indexOf(PICTURE_PATH_SEPARATOR)).replace(PICTURE_PATH_SEPARATOR, "");
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


    @PostMapping(value = "/signature_image")
    @ApiOperation(value = "数字签名图片上传", notes = "type=1:单据签名；type=2:盘点签名")
    public CommonResult uploadSignatureImg(@Validated @RequestBody SignatureBO signatureBO,
            HttpServletRequest request) {
        if (!signatureBO.getImageBase64().startsWith(BASE_PREFFIX)) {
            return ResponseUtil.badArgument();
        }
        String filename = getFilePath(SINGTURE_DEFAULT_PATH);
        boolean isOk = base64ToImage(filename, signatureBO.getImageBase64());
        if (!isOk) {
            return ResponseUtil.fail();
        }
        String urlPath = getUrlPath(filename, request);
        String signatureHash = getSignature(filename);
        if (StringUtils.isEmpty(signatureHash)) {
            return ResponseUtil.fail(ErrorCodeEnum.SIGNATURE_FAILE.getErrorCode(), ErrorCodeEnum.SIGNATURE_FAILE.getErrorMsg());
        }
        // 修改单据信息
        if (STOCKTAKING_FLAG.equals(signatureBO.getType())) {
            stocktakingService.uploadStocktakingSignature(signatureHash, urlPath, signatureBO.getBillId());
        } else if (Bill_FLAG.equals(signatureBO.getType())) {
            billService.uploadBillSignature(signatureHash, urlPath, signatureBO.getBillId());
        } else {
            return ResponseUtil.badArgument();
        }
        return ResponseUtil.ok(urlPath);

    }

    /**
     * 判断是否图片
     *
     * @param file 上传文件
     */
    private boolean isImage(MultipartFile file) {
        boolean isLegal = false;
        for (String type : IMAGE_TYPE) {
            if (StringUtils.endsWithIgnoreCase(file.getOriginalFilename(), type)) {
                isLegal = true;
                break;
            }
        }
        return isLegal;
    }

    /**
     * 生成图片访问地址
     *
     * @param filename 文件名称
     * @param request 请求对象
     * @return string
     */
    private String getUrlPath(String filename, HttpServletRequest request) {
        StringBuilder urlPath = new StringBuilder();
        urlPath.append(StringUtils.replace(request.getRequestURL().toString(), request.getServletPath(), ""))
                .append("/")
                .append("images")
                .append("/")
                .append(StringUtils.difference(uplodPath, filename).replace("\\", "/"));
        return urlPath.toString();
    }

    /**
     * 获取文件地址
     *
     * @param sourceFileName 上传文件名称
     */
    private String getFilePath(String sourceFileName) {
        StringBuilder fileFolder = new StringBuilder();
        Date nowDate = new Date();
        fileFolder.append(uplodPath)
                .append(new DateTime(nowDate).toString("yyyy"))
                .append(File.separator)
                .append(new DateTime(nowDate).toString("MM"))
                .append(File.separator + new DateTime(nowDate).toString("dd"));
        File file = new File(fileFolder.toString());
        if (!file.isDirectory()) {
            file.mkdirs();
        }
        StringBuilder fileName = new StringBuilder()
                .append(AccessNumberUtil.getFilePerfixNumber())
                .append(".")
                .append(StringUtils.substringAfterLast(sourceFileName, "."));
        return fileFolder.toString() + File.separator + fileName.toString();
    }


    /**
     * 获取图片文件签名sha-256哈希值
     *
     * @param fileName 上传文件路径
     * @return 图片文件签名sha-256哈希值
     */
    private String getSignature(String fileName) {
        File file = new File(fileName);
        byte[] data = new byte[(int) file.length()];
        String signatureHash;
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(data);
            signatureHash = Sha256Util.signatureImageHash(data);
            if (StringUtils.isEmpty(signatureHash)) {
                return null;
            }
        } catch (IOException e) {
            log.info("签署请求失败:" + e.getMessage(), e);
            return null;
        }
        return signatureHash;
    }

    /**
     * base64转图片写入硬盘
     *
     * @param filename 文件名称
     */
    private boolean base64ToImage(String filename, String imageBase64) {
        BASE64Decoder decoder = new BASE64Decoder();
        try (OutputStream out = new FileOutputStream(filename)) {
            byte[] b = decoder.decodeBuffer(imageBase64.substring(imageBase64.indexOf(",") + 1));
            for (int i = 0; i < b.length; ++i) {
                //调整异常数据
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            out.write(b);
            out.flush();
            return true;
        } catch (IOException e) {
            log.error("签名图片base64转图片失败" + e.getMessage(), e);
            return false;
        }
    }
}
