package org.example.entity;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.jsonView.BankAccountView;
import org.example.entity.jsonView.UserView;

import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BankAccount implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonView(BankAccountView.Admin.class)
    private UUID uuid;

    @JsonView(BankAccountView.Basic.class)
    private String bankOrigin;

    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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
