package com.blu.imdg.nosql.model;

import java.time.LocalDate;
import java.util.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
public class MongoPost {
    @Id
    private String id;
    private String title;
    private String description;
    //private LocalDate creationDate;
    private String author;

    public MongoPost() {
    }

    public MongoPost(String id, String title, String description, /*LocalDate creationDate,*/ String author) {
        this.id = id;
        this.title = title;
        this.description = description;
        //this.creationDate = creationDate;
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public LocalDate getCreationDate() {
//        return creationDate;
//    }
//
//    public void setCreationDate(LocalDate creationDate) {
//        this.creationDate = creationDate;
//    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MongoPost post = (MongoPost)o;
        return Objects.equals(id, post.id) &&
            Objects.equals(title, post.title) &&
            Objects.equals(description, post.description) &&
            //Objects.equals(creationDate, post.creationDate) &&
            Objects.equals(author, post.author);
    }

    @Override public int hashCode() {
        return Objects.hash(id, title, description, /*creationDate, */author);
    }

    @Override public String toString() {
        return "MongoPost{" +
            "id='" + id + '\'' +
            ", title='" + title + '\'' +
            ", description='" + description + '\'' +
            /*", creationDate=" + creationDate +*/
            ", author='" + author + '\'' +
            '}';
    }
}
