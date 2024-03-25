package dev.guiga.proj1.user_management;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(unique = true)
    @NotBlank(message = "username is mandatory")
    private String username;

    @Column
    @NotBlank(message = "password is mandatory")
    private String password;

    @Column
    @Builder.Default
    private Integer totalLogins = 0;

    @Column
    @Builder.Default
    private Integer totalFailedLogins = 0;

    @Column
    @Builder.Default
    private boolean blocked = false;

}
