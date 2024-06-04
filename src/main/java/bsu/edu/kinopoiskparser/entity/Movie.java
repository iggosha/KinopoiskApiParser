package bsu.edu.kinopoiskparser.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@Table(name = "movies")
public class Movie {

    @Id
    @EqualsAndHashCode.Exclude
    private Long id;

    private String name;

    private String alternativeName;

    private String enName;

    private String type;

    private Integer year;

    private String description;

    private String shortDescription;

    private String slogan;

    private String status;

    private Integer movieLength;

    private Integer ageRating;

    private Boolean ticketsOnSale;

}