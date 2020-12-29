package com.webapp.imageprocessing;

import com.webapp.domain.UserImage;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public abstract class Filter extends ProcessingProduct{
    protected final UserImage userImage;

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

    abstract List<String> prepareArguments();
}
