package com.khaledmosharraf.twtms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@Table(name = "districts")
public class District extends Autditable{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "district_sequence")
    @SequenceGenerator(name = "district_sequence", sequenceName = "DISTRICT_SEQUENCE", allocationSize = 1)

    private Long id;
    private String eName;
    private String bName;

    @OneToMany(mappedBy = "district", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    List<SubDistrict> subDistricts ;
    public District() {
        this.subDistricts = new ArrayList<>();
    }

}
