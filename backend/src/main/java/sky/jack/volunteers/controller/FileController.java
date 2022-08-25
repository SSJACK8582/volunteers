package sky.jack.volunteers.controller;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sky.jack.volunteers.tool.AjaxResult;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.UUID;

@CrossOrigin
@RestController
public class FileController {
    private static final String FILE_PATH = System.getProperty("user.dir") + File.separator + "upload";

    @PostMapping("/upload")
    public AjaxResult upload(@RequestParam("file") MultipartFile file) {
        String fileName = writeFile(file);
        if (fileName != null) {
            return AjaxResult.success(fileName);
        } else {
            return AjaxResult.error();
        }
    }

    @GetMapping("/download")
    public void download(@RequestParam("fileName") String fileName, HttpServletResponse response) {
        File file = new File(FILE_PATH, fileName);
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                response.setContentType("application/force-download");
                response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                ServletOutputStream os = response.getOutputStream();
                FileCopyUtils.copy(fis, os);
                os.flush();
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping("image/{fileName}")
    public void getImage(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        File file = new File(FILE_PATH, fileName);
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                response.setContentType("image/jpeg;charset=utf-8");
                response.setHeader("content-disposition", "inline;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                ServletOutputStream os = response.getOutputStream();
                FileCopyUtils.copy(fis, os);
                os.flush();
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String writeFile(MultipartFile file) {
        String oldName = file.getOriginalFilename();
        String fileType = oldName.substring(oldName.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + fileType;
        File newFile = new File(FILE_PATH, fileName);
        if (!newFile.getParentFile().exists())
            newFile.getParentFile().mkdirs();
        try {
            file.transferTo(newFile);
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
