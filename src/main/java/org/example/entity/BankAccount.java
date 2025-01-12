package org.example.entity;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.entity.jsonView.BankAccountView;
import org.example.entity.jsonView.UserView;

import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@SuperBuilder
public abstract class BankAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonView(BankAccountView.Admin.class)
    private UUID uuid;

    @JsonView(BankAccountView.Basic.class)
    private String bankOrigin;

    @JsonView(BankAccountView.Basic.class)
    @Column(unique = true)
    private Long number;

    @JsonView(BankAccountView.Basic.class)
    private String agency;

    @JsonView(BankAccountView.Detailed.class)
    private Long balance;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "user_id")
    @JsonView(UserView.Admin.class)
    private User user;



}
