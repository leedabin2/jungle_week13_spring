package com.jungle.week13.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
public class ErrorResponse {

    private List<ErrorField> errors;

    public ErrorResponse(List<ErrorField> errors) {
        this.errors = errors;
    }

    public static ErrorResponse of(BindingResult bindingResult) {
        return new ErrorResponse(ErrorField.of(bindingResult));
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ErrorField {
        private String field;
        private String value;
        private String reaseon;

        public static List<ErrorField> of(BindingResult bindingResult) {
            List<ErrorField> errorFields = bindingResult.getAllErrors().stream()
                    .map(error ->
                    new ErrorField(((FieldError) error).getField(), ((FieldError) error).getRejectedValue().toString(),
                            ((FieldError) error).getDefaultMessage())).collect(Collectors.toList());
            return errorFields;
        }
    }
}
