package com.wevioo.fgdb.referenciel.rest.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class CountryDto implements Serializable {


    /**
     * Serial Number
     */

    private static final long serialVersionUID = -6701940749904713076L;


    /**
     * Country id.
     */

    private String id;

    /**
     * Country's code
     */
    private String code;

    /**
     * Country's code2
     */
    private String code2;

    /**
     * Country's label
     */
    private String label;

    /**
     * Country's  Nationality
     */
    private String nationality;

}
