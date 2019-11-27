package com.dazhao.external.archive;

import com.cunnar.domain.AccessToken;
import com.cunnar.domain.FileInfo;
import com.cunnar.module.Account;
import com.cunnar.module.Oauth;
import com.dazhao.common.utils.AccessNumberUtil;
import com.dazhao.external.archive.WitnessCloudService;
import com.dazhao.pojo.mapper.MaterialMapper;
import com.dazhao.pojo.vo.MaterialGroupVO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WitnessCloudServiceImpl implements WitnessCloudService {

    @Value("${cunnar.userId}")
    private String cunnarUserId;

    @Value("${cunnar.temporary-files-path}")
    private String temporaryFilesPath;

    private static final String EXCEL_SUFFIX = ".xlsx";

    @Autowired
    private MaterialMapper materialMapper;

    @Override
    public String createUser(String outId) {
        return Account.createAccountById(outId);
    }

    /**
     * 上传云存证
     */
    @Async
    @Override
    public void uploadFileExcel() {
        File cacheDir = new File(temporaryFilesPath);
        if (!cacheDir.isDirectory()) {
            cacheDir.mkdirs();
        }
        StringBuilder stringBuilder = new StringBuilder();
        String fileName = stringBuilder.append(temporaryFilesPath)
                .append(AccessNumberUtil.getFilePerfixNumber())
                .append(EXCEL_SUFFIX)
                .toString();
        createWitnessExcel(fileName);
        File file = new File(fileName);
        AccessToken accessToken = getAccessToken();
        try {
            // 创建文件信息获取文件唯一标识
            String fileId = createFile(accessToken, file, String.valueOf(System.currentTimeMillis()));
            // 上传文件
            uploadFile(accessToken, fileId, file);
            // 删除临时文件
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            log.error("上传存在云异常", e);
        }


    }

//    /**
//     * 下载存在信息
//     */
//    private void downloadFile(AccessToken accessToken, String fileId, final String filePath) {
//        com.cunnar.module.File.downloadFile(accessToken, fileId, new InputStreamCallBack() {
//            @Override
//            public void doInputStream(FileStream fileStream) throws IOException {
//                InputStream inputStream = fileStream.getInputStream();
//                FileOutputStream fileOutputStream = new FileOutputStream(filePath);
//                int i;
//                byte[] bytes = new byte[4096];
//                while ((i = inputStream.read(bytes)) > 0) {
//                    fileOutputStream.write(bytes, 0, i);
//                }
//                fileOutputStream.flush();
//                fileOutputStream.close();
//            }
//        });
//
//
//    }

    /**
     * 获取AccessToken
     *
     * @return accessToken
     */
    private AccessToken getAccessToken() {
        return Oauth.getAccessToken(cunnarUserId);
    }

    /**
     * 创建文件信息
     *
     * @param accessToken token
     * @param file 文件对象
     * @param id 接入方系统中文件的唯一标识
     */
    private String createFile(AccessToken accessToken, File file, String id) {
        String fileId = null;
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            FileInfo info = new FileInfo(id, file.getName(), file.length(), DigestUtils.shaHex(fileInputStream));
            fileId = com.cunnar.module.File.create(accessToken, info);
        } catch (IOException e) {
            log.error("创建文件信息异常", e);
        }
        return fileId;
    }

    /**
     * 上传文件
     *
     * @param accessToken token
     * @param fileId 文件唯一标识
     */
    private void uploadFile(AccessToken accessToken, String fileId, File file) {
        long uploadLength = com.cunnar.module.File.getFileUploadLength(accessToken, fileId);
        // 上传文件
        if (uploadLength < file.length()) {
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                com.cunnar.module.File.uploadStream(accessToken, fileId, uploadLength, fileInputStream);
            } catch (Exception e) {
                log.error("上传存在云异常", e);
            }
        }
    }

    /**
     * 生成上传文件
     *
     * @param fileName 文件名路径
     * @return 文件路径
     */
    private void createWitnessExcel(String fileName) {
        List<MaterialGroupVO> materialList = materialMapper.queryExistMaterialGroup(null, null);

        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName); XSSFWorkbook workbook = new XSSFWorkbook()) {
            // 创建一个sheet
            XSSFSheet sheet = workbook.createSheet();
            XSSFRow headRow = sheet.createRow(0);
            String[] headValue = {"物资类型", "物资名称", "计量单位", "型号", "数量", "仓位", "备注"};
            for (int i = 0; i < headValue.length; i++) {
                headRow.createCell(i).setCellValue(headValue[i]);
            }
            int rowNumber = 1;
            for (MaterialGroupVO materialGroupVO : materialList) {
                XSSFRow row = sheet.createRow(rowNumber);
                row.createCell(0).setCellValue(materialGroupVO.getSortName());
                row.createCell(1).setCellValue(materialGroupVO.getMaterialName());
                row.createCell(2).setCellValue(materialGroupVO.getMaterialUnit());
                row.createCell(3).setCellValue(materialGroupVO.getMaterialType());
                row.createCell(4).setCellValue(materialGroupVO.getTotal());
                row.createCell(5).setCellValue(materialGroupVO.getLocationNumber());
                row.createCell(6).setCellValue(materialGroupVO.getMaterialComment());
                rowNumber++;
            }
            workbook.write(fileOutputStream);
            fileOutputStream.flush();
        } catch (IOException e) {
            log.error("生成上传文件导出失败", e);
        }
    }
}
