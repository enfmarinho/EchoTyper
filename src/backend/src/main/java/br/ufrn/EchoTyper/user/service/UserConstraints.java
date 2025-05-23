package br.ufrn.EchoTyper.user.service;


public class UserConstraints {
   public static final String USERNAME_PATTERN = "^[^*/$#%&-]*$";
   
   public static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
}
