package spring.boot.contributionmanagement.restful;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import spring.boot.contributionmanagement.entities.Article;
import spring.boot.contributionmanagement.mailService.MailService;
import spring.boot.contributionmanagement.mailService.MailStructure;
import spring.boot.contributionmanagement.services.AcademicYearService;
import spring.boot.contributionmanagement.services.ArticleService;
import spring.boot.contributionmanagement.services.UserService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.nio.file.Paths.get;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@RestController
@RequestMapping("/file")
public class DownloadService {
    private final ArticleService articleService;
    @Autowired
    public DownloadService(ArticleService articleService) {
        this.articleService = articleService;

    }
    public static final String DIRECTORY = System.getProperty("user.home") + "/OneDrive - Phucngocomputer/Desktop/ContributionManagement/src/main/resources/static/wordFiles/";

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFiles(Model model, @PathVariable("fileName") String fileName) throws IOException {
        Path filePath = get(DIRECTORY).toAbsolutePath().normalize().resolve(fileName);

        if (!Files.exists(filePath)){
            throw new FileNotFoundException(fileName + " was not found on the server");
        }
        Resource resource = new UrlResource(filePath.toUri());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("File-Name", fileName);
        httpHeaders.add(CONTENT_DISPOSITION, "attament;File-Name=" + resource.getFilename());

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                .headers(httpHeaders).body(resource);

    }


    @GetMapping("/multi-download")
    public ResponseEntity<Resource> downloadFiles(@RequestParam("fileNames") List<String> fileNames) throws IOException {
        List<Path> filePaths = new ArrayList<>();

        for (String fileName : fileNames){
            Path filePath = get(DIRECTORY).toAbsolutePath().normalize().resolve(fileName);

            if (!Files.exists(filePath)) {
                throw new FileNotFoundException(fileName + " was not found on the server");
            }
            filePaths.add(filePath);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            for (Path filePath : filePaths){
                ZipEntry zipEntry = new ZipEntry(filePath.getFileName().toString());
                zos.putNextEntry(zipEntry);
                Files.copy(filePath, zos);
            }
            zos.closeEntry();
        }catch (IOException e){
            e.printStackTrace();
        }
        ByteArrayResource arrayResource = new ByteArrayResource(baos.toByteArray());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=articles.zip");
        return ResponseEntity.ok().headers(httpHeaders).contentType(MediaType.APPLICATION_OCTET_STREAM).body(arrayResource);
    }





}
