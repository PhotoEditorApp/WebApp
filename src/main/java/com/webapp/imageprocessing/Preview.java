package com.webapp.imageprocessing;

import com.webapp.domain.Picture;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Preview extends ProcessingProduct{
    private final Picture userImage;

    public Preview(Path rootLocation, Picture userImage){
        super(rootLocation);
        this.userImage = userImage;
    }

    @Override
    String getScript() {
        return Paths.get("scripts/make_preview.py").normalize().toAbsolutePath().toString();
    }

    @Override
    String getProductPath() {
        String previewName = String.format("preview_%s.jpg", userImage.getName().split("\\.")[0]);
        return rootLocation.resolve(previewName).normalize().toString();
    }

    @Override
    List<String> prepareArguments() {
        return new ArrayList<>(
                List.of(rootLocation.resolve(userImage.getName()).normalize().toAbsolutePath().toString())
        );
    }
}
