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
@Table(name = "GR004T_LOCALITES")
@Getter
@Setter
public class Location implements Serializable {

    /**
     * Serial Number
     */
    private static final long serialVersionUID = 8865630621020254259L;

    /**
     * Location's id.
     */

    @EmbeddedId
    private LocationID id;

    /**
     * Location's label
     */
    @Column(name = "GR004LIB", length = 50)
    private String label;

    /**
     * Location's is zone AVT
     */
    @Column(name = "GR004ZONAVT")
    private Boolean zoneAVT;

    /**
     * Location's sup
     */
    @Column(name = "GR004SUP", length = 1)
    private String sup;

    /**
     * Location's compositeId
     */
    @jakarta.persistence.Transient
    private String compositeId;

    /**
     * Setter compositeId
     *
     * @param id LocationID
     */
    public void setCompositeId(LocationID id) {
        this.compositeId = id.toString();
    }
}
