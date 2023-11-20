package org.ucsc.enmoskill.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Iterator;

import io.github.cdimascio.dotenv.Dotenv;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

public class FileHandlerControler extends HttpServlet {
    Dotenv dotenv = Dotenv.load();
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (ServletFileUpload.isMultipartContent(request)) {

            DiskFileItemFactory factory = new DiskFileItemFactory();


            ServletFileUpload upload = new ServletFileUpload(factory);

            try {

                List<FileItem> items = upload.parseRequest(request);


                Iterator<FileItem> iter = items.iterator();
                while (iter.hasNext()) {
                    FileItem item = iter.next();


                    if (!item.isFormField()) {

                        InputStream fileContent = item.getInputStream();

//                        Date date=new Date();
//                        System.out.println(date.getTime());
                        String fileName = new Date().getTime()+item.getName();
                        String ACCKEY = dotenv.get("AWS_ACCESS_KEY_ID");
                        String SECKEY = dotenv.get("AWS_SECRET_ACCESS_KEY");

                        S3Client s3Client = S3Client.builder()
                                .region(Region.US_EAST_1)
                                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(
                                        ACCKEY, SECKEY
                                )))
                                .build();


                        String objectKey = "profile_pics/" + fileName;


                        s3Client.putObject(PutObjectRequest.builder()
                                .bucket("enmoskillbucket")
                                .key(objectKey)
                                .build(), RequestBody.fromInputStream(fileContent, item.getSize()));


                        String fileUrl = s3Client.utilities().getUrl(GetUrlRequest.builder()
                                .bucket("enmoskillbucket")
                                .key(objectKey)
                                .build()).toExternalForm();


                        response.getWriter().write(fileUrl);
                        return;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.getWriter().write("Error occurred while uploading the file.");
            }
        } else {
            response.getWriter().write("Invalid request: Not a multipart request.");
        }
    }
}