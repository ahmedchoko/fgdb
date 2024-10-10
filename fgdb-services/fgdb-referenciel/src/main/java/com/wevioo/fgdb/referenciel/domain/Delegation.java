package com.wevioo.fgdb.referenciel.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Entity
@Table(name = "GR003T_DELEGATIONS")
@Getter
@Setter
public class Delegation implements Serializable {

    /**
     * Serial Number
     */
    private static final long serialVersionUID = 8865630621020254259L;

    /**
     * Delegation's id.
     */

    @EmbeddedId
    private DelegationID id;

    /**
     * Delegation's label
     */
    @Column(name = "GR003LIB", length = 50)
    private String label;

    /**
     * Delegation's sup
     */
    @Column(name = "GR003SUP", length = 1)
    private String sup;
}
