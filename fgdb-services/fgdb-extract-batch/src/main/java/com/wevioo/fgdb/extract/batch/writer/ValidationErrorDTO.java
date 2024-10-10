package com.wevioo.fgdb.extract.batch.writer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ValidationErrorDTO {
    private String propertyPath;
    private Object invalidValue;
    private String message;
}
