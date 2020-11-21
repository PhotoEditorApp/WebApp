package com.webapp.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "average_color")
public class AverageColor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int rgb;

    public AverageColor() {
    }

    public AverageColor(int rgb) {
        this.rgb = rgb;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRgb() {
        return rgb;
    }

    public void setRgb(int rgb) {
        this.rgb = rgb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AverageColor that = (AverageColor) o;
        return rgb == that.rgb &&
                id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rgb);
    }
}