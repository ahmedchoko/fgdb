package com.wevioo.fgdb.referenciel.domain;

import jakarta.persistence.EmbeddedId;
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
@Table(name = "GR005T_CPOSTAUX")
@Getter
@Setter
public class ZipCode implements Serializable {

    /**
     * Serial Number
     */
    private static final long serialVersionUID = 8865630621020254259L;

    /**
     * ZipCode's id.
     */

    @EmbeddedId
    private ZipCodeID id;

    /**
     * ZipCode's label
     */
    @Column(name = "GR005LIB", length = 50)
    private String label;

    /**
     * ZipCode's sup
     */
    @Column(name = "GR005SUP", length = 1)
    private String sup;

}
