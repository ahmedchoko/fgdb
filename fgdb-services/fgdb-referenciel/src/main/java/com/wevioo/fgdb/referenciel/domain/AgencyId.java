package com.wevioo.fgdb.referenciel.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgencyId  implements Serializable {

    /**
     * Serial Number
     */
    private static final long serialVersionUID = 4750301741929883181L;

    /**
     * AgencyId's id.
     */

    @Column(name = "GR138AG", updatable = false, nullable = false)
    private String agencyId;

    /**
     * AgencyId's id.
     */

    @Column(name = "GR028BQ", updatable = false, nullable = false)
    private String bankId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AgencyId agencyId1 = (AgencyId) o;

        if (!agencyId.equals(agencyId1.agencyId)) return false;
        return bankId.equals(agencyId1.bankId);
    }

    @Override
    public int hashCode() {
        int result = agencyId.hashCode();
        result = 31 * result + bankId.hashCode();
        return result;
    }
}
