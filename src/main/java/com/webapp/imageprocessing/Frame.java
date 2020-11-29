package com.webapp.imageprocessing;

import com.webapp.domain.UserImage;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Frame extends ProcessingProduct{
    private final UserImage userImage;

    public Frame(Path rootLocation, UserImage userImage) {
        super(rootLocation);
        this.userImage = userImage;
    }

    @Override
    String getScript() {
        return Paths.get("scripts/add_frame.py").normalize().toAbsolutePath().toString();
    }

    @Override
    String getProductPath() {
        String collageName = String.format("with_frame_%s.jpg", userImage.getName().split("\\.")[0]);
        return rootLocation.resolve(collageName).normalize().toString();
    }

    @Override
    List<String> prepareArguments() {
        return new ArrayList<>(
            List.of(rootLocation.resolve(userImage.getName()).normalize().toAbsolutePath().toString())
        );
    }
}
