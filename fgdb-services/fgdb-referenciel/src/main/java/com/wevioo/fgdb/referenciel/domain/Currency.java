package com.wevioo.fgdb.referenciel.domain;

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

/**
 * Currency Model DataTable
 *
 *
 * @author shl
 *
 */
@Entity
@Table(name = "GR027T_DEVISES")
@Getter
@Setter
public class Currency implements Serializable {


    /**
     * Serial Number
     */
    private static final long serialVersionUID = 7815776217790697672L;

    /**
     * Currency's id.
     */
    @Id
    @Column(name = "GR027DEVISE", updatable = false, nullable = false)
    private String id;


    /**
     * Currency's code
     */
    @Column(name = "GR027ABR")
    private String code;

    /**
     * Currency's label
     */
    @Column(name = "GR027LIB")
    private String label;


    /**
     * label En
     */
    @Column(name = "GR027LIB_EN")
    private String labelEn;


//    /**
//     * Get the label based on language.
//     */
//    public String getLabel() {
//        String language = LanguageContext.getLanguage();
//        return "FR".equals(language) ? label : labelEn;
//    }
    /**
     * Get the label.
     */
    public String getLabelFr() {
        return label;
    }
}
