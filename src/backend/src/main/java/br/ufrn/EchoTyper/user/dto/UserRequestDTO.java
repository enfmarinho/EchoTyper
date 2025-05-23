package br.ufrn.EchoTyper.user.dto;


import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.ufrn.EchoTyper.user.service.UserConstraints;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserRequestDTO(
        @NotNull
        @JsonProperty("username")
        @Length(min = 6, max = 50)
        @Pattern(regexp = UserConstraints.USERNAME_PATTERN) 
        String username,
        @JsonProperty("email") 
        @NotNull
        @Pattern(regexp = UserConstraints.EMAIL_PATTERN) 
        String email,
        @JsonProperty("password")
        @Length(min=8, max=50)
        String password
        )

{

}