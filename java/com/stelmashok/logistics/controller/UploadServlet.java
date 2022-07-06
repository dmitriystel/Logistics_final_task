package com.stelmashok.logistics.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// import static com.stakhiyevich.openadboard.controller.command.RequestParameterHolder.UPLOAD_FILE_PATH;
public class UploadServlet {
    @MultipartConfig(
            fileSizeThreshold = 1024 * 1024 * 2,
            maxFileSize = 1024 * 1024 * 10,
            maxRequestSize = 1024 * 1024 * 100
    )
    @WebServlet(name = "UploadServlet", urlPatterns = "/upload")
    public class UploadServlet extends HttpServlet {
        private static final Logger logger = LogManager.getLogger();

        private static final String UPLOAD_PROPERTIES = "config/upload.properties";
        private static final String UPLOAD_DIRECTORY = "upload.directory";
        private String uploadDirectory;

        @Override
        public void init() throws ServletException {
            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(UPLOAD_PROPERTIES)) {
                Properties properties = new Properties();
                properties.load(inputStream);
                uploadDirectory = properties.getProperty(UPLOAD_DIRECTORY);
            } catch (IOException e) {
                logger.error("failed to load the upload resources file");
            }
        }

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String uploadFileName = req.getParameter(UPLOAD_FILE_PATH);
            if (uploadFileName == null) {
                logger.error("failed to find a file path request parameter");
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            String uploadFilePath = uploadDirectory + uploadFileName;
            try (InputStream inputStream = new FileInputStream(uploadFilePath)) {
                ServletOutputStream outputStream = resp.getOutputStream();
                inputStream.transferTo(outputStream);
            } catch (IOException e) {
                logger.error("failed to upload a file {}", uploadFileName);
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }
}
