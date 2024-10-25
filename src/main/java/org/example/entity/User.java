package org.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.Data;
import org.example.entity.jsonView.BankAccountView;
import org.example.entity.jsonView.UserView;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity()
@Table(name = "user")
@Data
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    @JsonView(UserView.Admin.class)
    private UUID id;

    @Column(nullable = false)
    @JsonView(UserView.Detailed.class)
    private String name;

    @Column(nullable = false)
    @JsonView(UserView.Detailed.class)
    private String email;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<BankAccount> userAccounts;

}
