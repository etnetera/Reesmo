package com.etnetera.tremapp.utils.file;

import java.io.InputStream;

/**
 * File container that holds file parameters
 */
public class FileContainer {

    private String fileName;
    private String contentType;
    private InputStream inputStream;
    private long size;

    public FileContainer(String fileName, String contentType, InputStream inputStream, long size) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.inputStream = inputStream;
        this.size = size;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

}
