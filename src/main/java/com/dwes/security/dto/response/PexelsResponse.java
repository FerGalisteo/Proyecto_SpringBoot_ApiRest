package com.dwes.security.dto.response;

import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class PexelsResponse implements Serializable {

    private static final long serialVersionUID = 1L;
	@JsonProperty("photos")
    private List<Photo> photos;

    // Getters y setters

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Photo implements Serializable {

        private static final long serialVersionUID = 1L;
 
		@JsonProperty("id")
        private int id;

        @JsonProperty("url")
        private String url;

        @JsonProperty("photographer")
        private String photographer;

        @JsonProperty("src")
        private Src src;

        // Getters y setters

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getPhotographer() {
            return photographer;
        }

        public void setPhotographer(String photographer) {
            this.photographer = photographer;
        }

        public Src getSrc() {
            return src;
        }

        public void setSrc(Src src) {
            this.src = src;
        }
        
        @Override
        public String toString() {
            return "Photo{" +
                    "id=" + id +
                    ", url='" + url + '\'' +
                    ", photographer='" + photographer + '\'' +
                    ", src=" + src +
                    '}';
        }
    }
 
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Src implements Serializable {

        private static final long serialVersionUID = 1L;
		@JsonProperty("original")
        private String original;

        // Getters y setters

        public String getOriginal() {
            return original;
        }

        public void setOriginal(String original) {
            this.original = original;
        }
        @Override
        public String toString() {
            return "Src{" +
                    "original='" + original + '\'' +
                    '}';
        }
    }
    
 // Sobrescribe el método toString
    @Override
    public String toString() {
        return "PexelsResponse{" +
                "photos=" + photos +
                '}';
    }
}