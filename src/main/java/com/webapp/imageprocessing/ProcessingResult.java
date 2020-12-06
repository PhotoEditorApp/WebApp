package com.webapp.imageprocessing;

import com.webapp.exceptions.StorageException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ProcessingResult {
    protected final Path rootLocation;

    public ProcessingResult(Path rootLocation){
        this.rootLocation = rootLocation;
    }

    public List<String> processing(){
        List<String> args = prepareArguments();
        String script = getScript();

        args.add(0, script);
        if (System.getProperty("os.name").startsWith("Windows"))
            args.add(0, "C:\\python3\\python3.exe");
        else
            args.add(0, "python3");

        return startProcess(args);
    }

    abstract String getScript();

    abstract List<String> prepareArguments();

    public List<String> startProcess(List<String> command) throws StorageException {
        List<String> result;
        try {
            Process process = new ProcessBuilder(command).start();
            result = new BufferedReader(new InputStreamReader(process.getInputStream()))
                    .lines()
                    .collect(Collectors.toList());
            String errorStr = new BufferedReader(new InputStreamReader(process.getErrorStream()))
                    .lines()
                    .toString();

            var returnValue = process.waitFor();

            if (returnValue != 0)
                throw new InterruptedException(String.format("Return code is: %d\n", returnValue)
                        + errorStr + "\n" + command.stream().reduce(String::concat) + "\n"
                        + result);
        } catch (IOException | InterruptedException e) {
            throw new StorageException(e.getMessage());
        }

        return result;
    }
}
