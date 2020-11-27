package com.webapp.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "average_color")
public class AverageColor implements Serializable {
    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE,
                     generator = "image_seq")
    @SequenceGenerator(name="image_seq", sequenceName = "image_id_seq", allocationSize = 1)
    private Long id;

    private long rgb;

    public AverageColor() {
    }

    public AverageColor(long rgb) {
        this.rgb = rgb;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getRgb() {
        return rgb;
    }

    public void setRgb(long rgb) {
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