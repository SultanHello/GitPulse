package org.example.roomgitservice.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
public class Developer{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String email;
    private String password;
    private String gitUsername;

    @OneToOne(mappedBy = "owner")
    private Group ownGroup;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")

    private Group registeredGroup;



}

