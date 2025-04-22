package br.ufrn.EchoTyper.user.dto;


import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.ufrn.EchoTyper.user.UserConstraints;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserRequestDTO(
        @NotNull
        Long id,
        @Nullable
        @JsonProperty("username")
        @Length(min = 6, max = 50)
        @Pattern(regexp = UserConstraints.USERNAME_PATTERN) 
        String username,
        @JsonProperty("email") 
        @Nullable 
        @Pattern(regexp = UserConstraints.EMAIL_PATTERN) 
        String email,
        @Length(min=6, max=50)
        String password
        )

{

}