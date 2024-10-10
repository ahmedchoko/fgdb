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
import java.util.List;
/**
 * Bank Model DataTable
 *
 * @author  knh
 *
 */
@Entity
@Table(name = "GR028T_BANQUES")
@Getter
@Setter
public class Bank  implements Serializable {

    /**
     * Serial Number
     */
    private static final long serialVersionUID = -8115650653859707569L;



    /**
     * Bank id.
     */
    @Id
    @Column(name = "GR028BQ", updatable = false, nullable = false)
    private String id;


    /**
     * Bank's code
     */
    @Column(name = "GR028ABR")
    private String code;

    /**
     * Bank's label
     */
    @Column(name = "GR028LIB")
    private String label;

    /**
     * Bank's label
     */
    @Column(name = "GR028LIB_EN")
    private String label_EN;

    /**
     * Bank's agencies
     * Bank's agencies
     */
    @OneToMany(mappedBy = "bank", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Agency> agency;

    /**
     * Get the label based on language.
     */
//    public String getLabel() {
//        String language = LanguageContext.getLanguage();
//        return "FR".equals(language) ? label : label_EN;
//    }
//    /**
//     * Get the label.
//     */
//    public String getLabelFr() {
//        return label;
//    }
}
