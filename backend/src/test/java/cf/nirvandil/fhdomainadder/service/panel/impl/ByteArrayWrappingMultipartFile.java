package cf.nirvandil.fhdomainadder.service.panel.impl;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Random;

public class ByteArrayWrappingMultipartFile implements MultipartFile {
    private byte[] data;
    private final Random random = new Random(System.currentTimeMillis());

    public ByteArrayWrappingMultipartFile(byte[] data) {
        this.data = data;
    }

    @Override
    public String getName() {
        return "wrapped_file_" + random.nextInt() + ".php";
    }

    @Override
    public String getOriginalFilename() {
        return getName();
    }

    @Override
    public String getContentType() {
        return MediaType.APPLICATION_OCTET_STREAM_VALUE;
    }

    @Override
    public boolean isEmpty() {
        return data == null || data.length == 0;
    }

    @Override
    public long getSize() {
        return data.length;
    }

    @Override
    public byte[] getBytes() {
        return data;
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(data);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        try (FileOutputStream outputStream = new FileOutputStream(dest)) {
            outputStream.write(data);
        }
    }
}
