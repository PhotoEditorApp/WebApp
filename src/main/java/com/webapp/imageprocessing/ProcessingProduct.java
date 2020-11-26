package com.webapp.imageprocessing;

import com.webapp.exceptions.StorageException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public abstract class ProcessingProduct {
    protected final Path rootLocation;

    public ProcessingProduct(Path rootLocation){
        this.rootLocation = rootLocation;
    }

    public String processing(){
        List<String> args = prepareArguments();
        String productPath = getProductPath();
        String script = getScript();

        args.add(0, script);
        if (System.getProperty("os.name").startsWith("Windows"))
            args.add(0, "C:\\python3\\python3.exe");
        else
            args.add(0, "python3");

        args.add(Paths.get(productPath).toAbsolutePath().normalize().toString());
        startProcess(args);

        return productPath;
    }

    abstract String getScript();

    abstract String getProductPath();

    abstract List<String> prepareArguments();

    public void startProcess(List<String> command) throws StorageException {
        try {
            Process process = new ProcessBuilder(command).start();
            String errorStr = new BufferedReader(new InputStreamReader(process.getErrorStream()))
                    .lines()
                    .toString();

            if (!errorStr.contains("java.util.stream.ReferencePipeline"))
                throw new IOException(errorStr);

            process.waitFor();

        } catch (IOException | InterruptedException e) {
            throw new StorageException(e.getMessage());
        }
    }
}
