package com.wevioo.fgdb.extract.batch.processor;

import com.wevioo.fgdb.extract.batch.configuration.CountryService;
import com.wevioo.fgdb.extract.batch.configuration.ErrorConfiguration;
import com.wevioo.fgdb.extract.batch.configuration.ErrorConfigurationRepository;
import generated.Vuc;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class XmlFileItemProcessor implements ItemProcessor<Vuc, XmlFileItemProcessor.ValidationResult> {





    private final AtomicBoolean stopCondition;
    private final AtomicInteger counter;  // Counter to track processed items

    @Autowired
    public CountryService countryService;



    public
    XmlFileItemProcessor(AtomicBoolean stopCondition, AtomicInteger counter) {
        this.stopCondition = stopCondition;
        this.counter = counter;
    }

    @Override
    public ValidationResult process(Vuc vuc) throws Exception {

        // Check stop condition before processing
        if (stopCondition.get()) {
            throw new JobInterruptedException("Job stopped due to the stop condition being met.");
        }
        List<ValidationError> validationErrors = new ArrayList<>();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        HashMap<String ,  List<ValidationError> >  validationErrorsDepositors = new HashMap<>();



        ////// Increment the counter
        int currentCount = counter.incrementAndGet();
        log.info("Processed item count: " + currentCount);

        ////// If count exceeds 2
//        if (currentCount > 3) {
//            stopCondition.set(true);
//            throw new JobInterruptedException("Stop condition met, halting processing.");
//        }errorConfigurationRepository
//        List<ErrorConfiguration> errorConfigurations = new ArrayList<>();
//        System.out.print("size " + errorConfigurations.size());
        validateVuc(vuc, validator, validationErrors , validationErrorsDepositors);
        return new ValidationResult(vuc, validationErrors , validationErrorsDepositors);
    }

    private void validateVuc(Vuc vuc, Validator validator, List<ValidationError> validationErrors ,   HashMap<String ,  List<ValidationError> >  validationErrorsDepositors ) {
        ////// Validate Vuc object itself
        Set<ConstraintViolation<Vuc>> violations = validator.validate(vuc);
        Map<String, String> errorConfigurations = countryService.getErrorsAsMap();

                System.out.print("errorConfigurations " + errorConfigurations.size());

        for (ConstraintViolation<Vuc> violation : violations) {
            String violationMessage = violation.getMessage();

            for (Map.Entry<String, String> entry : errorConfigurations.entrySet()) {
                String errorMessage = entry.getKey();
                String error = entry.getValue();
                if (violationMessage.contains(errorMessage)) {
                    validationErrors.add(new ValidationError(
                            violation.getPropertyPath().toString(),
                            violation.getInvalidValue(),
                            violationMessage,
                            error // Add the corresponding error code
                    ));
                }
            }
        }

        /// Validate Depositors
        if (vuc.getDepositors() != null) {
            validateDepositors(vuc.getDepositors(), validator, validationErrors , validationErrorsDepositors );
        }
    }

    private void validateDepositors(Vuc.Depositors depositors, Validator validator, List<ValidationError> validationErrors ,   HashMap<String ,  List<ValidationError> >  validationErrorsDepositors) {
        //// Validate Depositors object itself
        Map<String, String> errorConfigurations = countryService.getErrorsAsMap();
        Set<ConstraintViolation<Vuc.Depositors>> violations = validator.validate(depositors);
        for (ConstraintViolation<Vuc.Depositors> violation : violations) {
            String violationMessage = violation.getMessage();

            for (Map.Entry<String, String> entry : errorConfigurations.entrySet()) {
                String errorMessage = entry.getKey();
                String error = entry.getValue();

                if (violationMessage.contains(errorMessage)) {
                    validationErrors.add(new ValidationError(
                            violation.getPropertyPath().toString(),
                            violation.getInvalidValue(),
                            violationMessage,
                            error // Add the corresponding error code
                    ));
                }
            }
        }

        ///// Validate each Depositor
        if (depositors.getDepositor() != null) {
            for (Vuc.Depositors.Depositor depositor : depositors.getDepositor()) {
                validateDepositor(depositor, validator, validationErrorsDepositors    );
            }
        }
    }

    private void validateDepositor(Vuc.Depositors.Depositor depositor, Validator validator,  HashMap<String ,  List<ValidationError> >  validationErrorsDepositors ) {
        ///// Validate Depositor object itself
        String depositorId = UUID.randomUUID().toString();
        List<ValidationError> validationErrors =  new ArrayList<>();
        Set<ConstraintViolation<Vuc.Depositors.Depositor>> violations = validator.validate(depositor);
        Map<String, String> errorConfigurations = countryService.getErrorsAsMap();
        for (ConstraintViolation<Vuc.Depositors.Depositor> violation : violations) {
            String violationMessage = violation.getMessage();
            for (Map.Entry<String, String> entry : errorConfigurations.entrySet()) {
                String errorMessage = entry.getKey();
                String error = entry.getValue();
                if (errorMessage.contains(violationMessage)) {
                    validationErrors.add(new ValidationError(
                            violation.getPropertyPath().toString(),
                            violation.getInvalidValue(),
                            violationMessage,
                            error // Add the corresponding error code
                    ));
                }
            }
        }

        //// Validate ContactDetails if present
        if (depositor.getContactDetails() != null) {
            validateContactDetails(depositor.getContactDetails(), validator, validationErrors );
        }
        if (depositor.getDepositorIdentificationPp() != null) {
            validateDepositorIdentificationPP(depositor.getDepositorIdentificationPp(), validator, validationErrors );
        }
        if (depositor.getDepositorIdentificationPm() != null) {
            validateDepositorIdentificationPM(depositor.getDepositorIdentificationPm(), validator, validationErrors );
        }
        if (depositor.getAccountsIdentification() != null) {
            validateAccountsIdentification(depositor.getAccountsIdentification(), validator, validationErrors );
        }
        validationErrorsDepositors.put(depositorId, validationErrors);
    }
    private void validateDepositorIdentificationPP(Vuc.Depositors.Depositor.DepositorIdentificationPp depositorIdentificationPp, Validator validator, List<ValidationError> validationErrors ) {
        ////// Validate ContactDetails object itself
        Map<String, String> errorConfigurations = countryService.getErrorsAsMap();

        Set<ConstraintViolation<Vuc.Depositors.Depositor.DepositorIdentificationPp>> violations = validator.validate(depositorIdentificationPp);
        for (ConstraintViolation<Vuc.Depositors.Depositor.DepositorIdentificationPp> violation : violations) {
            String violationMessage = violation.getMessage();
            for (Map.Entry<String, String> entry : errorConfigurations.entrySet()) {
                String errorMessage = entry.getKey();
                String error = entry.getValue();
                if (violationMessage.contains(errorMessage)) {
                    validationErrors.add(new ValidationError(
                            violation.getPropertyPath().toString(),
                            violation.getInvalidValue(),
                            violationMessage,
                            error // Add the corresponding error code
                    ));
                }
            }
        }
    }

    private void validateDepositorIdentificationPM(Vuc.Depositors.Depositor.DepositorIdentificationPm depositorIdentificationPm, Validator validator, List<ValidationError> validationErrors ) {
        ////// Validate ContactDetails object itself
        Map<String, String> errorConfigurations = countryService.getErrorsAsMap();
        Set<ConstraintViolation<Vuc.Depositors.Depositor.DepositorIdentificationPm>> violations = validator.validate(depositorIdentificationPm);
        for (ConstraintViolation<Vuc.Depositors.Depositor.DepositorIdentificationPm> violation : violations) {
            String violationMessage = violation.getMessage();
            for (Map.Entry<String, String> entry : errorConfigurations.entrySet()) {
                String errorMessage = entry.getKey();
                String error = entry.getValue();

                if (violationMessage.contains(errorMessage)) {
                    validationErrors.add(new ValidationError(
                            violation.getPropertyPath().toString(),
                            violation.getInvalidValue(),
                            violationMessage,
                            error // Add the corresponding error code
                    ));
                }
            }
        }
    }


    private void validateAccountsIdentification(Vuc.Depositors.Depositor.AccountsIdentification accountsIdentification, Validator validator, List<ValidationError> validationErrors ) {
        ////// Validate ContactDetails object itself
        Map<String, String> errorConfigurations = countryService.getErrorsAsMap();
        Set<ConstraintViolation<Vuc.Depositors.Depositor.AccountsIdentification>> violations = validator.validate(accountsIdentification);
        for (ConstraintViolation<Vuc.Depositors.Depositor.AccountsIdentification> violation : violations) {
            String violationMessage = violation.getMessage();
            for (Map.Entry<String, String> entry : errorConfigurations.entrySet()) {
                String errorMessage = entry.getKey();
                String error = entry.getValue();

                if (violationMessage.contains(errorMessage)) {
                    validationErrors.add(new ValidationError(
                            violation.getPropertyPath().toString(),
                            violation.getInvalidValue(),
                            violationMessage,
                            error // Add the corresponding error code
                    ));
                }
            }
        }
    }


    private void validateContactDetails(Vuc.Depositors.Depositor.ContactDetails contactDetails, Validator validator, List<ValidationError> validationErrors ) {
        ////// Validate ContactDetails object itself
        Map<String, String> errorConfigurations = countryService.getErrorsAsMap();
        Set<ConstraintViolation<Vuc.Depositors.Depositor.ContactDetails>> violations = validator.validate(contactDetails);
        for (ConstraintViolation<Vuc.Depositors.Depositor.ContactDetails> violation : violations) {
            String violationMessage = violation.getMessage();
            for (Map.Entry<String, String> entry : errorConfigurations.entrySet()) {
                String errorMessage = entry.getKey();
                String error = entry.getValue();

                if (violationMessage.contains(errorMessage)) {
                    validationErrors.add(new ValidationError(
                            violation.getPropertyPath().toString(),
                            violation.getInvalidValue(),
                            violationMessage,
                            error // Add the corresponding error code
                    ));
                }
            }
        }
    }

    @Getter
    @ToString
    public static class ValidationError {
        private String propertyPath;
        private Object invalidValue;
        private String message;
        private String errorCode ;

        public ValidationError(String propertyPath, Object invalidValue, String message ,String errorCode) {
            this.propertyPath = propertyPath;
            this.invalidValue = invalidValue;
            this.message = message;
            this.errorCode = errorCode;
        }

    }

    @Getter
    public static class ValidationResult {
        private Vuc vuc;
        private List<ValidationError> validationErrors;
        private HashMap<String ,  List<ValidationError> >  validationErrorsDepositors;

        public ValidationResult(Vuc vuc, List<ValidationError> validationErrors , HashMap<String ,  List<ValidationError> >  validationErrorsDepositors) {
            this.vuc = vuc;
            this.validationErrors = validationErrors;
            this.validationErrorsDepositors = validationErrorsDepositors;
        }

    }
}
