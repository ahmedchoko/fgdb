package com.wevioo.fgdb.referenciel.domain;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Entity
@Table(name = "GR002T_GOUVERNORATS")
@Getter
@Setter
public class Governorate implements Serializable {

    /**
     * Serial Number
     */
    private static final long serialVersionUID = 6773432293523526139L;

    /**
     * Governorate's id.
     */
    @Id
    @Column(name = "GR002GOUV", updatable = false, nullable = false, length = 2)
    private String id;

    /**
     * Governorate's label
     */
    @Column(name = "GR002LIB", length = 50)
    private String label;

    /**
     * Governorate's regeco
     */
    @Column(name = "GR002REGECO", length = 2)
    private String regeco;

    /**
     * Governorate's ctoIR
     */
    @Column(name = "GR049CTOIR", length = 3)
    private String ctoIR;


    /**
     * Governorate's sup
     */
    @Column(name = "GR002SUP", length = 1)
    private String sup;



}
