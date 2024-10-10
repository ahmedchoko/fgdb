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
 * Country Model DataTable
 *
 * @author shl
 *
 */
@Entity
@Table(name = "GR030T_PAYS")
@Getter
@Setter
public class Country implements Serializable {


    /**
     * Serial Number
     */
    private static final long serialVersionUID = -8892641119788252565L;

    /**
     * Country id.
     */
    @Id
    @Column(name = "GR030PAYS", updatable = false, nullable = false)
    private String id;

    /**
     * Country's code
     */
    @Column(name = "GR030ABR1")
    private String code;

    /**
     * Country's code2
     */
    @Column(name = "GR030ABR2")
    private String code2;

    /**
     * Country's label
     */
    @Column(name = "GR030LIB")
    private String label;
    /**
     * Country's labelEn
     */
    @Column(name = "GR030LIB_EN")
    private String labelEn; 

    /**
     * Country's  Nationality
     */
    @Column(name = "GR030NALITE")
    private String nationality;
    
    /**
     * Country's  nationalityEn
     */
    @Column(name = "GR030NALITE_EN")
    private String nationalityEn;

//    /**
//     * Get the label based on language.
//     */
//    public String getLabel() {
//        String language = LanguageContext.getLanguage();
//        return "FR".equals(language) ? label : labelEn;
//    }
//    /**
//     * Get the label.
//     */
//    public String getLabelFr() {
//        return label;
//    }
//
//    /**
//     * Get the label based on language.
//     */
//    public String getNationality() {
//        String language = LanguageContext.getLanguage();
//        return "FR".equals(language) ? nationality : nationalityEn;
//    }
//    /**
//     * Get the label.
//     */
//    public String getNationalityFr() {
//        return nationality;
//    }
}
