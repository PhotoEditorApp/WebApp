package com.webapp.imageprocessing;

import com.webapp.domain.UserImage;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Collage extends ProcessingProduct{
    private final List<UserImage> userImageList;

    public Collage(Path rootLocation, List<UserImage> userImageList) {
        super(rootLocation);
        this.userImageList = userImageList;
    }

    @Override
    String getScript() {
        return Paths.get("scripts/make_collage.py").normalize().toAbsolutePath().toString();
    }

    @Override
    String getProductPath() {
        String collageName = String.format("collage%d.jpg", new Random().nextInt());
        return rootLocation.resolve(collageName).normalize().toString();
    }

    @Override
    List<String> prepareArguments() {
        return userImageList.stream()
                .map(UserImage::getName)
                .map(path -> rootLocation.resolve(path).normalize().toAbsolutePath().toString())
                .collect(Collectors.toList());
    }
}
