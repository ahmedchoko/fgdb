package com.wevioo.fgdb.referenciel.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class DelegationID implements Serializable {
    /**
     * Serial Number
     */
    private static final long serialVersionUID = 4750301741929883181L;

    /**
     * LocationID's id.
     */

    @Column(name = "GR002GOUV", updatable = false, nullable = false, length = 2)
    private String governorateId;

    /**
     * LocationID's id.
     */

    @Column(name = "GR003DELEG", updatable = false, nullable = false, length = 2)
    private String delegationId;
}
