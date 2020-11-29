package com.webapp.imageprocessing;

import com.webapp.domain.UserImage;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Filter extends ProcessingProduct{
    private final UserImage userImage;

    public Filter(Path rootLocation, UserImage userImage) {
        super(rootLocation);
        this.userImage = userImage;
    }

    @Override
    String getScript() {
        return Paths.get("scripts/apply_filter.py").normalize().toAbsolutePath().toString();
    }

    @Override
    String getProductPath() {
        String collageName = String.format("filtered_%s.jpg", userImage.getName().split("\\.")[0]);
        return rootLocation.resolve(collageName).normalize().toString();
    }

    @Override
    List<String> prepareArguments() {
        return new ArrayList<>(
            List.of(rootLocation.resolve(userImage.getName()).normalize().toAbsolutePath().toString())
        );
    }
}
