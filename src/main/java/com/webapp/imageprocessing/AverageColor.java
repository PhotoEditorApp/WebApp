package com.webapp.imageprocessing;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AverageColor extends ProcessingResult{
    private final String imageName;

    public AverageColor(Path rootLocation, String imageName) {
        super(rootLocation);
        this.imageName = imageName;
    }

    @Override
    String getScript() {
        return Paths.get("scripts/count_average_color.py")
                .normalize()
                .toAbsolutePath()
                .toString();
    }

    @Override
    List<String> prepareArguments() {
        return new ArrayList<>(
                List.of(rootLocation.resolve(imageName).normalize().toAbsolutePath().toString())
        );
    }
}
