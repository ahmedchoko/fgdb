package com.wevioo.fgdb.extract.batch.configuration;

import com.wevioo.fgdb.extract.batch.feign.CountryDto;
import generated.Vuc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DynamicValidationService {

    @Autowired
    private ValidationRuleService validationRuleService;

    @Autowired
    private CountryService countryService;


    private SpelExpressionParser parser = new SpelExpressionParser();

    public
    String validate(List<Vuc.Depositors.Depositor> depositors) {
        if (depositors == null || depositors.isEmpty()) {
            throw new IllegalArgumentException("Depositors list cannot be null or empty.");
        }

        // Get validation rules for the Depositor class
        String entityName = Vuc.Depositors.Depositor.class.getSimpleName();
        List<ValidationRule> rules = validationRuleService.getValidationRules(entityName);
        List<CountryDto> countries =  new ArrayList<>();
                ///countryService.getAllCountries();

        // Get the Tunisian nationality code from the list of countries
        BigDecimal tunisienCode = new BigDecimal(countries.stream()
                .filter(country -> "TUNISIENNE".equalsIgnoreCase(country.getNationality()))
                .map(CountryDto::getId)
                .findFirst().get());

        Map<Vuc.Depositors.Depositor, List<String>> validationErrors = new HashMap<>();

        for (Vuc.Depositors.Depositor depositor : depositors) {
            // Create a new evaluation context for each depositor
            StandardEvaluationContext context = new StandardEvaluationContext(depositor);
            context.setVariable("tunisianNationalityCode", tunisienCode);
            List<String> errors = new ArrayList<>();
            for (ValidationRule rule : rules) {

                boolean isValid = Boolean.TRUE.equals(parser.parseExpression(rule.getValidationExpression())
                        .getValue(context, Boolean.class));
                if (!isValid) {
                    String errorMessage = getErrorMessageForFieldGroup(rule.getFieldName());
                    errors.add(errorMessage);
                }
            }
            if (!errors.isEmpty()) {
                validationErrors.put(depositor, errors);
            }
        }

        if (!validationErrors.isEmpty()) {
            StringBuilder errorMessageBuilder = new StringBuilder("Validation failed for the following depositors:\n");
            validationErrors.forEach((depositor, errors) -> {
                errorMessageBuilder.append("Depositor: ").append(depositor.getDepositorIdentificationPp().getCinNum())
                        .append("\nErrors: ").append(String.join(", ", errors))
                        .append("\n");
            });
            return errorMessageBuilder.toString();
          // throw new IllegalArgumentException(errorMessageBuilder.toString());
        }
        return entityName;
    }


    public String getErrorMessageForFieldGroup(String fieldName) {
        Map<String, String> fieldGroupMessages = Map.of(
                "cin_passport_carteSejour", "One of CIN, Passport, or Carte de Sejour must be provided.",
                "nationality", "If CIN is provided, the nationality must be Tunisian.",
                "cinIssueDate", "If CIN is provided, the date of issuance must be in the future."

        );
        return fieldGroupMessages.getOrDefault(fieldName, "Validation failed for field group: " + fieldName);
    }

}
