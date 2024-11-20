package org.example.roomgitservice.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "rooms")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nameOfGroup;

    @OneToMany(mappedBy = "registeredGroup", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    @Builder.Default
    private List<Developer> developers  = new ArrayList<>();;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "developer_id")
    private Developer owner;

}
